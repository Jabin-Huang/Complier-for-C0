package demo.inter;

import demo.symbols.Type;

public class Assign extends Stmt {
    public Id id;
    public Expr expr;
    public Assign(Id i, Expr x){
        id = i;
        expr = x;
        if(Type.assign_check(id.type, expr.type)  == null){
            error("type error");
        }
    }
    public void gen(int b, int a){
        emit(id.toString() + " = "+ expr.gen().toString());
    }


}
