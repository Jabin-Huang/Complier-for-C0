package demo.inter.expr;

import demo.lexer.Word;
import demo.symbols.Type;

public class Temp extends Expr {
    public static int count = 0;
    private int number = 0;

    public Temp(Type p) {
        super(Word.temp, p);
        number = ++count;
    }

    public String toString() {
        return "t" + number;
    }

    public String AST_str(int col) {
        return "\t".repeat(Math.max(0, col)) +
                "Temp(" + type.toString() + ")";
    }
}
