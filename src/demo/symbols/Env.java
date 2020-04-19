package demo.symbols;

import demo.inter.expr.Id;
import demo.lexer.Token;

import java.util.Hashtable;
import java.util.Stack;

public class Env {
    private Hashtable table;
    private Env prev;

    public static Env top = null;
    private static Stack<Env> S = new Stack<>();
    public Env(Env p){
        table = new Hashtable();
        prev = p;
    }

    public void put(Token w, Id i){
        table.put(w, i);
    }

    public static void push(Env e){
        S.push(e);
    }

    public static Env pop(){
         return S.pop();
    }

    public Id get(Token w){
        for(Env e = this; e != null; e = e.prev){
            Id found = (Id)(e.table.get(w));
            if(found != null) return found;
        }
        return null;
    }


}
