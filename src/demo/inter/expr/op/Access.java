package demo.inter.expr.op;

import demo.inter.Quadruple;
import demo.inter.expr.Expr;
import demo.inter.expr.Id;
import demo.inter.expr.Temp;
import demo.lexer.Tag;
import demo.lexer.Word;
import demo.symbols.Type;

public class Access extends Expr {
    public Id array;
    public Expr index;

    public Access(Id a, Expr i, Type p) {
        super(new Word("[]", Tag.INDEX), p);
        array = a;
        index = i;
    }


    public Expr reduce() {
        Expr a1 = array;
        Expr a2 = index.reduce();
        Temp t = new Temp(type);
        //  emit(t.toString() + " = " + a1.toString() + "[" + a2.toString() + "]");
        Quadruple.ins.add(new Quadruple(this.op, a1, a2, t));
        return t;
    }

    public String toString() {
        return array.toString() + "[" + index.toString() + "]";
    }

    public String AST_str(int col) {
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
