package demo.inter.expr.logical;

import demo.inter.expr.Expr;
import demo.lexer.Token;

public class Or extends Logical {
    public Or(Token tok, Expr x1, Expr x2){
        super(tok, x1, x2);
    }

    /*
        // t != 0

               if expr1 goto t
               if expr2 goto t
               goto f

        // t == 0

              if expr1 goto label
              ifFalse expr2 goto f
        label:

     */
    public void jumping(int t, int f){
        int label = t != 0 ? t : newlabel();
        expr1.jumping(label, 0);
        expr2.jumping(t, f);
        if(t == 0) emitlabel(label);
    }

    public String AST_str(int col){
        String AST_child = "\t".repeat(Math.max(0,col + 1)) +
                op.toString() +
                ",\n"+
                expr1.AST_str(col + 1) +
                ",\n"+
                expr2.AST_str(col + 1);

        return "\t".repeat(Math.max(0, col)) +
                "Or(" +
                '\n' +
                AST_child +
                '\n' +
                "\t".repeat(Math.max(0, col)) +
                ')';
    }
}