package demo.inter;

public class Break extends Stmt {
    Stmt stmt;
    public Break(){
        if(Stmt.Enclosing == Stmt.Null){
            error("unenclosed break");
        }
        // stmt外围构造
        stmt = Stmt.Enclosing;
    }
    //after : 紧跟stmt之后的代码
    public void gen(int b, int a){
        emit("goto L" + stmt.after);
    }
}