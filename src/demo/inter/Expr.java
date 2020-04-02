package demo.inter;

import demo.lexer.Token;
import demo.symbols.Type;

public class Expr extends Node{
    public Token op;
    public Type type;
   Expr(Token tok, Type p){
        op = tok;
        type = p;
    }
    //return a term, as the right body of three-address instruction.
    //usually it's overridden in subclass
    public Expr gen(){
        return this;
    }

    //return the temp variable which store the reduced result of expression.
    public Expr reduce(){
        return this;
    }

    //for bool expr and jumping expr
    public void jumping(int t, int f){
        emitjumps(toString(), t, f);
    }
    public void emitjumps(String test, int t, int f){
        if(t != 0 && f != 0){
            emit("if" + test + "goto L" +t);
            emit("goto L" + f);
        }
        else if(t != 0) emit("if " + test + " goto L"  + t);
        else if(f != 0) emit("ifFalse " + test + " goto L" + f);
        else ;
    }
    public String toString(){
        return op.toString();
    }
}
