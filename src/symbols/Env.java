package symbols;

import AST.expr.Function;
import AST.expr.Id;
import lexer.Word;

import java.util.Hashtable;
import java.util.Stack;

public class Env {
    public static Env top = null;
    private static Hashtable<Word, Function> funcTable = new Hashtable<Word, Function>();
    private static Stack<Env> S = new Stack<>();
    public int used = 0;
    private Hashtable<Word, Id> idTable;
    private Env prev;

    public Env(Env p) {
        idTable = new Hashtable<Word, Id>();
        prev = p;
        used = 0;
    }

    public static void push(Env e) {
        S.push(e);
    }

    public static Env pop() {
        return S.pop();
    }

    public static void putFunc(Word w, Function i) {
        funcTable.put(w, i);
    }

    public static Function getFunc(Word w) {
        Function found = funcTable.get(w);
        if (found != null) return found;
        else return null;
    }

    public void putId(Word w, Id i) {
        idTable.put(w, i);
    }

    public Id getId(Word w) {
        for (Env e = this; e != null; e = e.prev) {
            Id found = (Id) (e.idTable.get(w));
            if (found != null) return found;
        }
        return null;
    }

}
