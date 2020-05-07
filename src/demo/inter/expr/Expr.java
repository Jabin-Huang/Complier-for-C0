package demo.inter.expr;

import demo.inter.Node;
import demo.inter.Quadruple;
import demo.lexer.Token;
import demo.lexer.Word;
import demo.symbols.Type;

import java.util.Vector;

public class Expr extends Node {
    public Token op;
    public Type type;
    public Vector<Integer> truelist = new Vector<>(), falselist = new Vector<>(); //包含跳转指令的列表

    public Expr(Token tok, Type p) {
        op = tok;
        type = p;
    }

    Expr() {
    }

    public static Expr Null = new Expr();

    //返回非叶子结点归约后的结果，其用一个地址temp表示
    //若是常量、标识符、temp 则返回自身， 带有运算符的其他表达式在op类重写，返回一个 temp
    public Expr reduce() {
        return this;
    }

    //为布尔表达式生成跳转语句
    public void jumping(int t, int f) {
        emitjumps(reduce().toString(), t, f);
    }

    public void emitjumps(String test, int t, int f) {
        if (t != 0 && f != 0) {
            emit("if" + test + "goto L" + t);
            emit("goto L" + f);
        } else if (t != 0) emit("if " + test + " goto L" + t);
        else if (f != 0) emit("ifFalse " + test + " goto L" + f);
        else ;
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
