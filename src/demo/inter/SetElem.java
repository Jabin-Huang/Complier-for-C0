package demo.inter;

import demo.symbols.Type;

public class SetElem extends Stmt {
    public Id array;
    public Expr index;
    public Expr expr;
    public SetElem(Access x, Expr y){
        array = x.array;
        index = x.index;
        expr = y;
        if(Type.setElem_check(array.type, expr.type) == null){
            error("type error");
        }
    }
    public void gen(int b, int a){
        String s1 = index.reduce().toString();
        String s2 = expr.reduce().toString();
        emit(array.toString() + "[" + s1 + "] = " + s2 );
    }
}
