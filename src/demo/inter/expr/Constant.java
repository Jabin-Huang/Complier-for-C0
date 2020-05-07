package demo.inter.expr;

import demo.inter.Quadruple;
import demo.lexer.Num;
import demo.lexer.Token;
import demo.lexer.Word;
import demo.symbols.Type;


public class Constant extends Expr {
    public static final Constant
            True = new Constant(Word.True, Type.Bool),
            False = new Constant(Word.False, Type.Bool);

    public Constant(Token tok, Type p) {
        super(tok, p);
    }

    public Constant(int i) {
        super(new Num(i), Type.Int);
    }

    //布尔常量
    public void jumping(int t, int f) {
        if (this == True && t != 0) {
            emit("goto L" + t);
        } else if (this == False && f != 0) {
            emit("goto L" + f);
        }
    }

    public void gen() {
        if (this == True) {
            truelist = makelist(Quadruple.nextinstr());
            //(jmp, _, _, 等待回填)
            Quadruple.ins.add(new Quadruple(Word.jmp, null, null, null));
        } else if (this == False) {
            falselist = makelist(Quadruple.nextinstr());
            //(jmp, _, _, 等待回填)
            Quadruple.ins.add(new Quadruple(Word.jmp, null, null, null));
        }
    }


    public String toString() {
        return op.toString();
    }

    public String AST_str(int col) {
        return "\t".repeat(Math.max(0, col)) +
                "Constant(" + op.toString() + ',' + type.toString() + ')';
    }
}
