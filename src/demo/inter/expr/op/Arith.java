package demo.inter.expr.op;

import demo.inter.expr.Expr;
import demo.inter.expr.Temp;
import demo.lexer.Token;
import demo.symbols.Type;

public class Arith extends Op {
    public Expr expr1, expr2;
    public Arith(Token tok, Expr x1, Expr x2) {
        super(tok, null);
        expr1 = x1;
        expr2 = x2;
        type = Type.max(expr1.type, expr2.type);
        if(type == null) error("type error");
    }

//    public Expr gen(){
//        return new Arith(op, expr1.reduce(), expr2.reduce());
//    }

    public String toString(){
        return expr1.reduce().toString() + " " + op.toString()  + " " + expr2.reduce().toString();
    }
    public String AST_str(int col){
        String AST_child = "\t".repeat(Math.max(0,col + 1)) +
                op.toString() +
                ",\n"+
                expr1.AST_str(col + 1) +
                ",\n"+
                expr2.AST_str(col + 1);

        return "\t".repeat(Math.max(0, col)) +
                "Arith(" +
                '\n' +
                AST_child +
                '\n' +
                "\t".repeat(Math.max(0, col)) +
                ')';
    }

}
