package AST.expr;

import lexer.Word;
import symbols.Type;

public class Id extends Expr {
    public int offset;

    public Id(Word id, Type p, int b) {
        super(id, p);
        offset = b;
    }

    public String AST_str(int col) {
        return "\t".repeat(Math.max(0, col)) +
                "Id(" + op.toString() + ',' + type.toString() + ',' + offset + ')';
    }

}
