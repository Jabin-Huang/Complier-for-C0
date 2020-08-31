package AST.expr;

public class Addr_label extends Expr {
    public int addr;

    public Addr_label(int i) {
        addr = i;
    }

    public String toString() {
        return "" + addr;
    }
}
