package demo.lexer;

import demo.symbols.Type;

import java.io.*;
import java.util.Hashtable;

public class Lexer {
    public static int line = 1 ;
    private char peek = ' ';
    private Hashtable words = new Hashtable();
    private InputStream in;
    private void reserve(Word t){
        words.put(t.lexeme, t);
    }

    public Lexer(InputStream input){
        line = 1;
        in = input;
        reserve(new Word("true", Tag.TRUE));
        reserve(new Word("false", Tag.FALSE));
        reserve(new Word("if", Tag.IF));
        reserve(new Word("else",Tag.IF));
        reserve(new Word("break",Tag.BREAK));
        reserve(new Word("while",Tag.WHILE));
        reserve(new Word("do",Tag.DO));
        reserve(Type.Char);
        reserve(Type.Int);
        reserve(Type.Bool);
        reserve(Type.Float);
    }

    private void readch() throws IOException {
        peek = (char)in.read();
    }

    private boolean readch(char c) throws IOException {
        readch();
        if(peek != c) return false;
        peek = ' ';
        return true;
    }

    public Token scan() throws IOException {
        //skip the blank and comments
        for( ; ; readch()){
            if(peek == ' ' || peek =='\t' || peek == '\r') continue;
            else if(peek =='/'){
                readch();
                //single line comments
                if(peek == '/'){
                    do { readch(); } while(peek !='\n');
                    if(peek == '\n') line = line + 1;
                }
                //multi-line comments
                else if(peek == '*'){
                    do{
                        readch();
                        if(peek == '\n') line = line + 1;
                        else if(peek == '*') {
                            readch();
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
        //multi-character op
        switch (peek){
            case '&':
                if(readch('&')) return Word.and; else return new Token('&');
            case '|':
                if(readch('|')) return Word.or; else return new Token('|');
            case '=':
                if(readch('=')) return Word.eq; else return new Token('=');
            case '!':
                if(readch('=')) return Word.ne; else return new Token('!');
            case '<':
                if(readch('=')) return Word.le; else return new Token('<');
            case '>':
                if(readch('=')) return Word.ge; else return new Token('>');
        }

        //num (include real)
        if(Character.isDigit(peek) ){
            int v = 0;
            do{
                v = 10*v + Character.digit(peek,10);
                readch();
            } while(Character.isDigit(peek));
            if(peek != '.') return new Num(v);
            float x = v, d = 10;
            for(;;){
                readch();
                if(!Character.isDigit(peek)) break;
                x = x + Character.digit(peek, 10) / d;
                d = d * 10;
            }
            return new Real(x);
        }
        if(Character.isLetter(peek)){
            StringBuffer b = new StringBuffer();
            do{
                b.append(peek);
                readch();
            } while(Character.isLetterOrDigit(peek));
            String s = b.toString();
            //check if s is of reserved words.
            Word w = (Word)words.get(s);
            if(w != null) return w;
            //now s is an ID.
            w =new Word(s, Tag.ID);
            words.put(s, w);
            return w;
        }
        //single character
        Token t= new Token(peek);
        peek = ' ';
        return t;
    }
}
