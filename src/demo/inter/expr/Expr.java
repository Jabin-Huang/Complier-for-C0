package demo.inter.expr;

import demo.inter.Node;
import demo.lexer.Token;
import demo.symbols.Type;

public class Expr extends Node {
    public Token op;
    public Type type;
    protected Expr(Token tok, Type p){
        op = tok;
        type = p;
    }
    Expr(){}

    public static Expr Null = new Expr();

    //对非叶子结点x，将其各个运算分量通过reduce（）规约成temp，返回分量规约后的新结点x
    //若是常量、标识符、temp 则返回自身， 带有运算符的其他表达式在子类按需重写
    //public Expr gen(){
    //    return this;
    //}

    //返回非叶子结点归约后的结果，其用一个地址temp表示
    //若是常量、标识符、temp 则返回自身， 带有运算符的其他表达式在op类重写，返回一个 temp
    public Expr reduce(){
        return this;
    }

    //为布尔表达式生成跳转语句
    public void jumping(int t, int f){
        emitjumps(toString(), t, f);
    }
    public void emitjumps(String test, int t, int f){
        if(t != 0 && f != 0){
            emit("if" + test + "goto L" +t);
            emit("goto L" + f);
        }
        else if(t != 0) emit("if " + test + " goto L"  + t);
        else if(f != 0) emit("ifFalse " + test + " goto L" + f);
        else ;
    }

    //在子类重写
    public String toString(){
         return op.toString();
    }

    //在子类重写
     public String AST_str(int col){
        return null;
    }

}
