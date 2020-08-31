package AST.expr.op;

import AST.Quadruple;
import AST.expr.Expr;
import AST.expr.Id;
import AST.expr.Temp;
import lexer.Tag;
import lexer.Word;
import symbols.Type;

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
                "\t".repeat(col + 1) +
                type.toString();

        return "\t".repeat(col) +
                "Access(" +
                '\n' +
                AST_child +
                '\n' +
                "\t".repeat(col) +
                ')';
    }
}
