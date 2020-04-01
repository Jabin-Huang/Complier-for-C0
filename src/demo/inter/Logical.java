package demo.inter;

import demo.lexer.Token;
import demo.symbols.Type;

public class Logical extends Expr {
    public Expr expr1, expr2;
    Logical(Token tok, Expr x1, Expr x2){
        super(tok, null);
        expr1 = x1;
        expr2 = x2;
        type = Type.bool_check(expr1.type, expr2.type);
        if(type == null) error("type error");
    }

    //return bool value
    public Expr gen(){
        int f = newlabel();
        int a = newlabel();
        Temp temp =new Temp(type);
        this.jumping(0, f);
        emit(temp.toString() + " = true");
        emit("goto L" + a);
        emitlabel(f);
        emit(temp.toString() + " = false");
        emitlabel(a);
        return temp;
    }

    public String toString(){
        return expr1.toString() + " " + op.toString() + " " +expr2.toString();
    }
}
