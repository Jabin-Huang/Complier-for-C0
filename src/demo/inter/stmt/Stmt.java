package demo.inter.stmt;

import demo.inter.Node;

public class Stmt extends Node {
    public Stmt(){}
    public static Stmt Null = new Stmt();
    public void gen(int b, int a){} // a标记语句之后的第一条指令，b标记语句代码的开始处
    int after = 0; //保存语句的下一条指令的标号
    public static Stmt Enclosing = Stmt.Null;  //用于break语句
    public String AST_str(int col){
        return "\t".repeat(Math.max(0, col)) +
                "Stmt.null" ;
    }
}
