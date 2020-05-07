package demo.lexer;

public class Word extends Token {
    //多字符token
    public final String lexeme;

    public static final Word
            //源程序中出现的关键词
            and = new Word("&&", Tag.AND), or = new Word("||", Tag.OR),
            eq = new Word("==", Tag.EQ), ne = new Word("!=", Tag.NE),
            le = new Word("<=", Tag.LE), ge = new Word(">=", Tag.GE),
            True = new Word("true", Tag.TRUE),
            False = new Word("false", Tag.FALSE),

    // 为了生成中间代码方便使用
    minus = new Word("minus", Tag.MINUS),
            temp = new Word("t", Tag.TEMP),
            call = new Word("call", Tag.CALL),
            parm = new Word("parm", Tag.PARM),
            ret = new Word("ret", Tag.RETURN),
            jmp = new Word("jmp", Tag.JMP),
            jz = new Word("jz", Tag.JZ),
            jnz = new Word("jnz", Tag.JNZ),
            jne = new Word("j!=", Tag.JNE),
            je = new Word("j==", Tag.JE),
            jle = new Word("j<=", Tag.JLE),
            jge = new Word("j>=", Tag.JGE),
            jl = new Word("j<", Tag.JL),
            jg = new Word("j>", Tag.JG),
            setElem = new Word("[]= ", Tag.SETELEM);

    public Word(String s, int t) {
        super(t);
        lexeme = s;
    }

    public String toString() {
        return lexeme;
    }

}
