package demo.parser;

import com.google.gson.Gson;
import demo.inter.expr.Constant;
import demo.inter.expr.Expr;
import demo.inter.expr.Id;
import demo.inter.expr.logical.And;
import demo.inter.expr.logical.Not;
import demo.inter.expr.logical.Or;
import demo.inter.expr.logical.Rel;
import demo.inter.expr.op.Access;
import demo.inter.expr.op.Arith;
import demo.inter.expr.op.Unary;
import demo.inter.stmt.*;
import demo.lexer.*;
import demo.symbols.*;

import java.io.IOException;


public class Parser {
    private Lexer lex;
    private Token look; //向前看词法单元
    private int used = 0; //用于变量声明的存储位置
    public Parser(Lexer l) throws IOException {
        lex = l;
        move();
    }
    private void move() throws IOException {
        look = lex.scan();
    }
    private void error(String s){
        throw new Error("near line " + Lexer.line + ": " + s);
    }
    private void match(int t) throws IOException {
        if(look.tag == t) move();
        else {
            String expect = (t > 255) ? Tag.tagToString.get(t) : ""+(char) t;
            error("syntax error: expected "+ expect +"but found " + look.toString() );
        }
    }


    //program -> process program | e
    public void progarm() throws IOException {
        Env.top = new Env(Env.top);
       // while (look.tag == Tag.BASIC){
            process();
       // }
    }

    //process -> type id (parameter) block
    private void process() throws IOException{
//        Type fun_t = (Type) look;
//        match(Tag.BASIC);
//        Word fun_w = (Word) look;
//        match(Tag.ID);
//        /*
//            TODO
//         */
//        match('(');
//        // parameter -> type id ids | e
//        // ids -> , type id ids | e
//        do{
//            if(look.tag == ',') match(',');
//            Type pt_word = (Type) look;
//            match(Tag.BASIC);
//            Word id_word = (Word) look;
//            match(Tag.ID);
//            /*
//                TODO
//             */
//        }while(look.tag == ',');
//        match(')');

        Stmt s = block();
        System.out.print(s.AST_str(0));
        int begin = s.newlabel();
        int after = s.newlabel();
        s.emitlabel(begin);
        s.gen(begin, after);
        s.emitlabel(after);
    }



    //block -> {decls stmts}
    private Stmt block() throws IOException{
        match('{');
        Env.push(Env.top);
        Env.top = new Env(Env.top);
        decls();
        Stmt s = stmts();
        match('}');
        Env.top = Env.pop();
        return s;
    }

    //decls -> decl decls | e
    private void decls() throws IOException{
        while(look.tag == Tag.BASIC){
            //decl -> type ID IDs;
            Type basic_t = (Type)look;
            match(Tag.BASIC);
            //IDs -> ,ID IDs | e
            do{
                if(look.tag == ',') match(',');
                Word id_word = (Word) look;
                // ID -> id | id dims
                match(Tag.ID);
                Type p = basic_t;
                if(look.tag == '[') {
                    p = dims(basic_t);
                }
                //加入符号表
                Id id = new Id(id_word, p, used);
                Env.top.put(id_word, id);
                used = used + p.width;
            } while(look.tag == ',');
            match(';');
        }
    }

    //dims -> [NUM] dims | e
    private Type dims(Type p) throws IOException{
        match('[');
        Token tok = look;
        match(Tag.NUM);
        match(']');
        if(look.tag == '[')
            p = dims(p);
        return new Array(( (Num)tok ).value, p);
    }

    //stmts -> stmt stmts | e
    private Stmt stmts() throws IOException{
        if(look.tag == '}'){
            return Stmt.Null;
        }
        else{
            return new Seq(stmt(), stmts());
        }
    }

    private Stmt stmt() throws IOException{
        Expr x;
        Stmt s, s1, s2;
        Stmt savedStmt; //为break保存外层循环
        switch (look.tag){
            case ';':
                move();
                return Stmt.Null;
            case Tag.IF:
                //stmt -> if (bool) stmt
                match(Tag.IF);
                match('(');
                x = bool();
                match(')');
                s1 = stmt();
                if(look.tag != Tag.ELSE){
                    return new If(x, s1);
                }
                //stmt -> if (bool) stmt else stmt
                match(Tag.ELSE);
                s2 = stmt();
                return new If_else(x, s1, s2);
            case Tag.WHILE:
                //stmt -> while (bool) stmt
                While whilenode = new While();
                savedStmt = Stmt.Enclosing;
                Stmt.Enclosing = whilenode;
                match(Tag.WHILE);
                match('(');
                x = bool();
                match(')');
                s1 = stmt();
                whilenode.init(x, s1);
                Stmt.Enclosing = savedStmt;
                return whilenode;
            case Tag.DO:
                //stmt -> do stmt while(bool);
                Do_while donode = new Do_while();
                savedStmt = Stmt.Enclosing;
                Stmt.Enclosing = donode;
                match(Tag.DO);
                s1 = stmt();
                match(Tag.WHILE);
                match('(');
                x = bool();
                match(')');
                match(';');
                donode.init(s1, x);
                Stmt.Enclosing = savedStmt;
                return donode;
            case Tag.BREAK:
                // stmt -> break;
                match(Tag.BREAK);
                match(';');
                return new Break();
            case '{':
                // stmt -> block
                return block();
            default:
                // stmt -> loc = bool
                return assign();
        }

    }

    //stmt ->  loc = bool;
    private Stmt assign() throws IOException{
        Stmt stmt;
        Word id_word = (Word) look;
        match(Tag.ID);
        Id id = Env.top.get(id_word);
        if(id == null) error(id_word.toString() + " undeclared");

        //loc -> id
        if(look.tag == '='){
            move();
            stmt = new Assign(id, bool());
        }else{
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
    private Expr bool() throws IOException{
        Expr x = join();
        while(look.tag == Tag.OR){
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
    private Expr join() throws IOException{
        Expr x = equality();
        while(look.tag == Tag.AND){
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
    private Expr equality() throws IOException{
        Expr x = rel();
        while(look.tag == Tag.EQ || look.tag == Tag.NE){
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
    private Expr rel() throws IOException{
        Expr x = expr();
        switch (look.tag){
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
    private Expr expr() throws IOException{
        Expr x = term();
        while(look.tag == '+' || look.tag == '-'){
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
    private Expr term() throws IOException{
        Expr x = unary();
        while(look.tag == '*' || look.tag == '/'){
            Token tok = look;
            move();
            x = new Arith(tok, x, unary());
        }
        return x;
    }

    /*
        unary -> !unary
               | -unary
               | factor
     */
    private Expr unary() throws IOException{
        if(look.tag == '-'){
            move();
            return new Unary(Word.minus, unary());
        }
        else if(look.tag == '!'){
            Token tok = look;
            move();
            return new Not(tok, unary());
        }
        else return factor();
    }

    /*
     factor -> (bool)
             | loc
             | NUM
             | REAL
             | TRUE
             | FALSE
     */
    private Expr factor() throws IOException{
        Expr x = null;
        switch (look.tag){
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
                Id id = Env.top.get(look);
                if(id == null){
                    error(look.toString() + " undeclared");
                }
                move();
                if(look.tag != '[') return id;
                else return offset(id);
            default:
                error("syntax error");
                return x;
        }
    }

    private Access offset(Id a) throws IOException{
        Expr i, w, t1, t2, loc;
        Type type = a.type;
        match('[');
        i = bool();
        match(']');
        type = ((Array)type).of;
        w = new Constant(type.width);
        t1 = new Arith(new Token('*'), i, w);
        loc = t1;
        while(look.tag =='['){
            match('[');
            i = bool();
            match(']');
            type = ((Array)type).of;
            w = new Constant(type.width);
            t1 = new Arith(new Token('*'), i, w);
            t2 = new Arith(new Token('+'), loc, t1);
            loc = t2;
        }
        return new Access(a, loc, type);
    }

}
