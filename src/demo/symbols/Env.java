package demo.symbols;

import demo.inter.Id;
import demo.lexer.Token;

import java.util.Hashtable;

public class Env {
    private Hashtable table;
    private Env prev;
    public static Env top;

    public Env(Env p){
        table = new Hashtable();
        prev = p;
    }

    public void put(Token w, Id i){
        table.put(w, i);
    }

    public Id get(Token w){
        for(Env e = this; e != null; e = e.prev){
            Id found = (Id)(e.table.get(w));
            if(found != null) return found;
        }
        return null;
    }


}
