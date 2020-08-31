package AST;

import AST.expr.Constant;
import lexer.Lexer;
import main.Run;

import java.util.Vector;

public class Node {
    private int lexline;

    protected Node() {
        lexline = Lexer.line;
    }

    public static Vector<Integer> makelist(int index) {
        Vector<Integer> t = new Vector<>();
        t.add(index);
        return t;
    }

    public static Vector<Integer> merge(Vector<Integer> p1, Vector<Integer> p2) {
        Vector<Integer> p3 = new Vector<>();
        p3.addAll(p1);
        p3.addAll(p2);
        return p3;
    }

    public static void backpatch(Vector<Integer> p, int index) {
        for (int i : p) {
            Quadruple.ins.get(i).result = new Constant(index);
        }
    }

    protected void error(String s) {
        throw new Error("near line " + lexline + ":" + s);
    }


    protected void emit(String s) {
        Run.outStringBuffer.append("\t" + s + "\n");
    }


}
