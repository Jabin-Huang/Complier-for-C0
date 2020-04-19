package demo.inter.expr.op;

import demo.inter.expr.Expr;
import demo.lexer.Token;
import demo.symbols.Type;

public class Unary extends Op {
    public Expr expr;
    public Unary(Token tok, Expr x) {
        super(tok, null);
        expr = x;
        type = Type.max(Type.Int, expr.type);
        if(type == null) error("type error");
    }
    public Expr gen(){
        return new Unary(op, expr.reduce());
    }
    public String toString(){
        return op.toString() + " " + expr.toString();
    }
    public String AST_str(int col){
        String AST_child = "\t".repeat(Math.max(0, col + 1)) +
                op.toString() +
                ",\n"+
                expr.AST_str(col + 1);

        return "\t".repeat(Math.max(0, col)) +
                "Unary(" +
                '\n' +
                AST_child +
                '\n' +
                "\t".repeat(Math.max(0, col)) +
                ')';
    }
}
