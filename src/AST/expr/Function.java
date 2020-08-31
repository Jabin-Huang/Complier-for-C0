package AST.expr;

import lexer.Word;
import symbols.Type;

import java.util.Vector;

public class Function extends Expr {
    public Vector<Type> param;

    public Function(Word w, Type t, Vector<Type> pt) {
        super(w, t);
        param = pt;
    }

    public String toString() {
        return op.toString();
    }


}
