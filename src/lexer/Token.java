package lexer;

public class Token {
    public final int tag;

    public Token(int t) {
        tag = t;
    }

    // ASCII 单字符token
    public String toString() {
        return "" + (char) tag;
    }

}
