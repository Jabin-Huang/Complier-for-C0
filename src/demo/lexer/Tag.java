package demo.lexer;

import java.util.Hashtable;

public class Tag {
    public final static int
            NUM = 256,
            ID    = 257,
            TRUE = 258,
            FALSE = 259,
            BASIC = 260,
            IF  = 261,
            WHILE = 262,
            ELSE = 263,
            DO    = 264,
            EQ    = 265,
            NE  = 266,
            MINUS = 267,
            LE   = 268,
            GE    = 269,
            REAL  = 270,
            AND = 271,
            OR    = 272,
            INDEX = 273,
            BREAK = 274,
            TEMP = 275,
            RETURN = 276;

    public static Hashtable<Integer, String> tagToString = new Hashtable<>();
    static void init(){
        tagToString.put(NUM, "integer");
        tagToString.put(ID, "identifier");
        tagToString.put(BASIC, "basic type" );
    }
}
