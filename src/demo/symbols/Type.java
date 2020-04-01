package demo.symbols;

import demo.lexer.Tag;
import demo.lexer.Word;

public class Type extends Word {
    public int width = 0;
    public Type(String s, int tag, int v){
        super(s, tag);
        width = v;
    }
    public static final Type
        Int = new Type("int", Tag.BASIC, 4),
        Float = new Type("float", Tag.BASIC, 8),
        Char = new Type("char", Tag.BASIC, 1),
        Bool = new Type("bool", Tag.BASIC, 1);

    public static boolean numeric(Type p){
        if(p == Type.Char || p == Type.Int || p == Type.Float) return true;
        else return false;
    }

    public static Type max(Type p1, Type p2){
        if(!numeric(p1) || !numeric(p2)) return null;
        else if(p1 == Type.Float || p2 == Type.Float) return Type.Float;
        else if(p1 == Type.Int || p2 == Type.Int) return Type.Int;
        else return Type.Char;
    }
    public static Type bool_check(Type p1, Type p2){
        if(p1 == Type.Bool && p2 == Type.Bool) {
            return Type.Bool;
        }
        else return null;
    }
    public static Type rel_check(Type p1, Type p2){
        if(p1 instanceof Array || p2 instanceof Array){
            return null;
        }
        else if(p1 == p2){
            return Type.Bool;
        }
        else return null;
    }

}
