package demo.symbols;

import demo.lexer.Token;

import java.util.Hashtable;

public class Env {
    private Hashtable table;
    protected Env prev;

    public Env(Env p){
        table = new Hashtable();
        prev = p;
    }

    /*
    public void put(Token w, Id i){
        table.put(w, i);
    }

    public Id get(Token w){
        for(Env e = this; e != null; e = e.prev){
            Symbol found = (Symbol)(e.table.get(s));
            if(found != null) return found;
        }
        return null;
    }
     */
}
