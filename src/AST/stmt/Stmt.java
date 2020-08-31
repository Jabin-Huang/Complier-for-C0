package AST.stmt;

import AST.Node;

import java.util.Vector;

public class Stmt extends Node {
    public static Stmt Null = new Stmt();
    public static Stmt Enclosing = Stmt.Null;  //用于break语句
    public Vector<Integer> nextlist = new Vector<Integer>(); //待回填的跳转指令列表
    int after = 0; //保存语句的下一条指令的标号

    public Stmt() {
    }

    public void gen() {
    }

    public String AST_str(int col) {
        return "\t".repeat(Math.max(0, col)) + "Stmt.null";
    }
}
