package AST.stmt;

import AST.Quadruple;
import AST.expr.Expr;
import AST.expr.Id;
import lexer.Token;
import symbols.Type;

public class Assign extends Stmt {
    private Id id;
    private Expr expr;

    public Assign(Id i, Expr x) {
        id = i;
        expr = x;
        if (check(id.type, expr.type) == null) {
            error("type error");
        }
    }

    private Type check(Type p1, Type p2) {
        if (Type.numeric(p1) && Type.numeric(p2)) return p2;
        else if (p1 == Type.Bool && p2 == Type.Bool) return p2;
        else return null;
    }


    public void gen() {
        Expr x = expr.reduce();
        Quadruple.ins.add(new Quadruple(new Token('='), x, null, id));
    }

    public String AST_str(int col) {
        String AST_child = id.AST_str(col + 1) +
                ",\n" +
                expr.AST_str(col + 1);
        return "\t".repeat(Math.max(0, col)) +
                "Assign(" +
                '\n' +
                AST_child +
                '\n' +
                "\t".repeat(Math.max(0, col)) +
                ')';
    }

}
