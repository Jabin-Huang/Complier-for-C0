package AST.stmt;

import AST.Quadruple;
import AST.expr.Expr;
import AST.expr.Temp;
import lexer.Token;
import lexer.Word;
import symbols.Type;

public class Return extends Stmt {
    Expr res;
    Type type;

    public Return(Expr x, Type t) {
        res = x;
        type = t;
        if (check(x.type, type) == null) {
            error("return type error");
        }
    }

    private Type check(Type p1, Type p2) {
        if (Type.numeric(p1) && Type.numeric(p2)) return p2;
        else if (p1 == Type.Bool && p2 == Type.Bool) return p2;
        else return null;
    }


    public void gen() {
        Expr x = res.reduce();
        Temp ret_val = new Temp(type);
        Quadruple.ins.add(new Quadruple(new Token('='), x, null, ret_val));
        Quadruple.ins.add(new Quadruple(Word.ret, null, null, null));
    }

    public String AST_str(int col) {
        return "\t".repeat(Math.max(0, col)) + "Return(" + "\n" +
                res.AST_str(col + 1) +
                "," + "\n" +
                "\t".repeat(Math.max(0, col + 1)) + type.toString() + "\n" +
                "\t".repeat(Math.max(0, col)) + ")";

    }


}
