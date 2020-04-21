package demo.inter.stmt;

public class Seq extends Stmt {
    Stmt stmt1, stmt2;
    public Seq(Stmt s1, Stmt s2){
        stmt1 = s1;
        stmt2 = s2;
    }

    /*
                stm1
        label:  stmt2
     */
    public void gen(int b, int a){
        if(stmt2 == Stmt.Null){
            stmt1.gen(b, a);
        }
        else {
            int label = newlabel();
            stmt1.gen(b, label);
            emitlabel(label);
            stmt2.gen(label, a);
        }
    }

    public String AST_str(int col){
        String AST_child = stmt1.AST_str(col + 1) +
                ",\n"+
                stmt2.AST_str(col + 1);

        return "\t".repeat(Math.max(0, col)) + "Seq(" + '\n' +
                AST_child + '\n' +
                "\t".repeat(Math.max(0, col)) + ')';
    }

}
