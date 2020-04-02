package demo.inter;

import demo.lexer.Token;

public class And extends Logical {
    public And(Token tok, Expr x1, Expr x2){
        super(tok, x1, x2);
    }

    /* //f == 0
              ifFalse expr1 goto label
              if expr2 goto t
        label:

        //f != 0
              ifFalse expr goto f
              if expr2 goto t
              goto f
     */
    public void jumping(int t, int f){
        int label = f != 0 ? f : newlabel();
        expr1.jumping(0, label);
        expr2.jumping(t, f);
        if( f == 0 ) emitlabel(label);
    }

}
