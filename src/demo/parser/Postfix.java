package demo.parser;
import java.io.*;
class PostfixParser {
    static  int lookahead;

    public PostfixParser() throws  IOException{
        lookahead = System.in.read();
    }

    void expr() throws IOException{
        terms();
        while(true){
            if(lookahead == '+'){
                match('+'); terms(); System.out.write('+');
            }
            else if(lookahead == '-'){
                match('-'); terms(); System.out.write('-');
            }
            else return;
        }
    }

    void terms() throws IOException{
        if( Character.isDigit((char)lookahead) ){
            System.out.write((char)lookahead); match(lookahead);
        }
        else throw new Error("syntax error");
    }

    void match(int t) throws IOException{
        if(lookahead == t) lookahead = System.in.read();
        else throw new Error("syntax error");
    }
}

public class Postfix{
    public static void main(String[] args) throws IOException{
        PostfixParser parse = new PostfixParser();
        parse.expr();
        System.out.write('\n');
    }
}
