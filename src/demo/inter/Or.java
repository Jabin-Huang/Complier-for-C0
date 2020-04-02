package demo.inter;

import demo.lexer.Token;

public class Or extends Logical{
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
}
