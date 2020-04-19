package demo.inter;

import demo.lexer.Lexer;
import demo.main.Run;
import demo.parser.Parser;

public class Node {
    private int lexline;
    protected Node() { lexline = Lexer.line; }
    protected void error(String s){
        throw new Error("near line " + lexline + ":" + s);
    }
    private static int labels = 0;
    public int newlabel(){
        return ++labels;
    }
    public void emitlabel(int i){
        Run.outStringBuffer.append("L"+i+":");
    }
    protected void emit(String s){
        Run.outStringBuffer.append("\t" + s + "\n");
    }

}
