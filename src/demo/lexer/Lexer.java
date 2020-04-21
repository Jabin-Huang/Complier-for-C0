package demo.lexer;

import demo.inter.expr.Temp;
import demo.symbols.Type;

import java.io.*;
import java.util.Hashtable;

public class Lexer {
    public static int line;
    private char peek = ' ';
    private Hashtable words = new Hashtable();
    private InputStream in;

    private void reserve(Word t){
        words.put(t.lexeme, t);
    }

    public Lexer(InputStream input){
        line = 1;
        Temp.count = 0;
        in = input;
        reserve(new Word("true", Tag.TRUE));
        reserve(new Word("false", Tag.FALSE));
        reserve(new Word("if", Tag.IF));
        reserve(new Word("else",Tag.IF));
        reserve(new Word("break",Tag.BREAK));
        reserve(new Word("while",Tag.WHILE));
        reserve(new Word("do",Tag.DO));
        reserve(new Word("return", Tag.RETURN));
        reserve(Type.Char);
        reserve(Type.Int);
        reserve(Type.Bool);
        reserve(Type.Float);
        reserve(Type.Void);
        Tag.init();
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
        //多字符运算符
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

        //数字
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
        //字母开头的关键字
        if(Character.isLetter(peek)){
            StringBuffer b = new StringBuffer();
            do{
                b.append(peek);
                readch();
            } while(Character.isLetterOrDigit(peek));
            String s = b.toString();
            //查看是否已是关键字
            Token w = (Token) words.get(s);
            if(w != null) return w;

            w =new Word(s, Tag.ID);
            words.put(s, w);
            return w;
        }
        //其他单字符
        Token t= new Token(peek);
        peek = ' ';
        return t;
    }
}
