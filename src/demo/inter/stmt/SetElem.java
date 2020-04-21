package demo.inter.stmt;

import demo.inter.expr.Expr;
import demo.inter.expr.Id;
import demo.inter.expr.op.Access;
import demo.symbols.Array;
import demo.symbols.Type;

public class SetElem extends Stmt {
    public Id array;
    public Expr index;
    public Access access;
    public Expr expr;
    public SetElem(Access x, Expr y){
        access = x;
        array = x.array;
        index = x.index;
        expr = y;
        if(check(x.type, expr.type) == null){
            error("type error");
        }
    }


    public Type check(Type p1, Type p2){
        if(p1 instanceof Array || p2 instanceof Array){
            return null;
        }
        else if(p1 == p2){
            return p2;
        }
        else if(Type.numeric(p1) && Type.numeric(p2)){
            return p2;
        }
        else return null;
    }

    public void gen(int b, int a){
        String s1 = index.reduce().toString();
        String s2 = expr.reduce().toString();
        emit(array.toString() + "[" + s1 + "] = " + s2 );
    }
    public String AST_str(int col){
        String AST_child = access.AST_str(col + 1) +
                ",\n"+
                expr.AST_str(col + 1);
        return "\t".repeat(Math.max(0, col)) +
                "SetElem(" +
                '\n' +
                AST_child +
                '\n' +
                "\t".repeat(Math.max(0, col)) +
                ')';
    }

}
