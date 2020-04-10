package demo.main;

import demo.lexer.Lexer;
import demo.parser.Parser;

import java.io.*;

public class Run {
    private static InputStream instream;
    public static StringBuffer outStringBuffer;
    Run(String in){
        instream = new ByteArrayInputStream(in.getBytes());
    }
    public static String work() throws IOException {
          Lexer lex = new Lexer(instream);
          Parser parse = new Parser(lex);
          outStringBuffer = new StringBuffer();
          parse.program();
          outStringBuffer.append('\n');
          return outStringBuffer.toString();
    }
}
