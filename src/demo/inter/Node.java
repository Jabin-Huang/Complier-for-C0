package demo.inter;

import demo.lexer.Lexer;
import demo.main.Run;
import demo.parser.Parser;

public class Node {
    private int lexline = 0;
    Node() { lexline = Lexer.line; }
    void error(String s){
        throw new Error("near line " + lexline + ":" + s);
    }
    private static int labels = 0;
    public int newlabel(){
        return ++labels;
    }
    public void emitlabel(int i){
        Run.outStringBuffer.append("L"+i+":");
    }
    public void emit(String s){
        Run.outStringBuffer.append("\t" + s + "\n");
    }
}
