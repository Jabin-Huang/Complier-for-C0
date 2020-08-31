package AST.expr.logical;

import AST.Quadruple;
import AST.expr.Constant;
import AST.expr.Expr;
import AST.expr.Temp;
import lexer.Token;
import lexer.Word;
import symbols.Type;

import java.util.Vector;

public class Logical extends Expr {
    public Expr expr1, expr2;

    Logical(Token tok, Expr x1, Expr x2) {
        super(tok, null);
        expr1 = x1;
        expr2 = x2;
        type = check(expr1.type, expr2.type);
        if (type == null) error("type error");
    }

    public Type check(Type p1, Type p2) {
        if (p1 == Type.Bool && p2 == Type.Bool) {
            return Type.Bool;
        } else return null;
    }


    public Expr reduce() {
        Temp temp = new Temp(type);
        //先为E生成跳转指令
        this.gen();
        //在真出口将true赋给一个临时变量, 对跳转到该处的跳转指令进行回填
        backpatch(truelist, Quadruple.nextinstr());
        //（ = , true , _ , t)
        Quadruple.ins.add(new Quadruple(new Token('='), Constant.True, null, temp));
        //( jmp, _ , _ , 等待回填) 跳转到E之后的第一条指令
        Vector<Integer> nextlist = makelist(Quadruple.nextinstr());
        Quadruple.ins.add(new Quadruple(Word.jmp, null, null, null));
        //在假出口将false赋给一个临时变量， 对跳转到该处的跳转指令进行回填
        backpatch(falselist, Quadruple.nextinstr());
        //（ = , false , _ , t)
        Quadruple.ins.add(new Quadruple(new Token('='), Constant.False, null, temp));
        //将下一条指令的序号回填到nextlist里的跳转指令里
        backpatch(nextlist, Quadruple.nextinstr());
        return temp;
    }


    public String toString() {
        return expr1.toString() + " " + op.toString() + " " + expr2.toString();
    }
}
