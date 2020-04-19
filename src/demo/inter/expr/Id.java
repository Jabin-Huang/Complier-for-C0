package demo.inter.expr;

import demo.inter.Node;
import demo.inter.expr.Expr;
import demo.lexer.Word;
import demo.symbols.Type;

public class Id extends Expr {
    public int offset;
    public Id(Word id, Type p, int b){
        super(id, p);
        offset = b;
    }
    public String AST_str(int col){
        return "\t".repeat(Math.max(0, col)) +
                "Id(" + op.toString() + ',' + type.toString() + ',' + offset + ')';
    }

}
