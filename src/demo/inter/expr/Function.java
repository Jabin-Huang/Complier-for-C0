package demo.inter.expr;

import demo.lexer.Word;
import demo.symbols.Type;

import java.util.Vector;

public class Function extends Expr{
    public Vector<Type> param;
    public Function(Word w, Type t, Vector<Type> pt){
        super(w, t);
        param = pt;
    }

    public String toString(){
        return op.toString() ;
    }


}
