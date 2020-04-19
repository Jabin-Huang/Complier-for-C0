package demo.inter.expr.op;

import demo.inter.expr.Temp;
import demo.inter.expr.Expr;
import demo.lexer.Token;
import demo.symbols.Type;

public class Op extends Expr {
    public Op(Token tok, Type p){
        super(tok, p);
    }
    //生成临时变量temp，并返回作为运算分量
    public Expr reduce(){
        Expr x = gen();
        Temp t = new Temp(type);
        emit(t.toString() + " = " + x.toString());
        return  t;
    }

}
