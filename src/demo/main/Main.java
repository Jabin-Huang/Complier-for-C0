package demo.main;

import demo.lexer.Lexer;
import demo.parser.Parser;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class Main {
    public static void main(String[] args) throws IOException{
        InputStream f = new FileInputStream("./program.txt");
        Lexer lex = new Lexer(f);
        Parser parse = new Parser(lex);
        parse.program();
        System.out.write('\n');
    }
}
