package demo.inter.stmt;

import demo.inter.expr.Expr;
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
            error("boolean required in do-while");
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

    public String AST_str(int col){
        int newCol = col + 1;
        String AST_child = expr.AST_str(col + 1) +
                ",\n"+
                stmt.AST_str(col + 1);
        return "\t".repeat(Math.max(0, col)) +
                "Do_while(" +
                '\n' +
                AST_child +
                '\n' +
                "\t".repeat(Math.max(0, col)) +
                ')';
    }
}
