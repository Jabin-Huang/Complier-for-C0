package demo.inter.stmt;

import demo.inter.expr.Expr;
import demo.symbols.Type;

import java.nio.Buffer;

public class While extends Stmt {
    Expr expr;
    Stmt stmt;
    public While(){
        expr = null;
        stmt = null;
    }
    public void init(Expr x, Stmt s){
        expr = x;
        stmt = s;
        if(expr.type != Type.Bool){
            error("boolean required in while");
        }
    }


    /*
            b: ifFalse expr goto a
        label: stmt
               goto b
            a:

     */
    public void gen(int b, int a){
        after =a;
        expr.jumping(0, a);
        int label = newlabel();
        emitlabel(label);
        stmt.gen(label, b);
        emit("goto L" + b);
    }

    public String AST_str(int col){
        String AST_child = expr.AST_str(col + 1) +
                ",\n"+
                stmt.AST_str(col + 1);

        return "\t".repeat(Math.max(0, col)) +
                "While(" +
                '\n' +
                AST_child +
                '\n' +
                "\t".repeat(Math.max(0, col)) +
                ')';
    }
}
