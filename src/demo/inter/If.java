package demo.inter;

import demo.symbols.Type;

public class If extends Stmt{
    Expr expr;
    Stmt stmt;
    public If(Expr x, Stmt s){
        expr = x;
        stmt = s;
        if(expr.type != Type.Bool){
            expr.error("boolean required in if");
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
}
