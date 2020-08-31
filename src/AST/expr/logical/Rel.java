package AST.expr.logical;

import AST.Quadruple;
import AST.expr.Expr;
import lexer.Tag;
import lexer.Token;
import lexer.Word;
import symbols.Array;
import symbols.Type;

public class Rel extends Logical {
    public Rel(Token tok, Expr x1, Expr x2) {
        super(tok, x1, x2);
    }

    public Type check(Type p1, Type p2) {
        if (p1 instanceof Array || p2 instanceof Array) {
            return null;
        } else if (p1 == p2) {
            return Type.Bool;
        } else return null;
    }


    public void gen() {
        Expr t1 = expr1.reduce(), t2 = expr2.reduce();
        truelist = makelist(Quadruple.nextinstr());
        falselist = makelist(Quadruple.nextinstr() + 1);
        Token jop;
        switch (op.tag) {
            case '<':
                jop = Word.jl;
                break;
            case '>':
                jop = Word.jg;
                break;
            case Tag.EQ:
                jop = Word.je;
                break;
            case Tag.GE:
                jop = Word.jge;
                break;
            case Tag.LE:
                jop = Word.jle;
                break;
            case Tag.NE:
                jop = Word.jne;
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + op.tag);
        }
        //(jop, exp1, exp2, 等待回填)
        Quadruple.ins.add(new Quadruple(jop, t1, t2, null));
        //(jmp, _, _, 等待回填)
        Quadruple.ins.add(new Quadruple(Word.jmp, null, null, null));
    }


    public String AST_str(int col) {
        String AST_child = "\t".repeat(col + 1) +
                op.toString() +
                ",\n" +
                expr1.AST_str(col + 1) +
                ",\n" +
                expr2.AST_str(col + 1);

        return "\t".repeat(col) +
                "Rel(" +
                '\n' +
                AST_child +
                '\n' +
                "\t".repeat(col) +
                ')';
    }
}
