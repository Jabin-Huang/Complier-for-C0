package demo.inter;

import demo.lexer.Token;

public class Rel extends Logical {
    public Rel(Token tok, Expr x1, Expr x2){
        super(tok, x1, x2);
    }
    public void jumping(int t, int f){
        Expr a = expr1.reduce();
        Expr b = expr2.reduce();
        String test = a.toString() + " " + op.toString() + " " + b.toString();
        emitjumps(test, t, f);
    }
}
