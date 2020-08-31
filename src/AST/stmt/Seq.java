package AST.stmt;

import AST.Quadruple;

public class Seq extends Stmt {
    Stmt stmt1, stmt2;

    public Seq(Stmt s1, Stmt s2) {
        stmt1 = s1;
        stmt2 = s2;
    }


    // L -> S M L1
    public void gen() {
        if (stmt2 == Stmt.Null) {
            stmt1.gen();
            int M_instr = Quadruple.nextinstr();
            backpatch(stmt1.nextlist, M_instr);
            nextlist = stmt1.nextlist;
        } else {
            stmt1.gen();
            int M_instr = Quadruple.nextinstr();
            stmt2.gen();
            backpatch(stmt1.nextlist, M_instr);
            nextlist = stmt2.nextlist;
        }
    }

    public String AST_str(int col) {
        String AST_child = stmt1.AST_str(col + 1) +
                ",\n" +
                stmt2.AST_str(col + 1);

        return "\t".repeat(Math.max(0, col)) + "Seq(" + '\n' +
                AST_child + '\n' +
                "\t".repeat(Math.max(0, col)) + ')';
    }

}
