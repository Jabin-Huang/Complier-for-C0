package AST.expr.logical;

import AST.Quadruple;
import AST.expr.Expr;
import lexer.Token;

public class And extends Logical {
    public And(Token tok, Expr x1, Expr x2) {
        super(tok, x1, x2);
    }


    // B - > B1 && M B2
    public void gen() {
        Logical B1 = (Logical) expr1, B2 = (Logical) expr2;
        B1.gen();
        int M_instr = Quadruple.nextinstr();
        B2.gen();

        backpatch(B1.truelist, M_instr);
        truelist = B2.truelist;
        falselist = merge(B1.falselist, B2.falselist);
    }

    public String AST_str(int col) {
        String AST_child = "\t".repeat(Math.max(0, col + 1)) +
                op.toString() +
                ",\n" +
                expr1.AST_str(col + 1) +
                ",\n" +
                expr2.AST_str(col + 1);

        return "\t".repeat(Math.max(0, col)) +
                "And(" +
                '\n' +
                AST_child +
                '\n' +
                "\t".repeat(Math.max(0, col)) +
                ')';
    }

}
