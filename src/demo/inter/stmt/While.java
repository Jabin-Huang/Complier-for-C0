package demo.inter.stmt;

import demo.inter.Quadruple;
import demo.inter.expr.Addr_label;
import demo.inter.expr.Expr;
import demo.lexer.Word;
import demo.symbols.Type;

public class While extends Stmt {
    Expr expr;
    Stmt stmt;

    public While() {
        expr = null;
        stmt = null;
    }

    public void init(Expr x, Stmt s) {
        expr = x;
        stmt = s;
        if (expr.type != Type.Bool) {
            error("boolean required in while");
        }
    }


    /*
            b: ifFalse expr goto a
        label: stmt
               goto b
            a:

     */
    public void gen(int b, int a) {
        after = a;
        expr.jumping(0, a);
        int label = newlabel();
        emitlabel(label);
        stmt.gen(label, b);
        emit("goto L" + b);
    }

    //S -> while M1 (B) M2 S1
    public void gen() {
        int M1_instr = Quadruple.nextinstr();
        Expr B = expr;
        B.gen();
        int M2_instr = Quadruple.nextinstr();
        stmt.gen();

        backpatch(stmt.nextlist, M1_instr);
        backpatch(B.truelist, M2_instr);
        nextlist = merge(nextlist, B.falselist);
        //跳转到M1处，继续尝试循环
        //（jmp,_ ,_ , M1_instr）
        Quadruple.ins.add(new Quadruple(Word.jmp, null, null, new Addr_label(M1_instr)));
    }

    public String AST_str(int col) {
        String AST_child = expr.AST_str(col + 1) +
                ",\n" +
                stmt.AST_str(col + 1);

        return "\t".repeat(Math.max(0, col)) +
                "While(" +
                '\n' +
                AST_child +
                '\n' +
                "\t".repeat(Math.max(0, col)) +
                ')';
    }
}
