package AST.expr;

import AST.Quadruple;
import lexer.Word;
import symbols.Type;

import java.util.Vector;

public class Call_ret extends Expr {

    public Function func;
    public Vector<Expr> parameters;
    public Temp ret;

    public Call_ret(Function f, Vector<Expr> p) {
        super(f.op, f.type);
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
        if (type == Type.Void) {
            error("return value can't be of void type");
        }
    }

    public Expr reduce() {
        for (Expr e : parameters) {
            Expr t = e.reduce();
            Quadruple.ins.add(new Quadruple(Word.parm, t, null, null));
        }
        ret = new Temp(func.type);
        Quadruple.ins.add(new Quadruple(Word.call, func, new Constant(parameters.size()), ret));
        return ret;
    }


    public String AST_str(int col) {
        return "\t".repeat(Math.max(0, col)) +
                "Call_ret(" + func.toString() + ", " + parameters.toString() + ')';
    }


}
