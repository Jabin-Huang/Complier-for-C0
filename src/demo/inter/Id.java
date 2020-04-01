package demo.inter;

import demo.lexer.Word;
import demo.symbols.Type;

public class Id extends Expr {
    public int offset;
    public Id(Word id, Type p, int b){
        super(id, p);
        offset = b;
    }

}
