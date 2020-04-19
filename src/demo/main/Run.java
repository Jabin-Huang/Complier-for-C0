package demo.main;

import demo.lexer.Lexer;
import demo.parser.Parser;

import java.io.*;

public class Run {
    private static InputStream instream;
    public static StringBuffer outStringBuffer;
    Run(String in){
        instream = new ByteArrayInputStream(in.getBytes());
        outStringBuffer = new StringBuffer();
    }
    public static String work() throws IOException {
          Lexer lex = new Lexer(instream);
          Parser parse = new Parser(lex);
          parse.progarm();
          outStringBuffer.append('\n');
          return outStringBuffer.toString();
    }
}
