package demo.inter.expr.logical;

import demo.inter.expr.Expr;
import demo.lexer.Token;

public class Not extends Logical {
    public Not(Token tok, Expr x2) {
        super(tok, x2, x2);
    }

    public void jumping(int t, int f) {
        expr2.jumping(f, t);
    }

    public void gen() {
        Logical B1 = (Logical) expr1;
        B1.gen();
        truelist = B1.falselist;
        falselist = B1.truelist;
    }

    public String toString() {
        return op.toString() + " " + expr2.toString();
    }

    public String AST_str(int col) {
        String AST_child = "\t".repeat(Math.max(0, col + 1)) +
                op.toString() +
                ",\n" +
                expr1.AST_str(col + 1);

        return "\t".repeat(Math.max(0, col)) +
                "Not(" +
                '\n' +
                AST_child +
                '\n' +
                "\t".repeat(Math.max(0, col)) +
                ')';
    }
}
