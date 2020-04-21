package demo.inter.stmt;

import demo.inter.expr.Expr;
import demo.symbols.Type;

public class If extends Stmt {
    Expr expr;
    Stmt stmt;
    public If(Expr x, Stmt s){
        expr = x;
        stmt = s;
        if(expr.type != Type.Bool){
            error("boolean required in if");
        }
    }

    /*
               ifFalse expr goto a
        label: stmt
            a:
     */
    public void gen(int b, int a){
        int label = newlabel(); //stmt的标号
        expr.jumping(0, a); //为真时继续执行，为假时转向a
        emitlabel(label);
        stmt.gen(label, a);
    }

    public String AST_str(int col){
        String AST_child = expr.AST_str(col + 1) +
                            ",\n"+
                            stmt.AST_str(col + 1);
        return "\t".repeat(Math.max(0, col)) +
                "If(" +
                '\n' +
                AST_child +
                '\n' +
                "\t".repeat(Math.max(0, col)) +
                ')';
    }



}
