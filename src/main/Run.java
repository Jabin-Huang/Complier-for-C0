package main;

import AST.Quadruple;
import lexer.Lexer;
import parser.Parser;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class Run {
    public static StringBuffer outStringBuffer;
    private static InputStream instream;

    Run(String in) {
        instream = new ByteArrayInputStream(in.getBytes());
        outStringBuffer = new StringBuffer();
    }

    public static String work() throws IOException {
        Lexer lex = new Lexer(instream);
        Parser parse = new Parser(lex);
        parse.progarm();
        for (Quadruple q : Quadruple.ins) {
            outStringBuffer.append(q.toString() + "\n");
        }
        return outStringBuffer.toString();
    }
}
