package demo.inter;

import demo.symbols.Type;

public class If_else extends Stmt {
    Expr expr;
    Stmt stmt1, stmt2;
    public If_else(Expr x, Stmt s1, Stmt s2){
        expr = x;
        stmt1 = s1;
        stmt2 = s2;
        if(expr.type != Type.Bool) {
            expr.error("boolean required in if");
        }
    }
    /*
                ifFalse expr goto label2
        label1: stmt1
                goto a
        label2: stmt2
             a:


     */

    public void gen(int b, int a){
        int label1 = newlabel();
        int label2 = newlabel();
        expr.jumping(0, label2);
        emitlabel(label1);
        stmt1.gen(label1, a);
        emit("goto L" + a );
        emitlabel(label2);
        stmt2.gen(label2, a);
    }
}
