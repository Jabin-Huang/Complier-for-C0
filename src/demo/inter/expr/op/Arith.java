package demo.inter.expr.op;

import demo.inter.Quadruple;
import demo.inter.expr.Expr;
import demo.inter.expr.Temp;
import demo.lexer.Token;
import demo.symbols.Type;

public class Arith extends Expr {
    public Expr expr1, expr2;

    public Arith(Token tok, Expr x1, Expr x2) {
        super(tok, null);
        expr1 = x1;
        expr2 = x2;
        type = Type.max(expr1.type, expr2.type);
        if (type == null) error("type error");
    }


    public Expr reduce() {
        Temp t = new Temp(type);
        Expr a1 = expr1.reduce();
        Expr a2 = expr2.reduce();
        // emit(t.toString() + " = " + a1.toString() + " " + op.toString() + " " + a2.toString());
        Quadruple.ins.add(new Quadruple(this.op, a1, a2, t));
        return t;
    }


    public String toString() {
        return expr1.toString() + " " + op.toString() + " " + expr2.toString();
    }

    public String AST_str(int col) {
        String AST_child = "\t".repeat(Math.max(0, col + 1)) +
                op.toString() +
                ",\n" +
                expr1.AST_str(col + 1) +
                ",\n" +
                expr2.AST_str(col + 1);

        return "\t".repeat(Math.max(0, col)) +
                "Arith(" +
                '\n' +
                AST_child +
                '\n' +
                "\t".repeat(Math.max(0, col)) +
                ')';
    }

}
