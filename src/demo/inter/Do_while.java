package demo.inter;

import demo.symbols.Type;

public class Do_while extends Stmt {
    Expr expr;
    Stmt stmt;
    public Do_while(){
        expr = null;
        stmt = null;
    }
    public void init(Stmt s, Expr x){
        expr = x;
        stmt = s;
        if(expr.type != Type.Bool){
            expr.error("boolean required in do-while");
        }
    }
    /*
            b: stmt
        label: if expr goto b
     */
    public void gen(int b, int a){
        after = a;
        int label = newlabel();
        stmt.gen(b, label);
        emitlabel(label);
        expr.jumping(b, 0);
    }
}
