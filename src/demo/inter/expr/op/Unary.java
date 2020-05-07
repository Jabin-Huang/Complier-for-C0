package demo.inter.expr.op;

import demo.inter.Quadruple;
import demo.inter.expr.Expr;
import demo.inter.expr.Temp;
import demo.lexer.Token;
import demo.symbols.Type;

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
        // emit(t.toString() + " = " + a1.toString());
        Quadruple.ins.add(new Quadruple(this.op, a1, null, t));
        return t;
    }

    public String toString() {
        return op.toString() + " " + expr.toString();
    }

    public String AST_str(int col) {
        String AST_child = "\t".repeat(Math.max(0, col + 1)) +
                op.toString() +
                ",\n" +
                expr.AST_str(col + 1);

        return "\t".repeat(Math.max(0, col)) +
                "Unary(" +
                '\n' +
                AST_child +
                '\n' +
                "\t".repeat(Math.max(0, col)) +
                ')';
    }
}
