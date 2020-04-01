package demo.inter;

import demo.lexer.Tag;
import demo.lexer.Word;
import demo.symbols.Type;

public class Access extends Op{
    public Id array;
    public Expr index;
    public Access(Id a, Expr i, Type p){
        super(new Word("[]", Tag.INDEX), p);
        array = a;
        index = i;
    }
    public Expr gen(){
        return new Access(array, index.reduce(), type);
    }
    //bool value stored in the array
    public void jumping(int t, int f){
        emitjumps(reduce().toString(), t, f);
    }
    public String toString(){
        return array.toString() + "[" + index.toString() + "]";
    }

}
