package demo.inter;

import demo.inter.expr.Expr;
import demo.lexer.Token;

import java.util.Vector;

public class Quadruple {
    public static int count = 0;
    public static Vector<Quadruple> ins = new Vector<>();
    public Token op;
    public Expr arg1, arg2, result;
    public int offset;

    public Quadruple(Token t, Expr x1, Expr x2, Expr res) {
        offset = count;
        count++;
        op = t;
        arg1 = x1;
        arg2 = x2;
        result = res;
    }

    public static int nextinstr() {
        return count;
    }

    public String toString() {
        return String.format("%d： (%s, %s, %s, %s)", offset, op, String.valueOf(arg1), String.valueOf(arg2), String.valueOf(result));
    }
}
