package AST.stmt;

import AST.Quadruple;
import AST.expr.Expr;
import symbols.Type;

public class If extends Stmt {
    Expr expr;
    Stmt stmt;

    public If(Expr x, Stmt s) {
        expr = x;
        stmt = s;
        if (expr.type != Type.Bool) {
            error("boolean required in if");
        }
    }


    // S -> if(B) M S1
    public void gen() {
        Expr B = expr;
        B.gen();
        int M_instr = Quadruple.nextinstr();
        stmt.gen();
        //拉链回填：将M处的下一个指令地址回填到B的真出口
        backpatch(B.truelist, M_instr);
        nextlist = merge(B.falselist, stmt.nextlist);
    }

    public String AST_str(int col) {
        String AST_child = expr.AST_str(col + 1) +
                ",\n" +
                stmt.AST_str(col + 1);
        return "\t".repeat(Math.max(0, col)) +
                "If(" +
                '\n' +
                AST_child +
                '\n' +
                "\t".repeat(Math.max(0, col)) +
                ')';
    }


}
