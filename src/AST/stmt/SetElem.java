package AST.stmt;

import AST.Quadruple;
import AST.expr.Expr;
import AST.expr.op.Access;
import lexer.Word;
import symbols.Array;
import symbols.Type;

public class SetElem extends Stmt {
    public Access access;
    public Expr expr;

    public SetElem(Access x, Expr y) {
        access = x;
        expr = y;
        if (check(x.type, expr.type) == null) {
            error("type error");
        }
    }


    public Type check(Type p1, Type p2) {
        if (p1 instanceof Array || p2 instanceof Array) {
            return null;
        } else if (p1 == p2) {
            return p2;
        } else if (Type.numeric(p1) && Type.numeric(p2)) {
            return p2;
        } else return null;
    }


    public void gen() {
        Expr x = expr.reduce();
        Expr index = access.index.reduce();
        Expr tar = new Access(access.array, index, access.type);
        Quadruple.ins.add(new Quadruple(Word.setElem, x, null, tar));
    }


    public String AST_str(int col) {
        String AST_child = access.AST_str(col + 1) +
                ",\n" +
                expr.AST_str(col + 1);
        return "\t".repeat(Math.max(0, col)) +
                "SetElem(" +
                '\n' +
                AST_child +
                '\n' +
                "\t".repeat(Math.max(0, col)) +
                ')';
    }

}
