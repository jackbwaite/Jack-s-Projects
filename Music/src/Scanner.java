import java.util.Stack;

/**
 * The Scanner class is initiated with an Input object, which is assumed to be a text file. It serves as an
 * induction point sheet music file.
 * This class scans a text file character by character, using the nextChar() method to do so.
 * getNextToken() creates Token objects using names from the Id enum when it recognizes
 * characters with specific meaning.
 * A Stack is used to put off analysis of certain characters
 */
public class Scanner {

    private Input in;
    private Stack<Token> state = new Stack<>();
    private int c; //Current character as ASCII int

    /**
     * Construct a Scanner object from an Input
     * @param in  an Input object made from a file or InputStream
     */
    public Scanner(Input in) {
        this.in = in;
    }

    /**
     * Update int c to the next character in Input
     *
     * Returns -1 when encounters the end of the file
     */
    private void nextChar() {
        c = in.getChar();
    }

    /**
     * Analyze current character from the Input, represented by int c, and return as a Token,
     * which is passed onto the Parser
     *
     * Whitespace is skipped over
     *
     * @return a Token object initialized with information about the character
     */
    public Token getNextToken() {

        //Check if there's a stack; if so, analyze and return the top of the stack; if not, scan the next character
        if (!state.empty()) {
            return state.pop();
        }

        //Find and return the first correct Token
        nextChar();
        while (c >= 0 && c <= 32) {
            nextChar(); //Skips whitespace
        }
        if (c == '{') {
            return new Token(Id.OPEN_CPAREN, '{');
        } else if (c == '}') {
            return new Token(Id.CLOSED_CPAREN, '}');
        } else if (c == '[') {
            return new Token(Id.OPEN_BRACKET, '[');
        } else if (c == ']') {
            return new Token(Id.CLOSED_BRACKET, ']');
        } else if (c == '<') {
            return new Token(Id.OPEN_ANGLE, '<');
        } else if (c == '>') {
            return new Token(Id.CLOSED_ANGLE, '>');
        } else if (c == ',') {
            return new Token(Id.COMMA, ',');
        } else if (c == '-') {
            return new Token(Id.HYPHEN, '-');
        } else if (c == '/') {
            return new Token(Id.SLASH, '/');
        } else if (c == '#') {
            return new Token(Id.SHARP, '#');
        } else if (c == '&') {
            return new Token(Id.FLAT, '&');
        } else if (c == '.') {
            return new Token(Id.DOT, '.');
        } else if (c == '*') {
            return new Token(Id.ASTERISK, '*');
        } else if (c == '_') {
            return new Token(Id.REST, '_');
        } else if (c >= '0' && c <= '9') {
            return new Token(c - '0');
        } else if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')) {
            return new Token((char) c);
        } else if (c == '\\') {
            //Returns BACK_SLASH, putting back the TAGNAME following it and an OPEN_ANGLE if applicable
            putback(new Token(Id.TAGNAME, getTagname()));
            return new Token(Id.BACK_SLASH, '\\');
        } else if (c == '\"') {
            //Returns QUOTE_MARK, putting back the final QUOTE_MARK and the STRING
            putback(new Token(getString()));
            return new Token(Id.QUOTE_MARK, '\"');
        } else if (c == -1) {
            //STOP means the Scanner has reached the end of the file
            return new Token(Id.STOP);
        } else {
            //NULL_TOKEN represents an error, or we're missing a current internal representation for the character scanned
            return new Token(Id.NULL_TOKEN);
        }
    }

    /**
     * Add the given Token to the stack, essentially "putting back" the current character for later analysis
     * @param tok  the Token to be put on top of the Stack
     */
    public void putback(Token tok) {
        state.push(tok);
    }

    /**
     * Returns the next Token and then puts it back on the Stack
     *
     * @return the next Token
     */
    public Token peak() {
        Token tok = getNextToken();
        putback(tok);
        return tok;
    }

    /**
     * Tells you if there is a character after this one; used for knowing when to stop scanning
     *
     * @return true if there is a character after the current one; false if end of the file
     */
    public boolean hasNext() {
        Token tok = getNextToken();
        if (tok.getId() == Id.STOP) {
            return false;
        }
        putback(tok);
        return true;
    }

    /**
     * Returns full String of the Tagname indicated by a backslash
     *
     * Note: a Tagname stops after a space or a parameter begins with an open angle <
     *
     * @return the current Tagname as a String for processing as a TAGNAME Token
     */
    private String getTagname() {
        String s = "";
        nextChar(); //First character in the Tagname
        while(c != ' ' && c != '<' && c != -1) {
            s = s + (char) c;
            nextChar();
        }
        if (c == '<') {
            putback(new Token(Id.OPEN_ANGLE, '<'));
        }
        if (c == -1) {
            in.fatal("Scanning error when getting Tagname");
        }
        return s;
    }

    /**
     * Returns full String within two quotation marks and puts back the terminal quotation mark
     *
     * @return the current String, as signaled by first scanning a quotation mark
     */
    private String getString() {
        String s = "";
        nextChar(); //First character in the String
        while(c != '\"' && c != -1) {
            s += (char) c;
            nextChar();
        }
        if (c == '\"') {
            putback(new Token(Id.QUOTE_MARK));
        }
        if (c == -1) {
            in.fatal("Missing closing quote in String");
        }
        return s;
    }
}