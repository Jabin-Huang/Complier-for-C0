package AST.expr.op;

import AST.Quadruple;
import AST.expr.Expr;
import AST.expr.Temp;
import lexer.Token;
import symbols.Type;

public class Unary extends Expr {
    public Expr expr;

    public Unary(Token tok, Expr x) {
        super(tok, null);
        expr = x;
        type = Type.max(Type.Int, expr.type);
        if (type == null) error("type error");
    }


    public Expr reduce() {
        Temp t = new Temp(type);
        Expr a1 = expr.reduce();
        Quadruple.ins.add(new Quadruple(this.op, a1, null, t));
        return t;
    }

    public String toString() {
        return op.toString() + " " + expr.toString();
    }

    public String AST_str(int col) {
        String AST_child = "\t".repeat(col + 1) +
                op.toString() +
                ",\n" +
                expr.AST_str(col + 1);

        return "\t".repeat(col) +
                "Unary(" +
                '\n' +
                AST_child +
                '\n' +
                "\t".repeat(col) +
                ')';
    }
}
