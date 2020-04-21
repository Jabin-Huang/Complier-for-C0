package demo.inter.expr.op;

import demo.inter.expr.Expr;
import demo.inter.expr.Id;
import demo.lexer.Tag;
import demo.lexer.Word;
import demo.symbols.Type;

public class Access extends Op {
    public Id array;
    public Expr index;
    public Access(Id a, Expr i, Type p){
        super(new Word("[]", Tag.INDEX), p);
        array = a;
        index = i;
    }
//    public Expr gen(){
//        return new Access(array, index.reduce(), type);
//    }
    //数组里保存了布尔值
    public void jumping(int t, int f){
        emitjumps(reduce().toString(), t, f);
    }
    public String toString(){
        return array.toString() + "[" + index.reduce().toString() + "]";
    }

    public String AST_str(int col){
        String AST_child = array.AST_str(col + 1) +
                ",\n" +
                index.AST_str(col + 1) +
                ",\n" +
                "\t".repeat(Math.max(0, col + 1)) +
                type.toString();


        return "\t".repeat(Math.max(0, col)) +
                "Access(" +
                '\n' +
                AST_child +
                '\n' +
                "\t".repeat(Math.max(0, col)) +
                ')';
    }
}
