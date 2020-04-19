package demo.inter.expr.logical;

import demo.inter.expr.Expr;
import demo.lexer.Token;
import demo.symbols.Array;
import demo.symbols.Type;

public class Rel extends Logical {
    public Rel(Token tok, Expr x1, Expr x2){
        super(tok, x1, x2);
    }

    public Type check(Type p1, Type p2){
        if(p1 instanceof Array || p2 instanceof Array){
            return null;
        }
        else if(p1 == p2){
            return Type.Bool;
        }
        else return null;
    }

    public String AST_str(int col){
        String AST_child = "\t".repeat(Math.max(0,col + 1)) +
                op.toString() +
                ",\n"+
                expr1.AST_str(col + 1) +
                ",\n"+
                expr2.AST_str(col + 1);

        return "\t".repeat(Math.max(0, col)) +
                "Rel(" +
                '\n' +
                AST_child +
                '\n' +
                "\t".repeat(Math.max(0, col)) +
                ')';
    }
}
