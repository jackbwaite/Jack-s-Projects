/**
 * This Token class serves to represent GUIDO characters in the Scanner and Parser
 * The main piece of information is the Id ident variable, which is needed to initiate all Tokens
 * and classifies the character(s) in GUIDO format
 */
public class Token {

    public Id ident;
    public int intNumber;
    public char character;
    public String string;

    //Constructors
    public Token(Token t) {
        this.ident = t.getId();
        this.intNumber = t.getInt();
        this.character = t.getChar();
        this.string = t.getString();
    }

    public Token(Id ident) {
        this.ident = ident;
    }

    public Token(int intNum) {
        this.intNumber = intNum;
        this.ident = Id.INTEGER;
    }

    public Token(char character) {
        this.character = character;
        this.ident = Id.CHARACTER;
    }

    public Token(String str) {
        this.string = str;
        this.ident = Id.STRING;
    }

    public Token(Id ident, char character) {
        this.ident = ident;
        this.character = character;
    }

    public Token(Id ident, String str) {
        this.ident = ident;
        this.string = str;
    }

    //Accessor Methods
    public Id getId() {
        return ident;
    }

    public int getInt() {
        return intNumber;
    }

    public char getChar() {
        return character;
    }

    public String getString() {
        return string;
    }

    @Override
    public String toString() {
        String s = "ID: " + this.ident;
        s += "\nSTRING: " + this.string;
        s += "\nCHARACTER: " + this.character;
        s += "\nINTEGER: " + this.intNumber;
        return s;
    }
}
