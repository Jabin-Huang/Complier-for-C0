package demo.parser;

import demo.inter.Node;
import demo.inter.expr.*;
import demo.inter.expr.logical.And;
import demo.inter.expr.logical.Not;
import demo.inter.expr.logical.Or;
import demo.inter.expr.logical.Rel;
import demo.inter.expr.op.Access;
import demo.inter.expr.op.Arith;
import demo.inter.expr.op.Unary;
import demo.inter.stmt.*;
import demo.lexer.*;
import demo.symbols.Array;
import demo.symbols.Env;
import demo.symbols.Type;

import java.io.IOException;
import java.util.Vector;


public class Parser {
    private Lexer lex;
    private Token look; //向前看词法单元
    private Type savedProcType; //用于return的类型检查

    public Parser(Lexer l) throws IOException {
        lex = l;
        move();
    }

    private void move() throws IOException {
        look = lex.scan();
    }

    private void error(String s) {
        throw new Error("near line " + Lexer.line + ": " + s);
    }

    private void match(int t) throws IOException {
        if (look.tag == t) move();
        else {
            String expect = (t > 255) ? Tag.tagToString.get(t) : "" + (char) t;
            error("syntax error: expected \"" + expect + "\", but found \"" + look.toString() + "\"");
        }
    }


    //program -> process program | e
    public void progarm() throws IOException {
        //顶层符号表
        Env.top = new Env(Env.top);
        do {
            process();
        } while (look.tag == Tag.BASIC);
    }

    //process -> type id (parameter) block
    private void process() throws IOException {
        Token fun_t = look;
        match(Tag.BASIC);
        Token fun_w = look;
        match(Tag.ID);
        match('(');

        Vector<Type> param = new Vector<>();
        //函数名在顶层符号表， 形参在相应函数块的符号表
        Env.push(Env.top);
        Env.top = new Env(Env.top);
        Env next = Env.top;
        // parameter -> type id ids | e
        // ids -> , type id ids | e
        while (look.tag == Tag.BASIC) {

            Token param_t = look;
            match(Tag.BASIC);
            param.add((Type) param_t);

            Token id_word = look;
            match(Tag.ID);
            if (look.tag == ',') match(',');
            //形参信息加入到相应函数块的符号表里
            Id id = new Id((Word) id_word, (Type) param_t, Env.top.used);
            Env.top.putId((Word) id_word, id);
            Env.top.used = Env.top.used + ((Type) param_t).width;
        }
        match(')');
        Env.top = Env.pop();
        //函数名加入顶层符号表
        Function func = new Function((Word) fun_w, (Type) fun_t, param);
        Env.putFunc((Word) fun_w, func);
        savedProcType = (Type) fun_t; //用于return类型检查

        Stmt s = block(next);
        System.out.println(s.AST_str(0));

        //int begin = s.newlabel();
        //int after = s.newlabel();
        //   s.emitlabel(begin);
        //  s.gen(begin, after);
        //  s.emitlabel(after);
        s.gen();
    }


    //block -> {decls stmts}
    private Stmt block(Env e) throws IOException {
        match('{');
        Env.push(Env.top);
        //若是函数块的开头，则将形参信息传过来
        if (e != null) Env.top = e;
        else Env.top = new Env(Env.top);
        decls();
        Stmt s = stmts();
        match('}');
        Env.top = Env.pop();
        return s;
    }

    //decls -> decl decls | e
    private void decls() throws IOException {
        while (look.tag == Tag.BASIC) {
            //decl -> type ID IDs;
            Type basic_t = (Type) look;
            match(Tag.BASIC);
            //IDs -> ,ID IDs | e
            do {
                if (look.tag == ',') match(',');
                Word id_word = (Word) look;
                // ID -> id | id dims
                match(Tag.ID);
                Type p = basic_t;
                if (look.tag == '[') {
                    p = dims(basic_t);
                }
                //加入符号表
                if (Env.top.getId(id_word) != null) {
                    error(id_word.toString() + " has been declared");
                }
                Id id = new Id(id_word, p, Env.top.used);
                Env.top.putId(id_word, id);
                Env.top.used = Env.top.used + p.width;
            } while (look.tag == ',');
            match(';');
        }
    }

    //dims -> [NUM] dims | e
    private Type dims(Type p) throws IOException {
        match('[');
        Token tok = look;
        match(Tag.NUM);
        match(']');
        if (look.tag == '[')
            p = dims(p);
        return new Array(((Num) tok).value, p);
    }

    //stmts -> stmt stmts | e
    private Stmt stmts() throws IOException {
        if (look.tag == '}') {
            return Stmt.Null;
        } else {
            return new Seq(stmt(), stmts());
        }
    }

    private Stmt stmt() throws IOException {
        switch (look.tag) {
            case ';':
                move();
                return Stmt.Null;
            case Tag.IF:
                return If();
            case Tag.WHILE:
                return While();
            case Tag.DO:
                return Do_while();
            case Tag.BREAK:
                return Break();
            case Tag.RETURN:
                return Return();
            case '{':
                return block(null);
            default:
                Token id_word = look;
                match(Tag.ID);
                //stmt -> id(exprList)
                if (look.tag == '(') {
                    return (Stmt) Call((Word) id_word, 0);
                }
                // stmt -> loc = bool
                else return assign((Word) id_word);
        }
    }

    private Stmt Return() throws IOException {
        match(Tag.RETURN);
        Expr x = bool();
        match(';');
        return new Return(x, savedProcType);
    }

    //stmt -> id(exprList);
    private Node Call(Word id_word, int kind) throws IOException {
        Function func = Env.getFunc(id_word);
        if (func == null) {
            error(id_word.toString() + "() undecleard");
        }
        match('(');
        Vector<Expr> parameters = new Vector<>();
        do {
            if (look.tag == ')') break;
            if (look.tag == ',') match(',');
            parameters.add(bool());
        } while (look.tag == ',');
        match(')');
        if (kind == 0) {
            match(';');
            return new Call(func, parameters);
        } else {
            return new Call_ret(func, parameters);
        }
    }

    private Stmt If() throws IOException {
        //stmt -> if (bool) stmt
        match(Tag.IF);
        match('(');
        Expr x = bool();
        match(')');
        Stmt s1 = stmt();
        if (look.tag != Tag.ELSE) {
            return new If(x, s1);
        }
        //stmt -> if (bool) stmt else stmt
        match(Tag.ELSE);
        Stmt s2 = stmt();
        return new If_else(x, s1, s2);
    }

    //stmt -> while (bool) stmt
    private Stmt While() throws IOException {
        While whilenode = new While();
        Stmt savedStmt = Stmt.Enclosing;
        //提前构造while结点，以用于stmt内部的break找到外层循环
        Stmt.Enclosing = whilenode;
        match(Tag.WHILE);
        match('(');
        Expr x = bool();
        match(')');
        Stmt s1 = stmt();
        whilenode.init(x, s1);
        Stmt.Enclosing = savedStmt;
        return whilenode;
    }

    //stmt -> do stmt while(bool);
    private Stmt Do_while() throws IOException {
        Do_while donode = new Do_while();
        Stmt savedStmt = Stmt.Enclosing;
        //提前构造while结点，以用于stmt内部的break找到外层循环
        Stmt.Enclosing = donode;
        match(Tag.DO);
        Stmt s1 = stmt();
        match(Tag.WHILE);
        match('(');
        Expr x = bool();
        match(')');
        match(';');
        donode.init(s1, x);
        Stmt.Enclosing = savedStmt;
        return donode;
    }

    // stmt -> break;
    private Stmt Break() throws IOException {
        match(Tag.BREAK);
        match(';');
        return new Break();
    }


    //stmt ->  loc = bool;
    private Stmt assign(Word id_word) throws IOException {
        Stmt stmt;
        Id id = Env.top.getId(id_word);
        if (id == null) error(id_word.toString() + " undeclared");

        //loc -> id
        if (look.tag == '=') {
            move();
            stmt = new Assign(id, bool());
        } else {
            //loc -> id offset
            Access x = offset(id);
            match('=');
            stmt = new SetElem(x, bool());
        }
        match(';');
        return stmt;
    }

    /*
       bool -> join || bool
             | join
     */
    private Expr bool() throws IOException {
        Expr x = join();
        while (look.tag == Tag.OR) {
            Token tok = look;
            move();
            x = new Or(tok, x, join());
        }
        return x;
    }

    /*
        join -> equality && join
              | equality
     */
    private Expr join() throws IOException {
        Expr x = equality();
        while (look.tag == Tag.AND) {
            Token tok = look;
            move();
            x = new And(tok, x, equality());
        }
        return x;
    }

    /*
        equality ->  rel == equality
                   | rel != equality
                   | rel
     */
    private Expr equality() throws IOException {
        Expr x = rel();
        while (look.tag == Tag.EQ || look.tag == Tag.NE) {
            Token tok = look;
            move();
            x = new Rel(tok, x, rel());
        }
        return x;
    }

    /*
        rel -> expr < expr
             | expr <= expr
             | expr >= expr
             | expr > expr
             | expr
     */
    private Expr rel() throws IOException {
        Expr x = expr();
        switch (look.tag) {
            case '<':
            case Tag.LE:
            case Tag.GE:
            case '>':
                Token tok = look;
                move();
                return new Rel(tok, x, expr());
            default:
                return x;
        }
    }

    /*
        expr -> term + expr
              | term - expr
              | term
     */
    private Expr expr() throws IOException {
        Expr x = term();
        while (look.tag == '+' || look.tag == '-') {
            Token tok = look;
            move();
            x = new Arith(tok, x, term());
        }
        return x;
    }

    /*
        term -> unary * term
              | unary / term
              | unary
     */
    private Expr term() throws IOException {
        Expr x = unary();
        while (look.tag == '*' || look.tag == '/') {
            Token tok = look;
            move();
            x = new Arith(tok, x, term());
        }
        return x;
    }

    /*
        unary -> !unary
               | -unary
               | factor
     */
    private Expr unary() throws IOException {
        if (look.tag == '-') {
            move();
            return new Unary(Word.minus, unary());
        } else if (look.tag == '!') {
            Token tok = look;
            move();
            return new Not(tok, unary());
        } else return factor();
    }

    /*
     factor -> (bool)
             | loc
             | NUM
             | REAL
             | TRUE
             | FALSE
     */
    private Expr factor() throws IOException {
        Expr x = null;
        switch (look.tag) {
            case '(':
                move();
                x = bool();
                match(')');
                return x;
            case Tag.NUM:
                x = new Constant(look, Type.Int);
                move();
                return x;
            case Tag.REAL:
                x = new Constant(look, Type.Float);
                move();
                return x;
            case Tag.TRUE:
                x = Constant.True;
                move();
                return x;
            case Tag.FALSE:
                x = Constant.False;
                move();
                return x;
            case Tag.ID:
                Word id_word = (Word) look;
                move();
                if (look.tag == '[') {
                    Id id = Env.top.getId((Word) id_word);
                    if (id == null) {
                        error(id_word.toString() + " undeclared");
                    }
                    return offset(id);
                } else if (look.tag == '(') {
                    return (Expr) Call(id_word, 1);
                } else {
                    Id id = Env.top.getId((Word) id_word);
                    if (id == null) {
                        error(id_word.toString() + " undeclared");
                    }
                    return id;
                }
            default:
                error("syntax error");
                return x;
        }
    }


    private Access offset(Id a) throws IOException {
        Expr i, w, t1, t2, loc;
        Type type = a.type;
        match('[');
        i = bool();
        match(']');
        type = ((Array) type).of;
        w = new Constant(type.width);
        t1 = new Arith(new Token('*'), i, w);
        loc = t1;
        while (look.tag == '[') {
            match('[');
            i = bool();
            match(']');
            type = ((Array) type).of;
            w = new Constant(type.width);
            t1 = new Arith(new Token('*'), i, w);
            t2 = new Arith(new Token('+'), loc, t1);
            loc = t2;
        }
        return new Access(a, loc, type);
    }

}
