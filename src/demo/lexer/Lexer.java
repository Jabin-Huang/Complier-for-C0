package demo.lexer;

import java.io.*;
import java.util.Hashtable;

public class Lexer {
    public int line = 1 ;
    private char peek = ' ';
    private Hashtable words = new Hashtable();

    void reserve(Word t){
        words.put(t.lexeme, t);
    }

    public Lexer(){
        reserve(new Word(Tag.TRUE, "true"));
        reserve(new Word(Tag.FALSE, "false"));
    }

    Token scan() throws IOException {
        //skip the blank and comments
        for( ; ; peek = (char)System.in.read()){
            if(peek == ' ' || peek =='\t') continue;
            else if(peek =='/'){
                peek = (char)System.in.read();
                //single line comments
                if(peek == '/'){
                    do { peek = (char)System.in.read(); } while(peek !='\n');
                    if(peek == '\n') line = line + 1;
                }
                //multi-line comments
                else if(peek == '*'){
                    do{
                        peek = (char)System.in.read();
                        if(peek == '\n') line = line + 1;
                        else if(peek == '*') {
                            peek = (char)System.in.read();
                            if(peek == '/') break;
                        }
                    } while(true);
                }
                //not comments, return '/'
                else {
                    Token t = new Token('/');
                    return t;
                }
            }
            else if(peek == '\n') line = line + 1;
            else break;
        }

        if(Character.isDigit(peek) ){
            int v = 0;
            do{
                v = 10*v + Character.digit(peek,10);
                peek =(char)System.in.read();
            } while(Character.isDigit(peek));
            return new Num(v);
        }
        if(Character.isLetter(peek)){
            StringBuffer b = new StringBuffer();
            do{
                b.append(peek);
                peek = (char) System.in.read();
            } while(Character.isLetterOrDigit(peek));
            String s = b.toString();
            //check if s is of reserved words.
            Word w = (Word)words.get(s);
            if(w != null) return w;
            //now s is an ID.
            w =new Word(Tag.ID, s);
            words.put(s, w);
            return w;
        }
        //single character
        Token t= new Token(peek);
        peek = ' ';
        return t;
    }
}
