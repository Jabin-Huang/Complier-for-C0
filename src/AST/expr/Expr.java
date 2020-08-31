package AST.expr;

import AST.Node;
import AST.Quadruple;
import lexer.Token;
import lexer.Word;
import symbols.Type;

import java.util.Vector;

public class Expr extends Node {
    public static Expr Null = new Expr();
    public Token op;
    public Type type;
    public Vector<Integer> truelist = new Vector<>(), falselist = new Vector<>(); //包含跳转指令的列表

    public Expr(Token tok, Type p) {
        op = tok;
        type = p;
    }

    Expr() {
    }

    //返回非叶子结点归约后的结果，其用一个地址temp表示
    //若是不需归约（常量、标识符、temp ）则返回自身，需归约的在各自重写的方法里返回一个 temp
    public Expr reduce() {
        return this;
    }

    //针对单个布尔变量（可能是数组元素）
    public void gen() {
        Expr x = this.reduce();
        truelist = makelist(Quadruple.nextinstr());
        //(jnz, x, _, 等待回填)
        Quadruple.ins.add(new Quadruple(Word.jnz, x, null, null));
        falselist = makelist(Quadruple.nextinstr());
        //(jz, x, _, 等待回填)
        Quadruple.ins.add(new Quadruple(Word.jz, x, null, null));
    }


    //在子类重写
    public String toString() {
        return String.valueOf(op);
    }

    //在子类重写
    public String AST_str(int col) {
        return null;
    }

}
