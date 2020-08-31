package AST.stmt;

import AST.Quadruple;
import AST.expr.Expr;
import lexer.Word;
import symbols.Type;

import java.util.Vector;

public class If_else extends Stmt {
    Expr expr;
    Stmt stmt1, stmt2;

    public If_else(Expr x, Stmt s1, Stmt s2) {
        expr = x;
        stmt1 = s1;
        stmt2 = s2;
        if (expr.type != Type.Bool) {
            error("boolean required in if");
        }
    }


    /*
        S -> if (B) M1 S1 N else M2 S2
     */
    public void gen() {
        Expr B = expr;
        B.gen();
        int M1_instr = Quadruple.nextinstr();
        stmt1.gen();
        //执行完S1， 要跳出if语句
        //(jmp, _, _, 待回填)
        Vector<Integer> N_nextlist = makelist(Quadruple.nextinstr());
        Quadruple.ins.add(new Quadruple(Word.jmp, null, null, null));
        int M2_instr = Quadruple.nextinstr();
        stmt2.gen();
        backpatch(B.truelist, M1_instr);
        backpatch(B.falselist, M2_instr);
        Vector<Integer> temp = merge(stmt1.nextlist, N_nextlist);
        nextlist = merge(temp, stmt2.nextlist);
    }

    public String AST_str(int col) {
        String AST_child =
                expr.AST_str(col + 1) +
                        ",\n" +
                        stmt1.AST_str(col + 1) +
                        ",\n" +
                        stmt2.AST_str(col + 1);
        return "\t".repeat(Math.max(0, col)) +
                "If(" +
                AST_child +
                '\n' +
                "\t".repeat(Math.max(0, col)) +
                ')';
    }
}
