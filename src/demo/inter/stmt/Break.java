package demo.inter.stmt;

import demo.inter.Quadruple;
import demo.lexer.Word;

public class Break extends Stmt {
    Stmt stmt;

    public Break() {
        if (Stmt.Enclosing == Stmt.Null) {
            error("unenclosed break");
        }
        // stmt外围构造
        stmt = Stmt.Enclosing;
    }

    //after : 紧跟stmt之后的代码
    public void gen(int b, int a) {
        emit("goto L" + stmt.after);
    }

    public void gen() {
        nextlist = makelist(Quadruple.nextinstr());
        //与外层循环结构的nextlist合并
        stmt.nextlist = merge(stmt.nextlist, nextlist);
        //(jmp,_,_,待回填)
        Quadruple.ins.add(new Quadruple(Word.jmp, null, null, null));
    }

    public String AST_str(int col) {
        return "\t".repeat(Math.max(0, col)) +
                "Break()";
    }
}
