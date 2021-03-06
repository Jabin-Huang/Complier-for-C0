package AST.stmt;

import AST.Quadruple;
import AST.expr.Constant;
import AST.expr.Expr;
import AST.expr.Function;
import lexer.Word;
import symbols.Type;

import java.util.Vector;

public class Call extends Stmt {
    public Function func;
    public Vector<Expr> parameters;

    public Call(Function f, Vector<Expr> p) {
        func = f;
        parameters = p;
        check(func.param, parameters);
    }

    private void check(Vector<Type> t, Vector<Expr> e) {
        if (t.size() > e.size()) {
            error("lack enough parameters for calling function");
        } else if (t.size() < e.size()) {
            error("too much parameters for calling function");
        } else {
            for (int i = 0; i < t.size(); ++i) {
                if (t.get(i) != e.get(i).type) {
                    error("type of parameter for calling function is not matched");
                }
            }
        }
    }


    public void gen() {
        for (Expr e : parameters) {
            Expr t = e.reduce();
            Quadruple.ins.add(new Quadruple(Word.parm, t, null, null));
        }
        Quadruple.ins.add(new Quadruple(Word.call, func, new Constant(parameters.size()), null));
    }


    public String AST_str(int col) {
        return "\t".repeat(Math.max(0, col)) +
                "call(" + func.toString() + ", " + parameters.toString() + ')';
    }

}
