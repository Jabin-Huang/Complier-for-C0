package demo.inter.stmt;

import demo.inter.Node;

import java.util.Vector;

public class Stmt extends Node {
    public Vector<Integer> nextlist = new Vector<Integer>(); //待回填的跳转指令列表

    public static Stmt Null = new Stmt();

    int after = 0; //保存语句的下一条指令的标号

    public Stmt() {
    }

    public static Stmt Enclosing = Stmt.Null;  //用于break语句

    public void gen(int b, int a) {
    } // a标记语句之后的第一条指令，b标记语句代码的开始处

    public void gen() {
    }

    public String AST_str(int col) {
        return "\t".repeat(Math.max(0, col)) + "Stmt.null";
    }
}
