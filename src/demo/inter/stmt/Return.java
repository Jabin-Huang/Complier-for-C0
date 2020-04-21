package demo.inter.stmt;

import demo.inter.expr.Expr;
import demo.inter.expr.Temp;
import demo.symbols.Type;

public class Return extends Stmt{
    Expr res;
    Type type;
    public Return(Expr x, Type t){
        res = x;
        type = t;
        if(check(x.type, type) == null){
            error("return type error");
        }
    }

    private Type check(Type p1, Type p2){
        if(Type.numeric(p1) && Type.numeric(p2)) return p2;
        else if(p1 == Type.Bool && p2 == Type.Bool) return p2;
        else return null;
    }

    public void gen(int b, int a){
        Temp ret_val = new Temp(type);
        emit(ret_val.toString() + " = " + res.toString());
        emit("ret");
    }

    public String AST_str(int col){
        return "\t".repeat(Math.max(0, col)) + "Return("+ "\n"+
                res.AST_str(col + 1) +
                "," + "\n" +
                "\t".repeat(Math.max(0, col + 1)) + type.toString() + "\n" +
                "\t".repeat(Math.max(0, col)) + ")";

    }


}
