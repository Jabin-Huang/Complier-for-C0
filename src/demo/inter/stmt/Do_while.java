package demo.inter.stmt;

import demo.inter.Quadruple;
import demo.inter.expr.Expr;
import demo.symbols.Type;

public class Do_while extends Stmt {
    Expr expr;
    Stmt stmt;

    public Do_while() {
        expr = null;
        stmt = null;
    }

    public void init(Stmt s, Expr x) {
        expr = x;
        stmt = s;
        if (expr.type != Type.Bool) {
            error("boolean required in do-while");
        }
    }

    /*
            b: stmt
        label: if expr goto b
     */
    public void gen(int b, int a) {
        after = a;
        int label = newlabel();
        stmt.gen(b, label);
        emitlabel(label);
        expr.jumping(b, 0);
    }

    //S -> do M1 S1 while M2 (B);
    public void gen() {
        Expr B = expr;
        int M1_instr = Quadruple.nextinstr();
        stmt.gen();
        int M2_instr = Quadruple.nextinstr();
        B.gen();

        backpatch(stmt.nextlist, M2_instr);
        backpatch(B.truelist, M1_instr);
        nextlist = merge(nextlist, B.falselist);
    }

    public String AST_str(int col) {
        int newCol = col + 1;
        String AST_child = expr.AST_str(col + 1) +
                ",\n" +
                stmt.AST_str(col + 1);
        return "\t".repeat(Math.max(0, col)) +
                "Do_while(" +
                '\n' +
                AST_child +
                '\n' +
                "\t".repeat(Math.max(0, col)) +
                ')';
    }
}
