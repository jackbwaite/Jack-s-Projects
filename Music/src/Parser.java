/**
 * The Parser class is used to organize Tokens into "sentences" by grouping them into methods.
 * Identification of certain Tokens require specific syntax (e.g. voices need []).
 * Thus, each method in the recursive descent returns true if this syntax is proper GUIDO format;
 * a false indicates improper GUIDO syntax.
 * The transpose() method is called when a Note is detected.
 */
public class Parser {

    private Scanner scan;
    private Output out;
    private String file = "";

    /**
     * Construct a Parser with a Scanner
     * @param sc a Scanner
     */
    public Parser(Scanner sc) {
        this.scan = sc;
        out = new Output();
    }

    /**
     * Construct a Parser with an Input object by constructing a Scanner
     * @param in an Input
     */
    public Parser(Input in) {
        this.scan = new Scanner(in);
        out = new Output();
    }

    /**
     * Construct a Parser with a filename, which is used to make an Input and then a Scanner
     * @param filename the name of a GUIDO file
     */
    public Parser(String filename) {
        this.scan = new Scanner(new Input(filename));
        out = new Output();
    }

    /**
     * Construct a Parser that will fill a second file with the transposition of the first
     * @param file1 an input GUIDO file
     * @param file2
     */
    public Parser(String file1, String file2) {
        this.scan = new Scanner(new Input(file1));
        out = new Output(file2);
    }

    //Accessor method to retrieve filename
    public String getFile() {
        return file;
    }

    /**
     * Retrieve the next processed Token from the Scanner class. Used to retrieve "letters" of the GUIDO language.
     * @return the next Token in the file
     */
    public Token scanToken() {
        return scan.getNextToken();
    }

    /**
     * Imported from Scanner: puts given Token back on the Stack in Scanner
     * @param tok the Token to be put on the Stack in Scanner
     */
    public void putback(Token tok) {
        scan.putback(tok);
    }

    /**
     * Imported from Scanner: peaks at the next Token and puts it back on the Stack
     * @return the next Token
     */
    public Token peak() {
        return scan.peak();
    }

    /**
     * "Expects" the next Token based on GUIDO syntax. Checks if the next Token matches a guessed Id.
     * If true, the Token is processed; if false, the Token is placed back on the Stack
     * @param ident the guessed Id of the next Token
     * @return true if Id matches Token; false if they don't match
     */
    public boolean expect(Id ident) {
        Token tok = scanToken();
        if (ident == tok.getId()) {
            return true;
        }
        else {
            putback(tok);
            return false;
        }
    }

    /**
     * Initiate recursive descent parsing
     */
    public void printScore() {
        score();
    }

    /**
     * Beginning of Recursive Descent
     *
     * Booleans are used to check for proper GUIDO syntax; a false will stop the parsing.
     * Characters and Tokens are printed through the Output class as their identity is confirmed.
     * Additionally, each Token is added to the file variable.
     */

    /**
     * Scores are the highest-order and are equivalent ot the file itself. GUIDO scores start with an open brace {
     * and end with a closed brace }. A list of voices is likely within a score but is not required.
     *
     * @return true if proper syntax
     */
    public boolean score() {
        if (expect(Id.OPEN_CPAREN)) {
            out.print('{'); file += "{";
            if (voiceList()
                    && expect(Id.CLOSED_CPAREN)) {
                out.print('}'); file += "}";
                return true;
            }
            return false;
        }
        return false;
    }

    /**
     * Voicelist is a series of voices, but it is valid for there to be only one voice or none at all.
     * Voice() is the method for if a voice is detected; a voice starts with an open bracket [ and ends with a
     * closed bracket ].
     * Voice() is called again if a comma is detected after the first voice, which indicates more than one voice.
     *
     * @return true if proper syntax
     */
    public boolean voiceList() {
        if (voice()) {
            Token tok = scanToken();
            if (tok.getId() == Id.COMMA) {
                out.print(','); file += ",";
                return voiceList();
            }
            putback(tok);
            return true;
        }
        return true;
    }

    /**
     * Voice is a list of notes, chords, and commands. An empty voice is valid. Voices are started with an
     * open bracket [ and ended with a closed bracket ].
     *
     * @return true if proper syntax
     */
    public boolean voice() {
        if(expect(Id.OPEN_BRACKET)) {
            out.print('['); file += "[";
            if (symbolsList()
                    && expect(Id.CLOSED_BRACKET)) {
                out.print(']'); file += "]";
                return true;
            }
            return false;
        }
        return false;
    }

    /**
     * SymbolsList is all the "symbols" within a voice, including notes, chords, and commands.
     * Individual symbols are processed by Symbol(), and there is always a space in between symbols.
     * SymbolsList() is called again if there is more than one symbol.
     *
     * @return true if proper syntax
     */
    public boolean symbolsList() {
        if (peak().getId() == Id.CLOSED_BRACKET) { //Always first check for the end of the voice
            return true;
        }
        if (symbol()) {
            out.print(" "); file += " ";
            return symbolsList();
        }
        return false;
    }

    /**
     * Symbol processes the individual notes, chords, and commands. The identity of the following Token
     * is checked before shuttling it into the proper method, i.e. Note(), Rest(), Chord(), or Tag().
     * A symbol must be one of these Id types, or else the syntax is invalid.
     *
     * @return true if proper syntax
     */
    public boolean symbol() {
        if (peak().getId() == Id.CHARACTER) {
            return note();
        } else if (peak().getId() == Id.REST) {
            return rest();
        } else if (peak().getId() == Id.OPEN_CPAREN) {
            return chord();
        } else if (peak().getId() == Id.BACK_SLASH) {
            return tag();
        }
        return false;
    }

    /**
     * Note process the note, including its tone, octave, and duration. The Note class is used to call
     * the Transpose() method, which changes the note according to a given number of half-steps.
     * A typical Note requires a character to indicate tone. Everything directly following is not required, but
     * must be in this order: accidentals, octave, duration and dots.
     *
     * @return true if proper syntax
     */
    private boolean note() {
        Token tok = scanToken();
        String noteName = tok.getChar() + accidentals();
        Note n = new Note(noteName);
        if (octave()) {

            n.transpose();
            out.print(n.getNote() + n.getOctave()); file += (n.getNote() + n.getOctave());

            if (duration() && dots()) {
                return true;
            }
            return false;
        }
        return false;
    }

    /**
     * Rest processes the rest and its duration. Like notes, rests can have dots.
     *
     * @return true if proper syntax
     */
    private boolean rest() {
        scanToken();
        out.print('_'); file += "_";
        duration();
        dots();
        return true;
    }

    /**
     * Accidentals processes whether or not the note is sharp or flat, as indicated by a # for sharp
     * and & for flat. GUIDO specification technically allows for any order and mixture of sharps/flats,
     * although this doesn't make sense musically.
     *
     * @return a String with all the accidentals, if any
     */
    private String accidentals() {
        Token tok = scanToken();
        String accidentals = "";
        while (tok.getId() == Id.FLAT) {
            accidentals += '&';
            tok = scanToken();
        }
        while (tok.getId() == Id.SHARP) {
            accidentals += '#';
            tok = scanToken();
        }
        putback(tok); //Put back token if not a sharp or flat
        return accidentals;
    }

    /**
     * Octave processes the octave of a note (e.g. A4 vs. A-1). Octave is an integer (-2,-1,0,1,2,3, etc.) and
     * immediately follows the letter name and accidental(s). Following notes will be at the same octave as
     * previous notes unless specified.
     *
     * @return true if proper syntax
     */
    private boolean octave() {
        Token tok = scanToken();
        if (tok.getId() == Id.HYPHEN) {
            if(peak().getId() == Id.INTEGER) {
                tok = scanToken();
                Note.setStaticOctave(-1 * tok.getInt());
                return true;
            }
            return false;
        } else if (tok.getId() == Id.INTEGER) {
            Note.setStaticOctave(tok.getInt());
            return true;
        }
        putback(tok);
        return true;
    }

    /**
     * Duration processes the duration of a note. Duration follows the octave (if specified) and can be written
     * in a few ways: (n is an integer) /n, *n, *n/n. Following notes will be at the same duration as
     * previous notes unless specified.
     * Number values are processed by Number().
     *
     * @return true if proper syntax
     */
    private boolean duration() {
        Token tok = scanToken();
        if (tok.getId() == Id.SLASH) {
            out.print('/'); file += "/";
            return number();
        } else if (tok.getId() == Id.ASTERISK) {
            out.print('*'); file += "*";
            if(number()) {
                tok = scanToken();
                if (tok.getId() == Id.SLASH) {
                    out.print('/'); file += "/";
                    return number();
                }
                putback(tok);
                return true;
            }
            return false;
        }
        putback(tok);
        return true;
    }

    /**
     * Number processes the values of durations. Numbers must be integers and can be greater than 10.
     *
     * @return true if proper syntax
     */
    private boolean number() {
        int n;
        Token tok = scanToken();
        if (tok.getId() == Id.INTEGER) {
            n = tok.getInt();
            while (peak().getId() == Id.INTEGER) {
                tok = scanToken();
                n = (n * 10) + tok.getInt();
            }
            out.print(n); file += ("" + n);
            return true;
        }
        putback(tok);
        return false;
    }

    /**
     * Dots processes any dots following duration. Dots are not required.
     *
     * @return true if proper syntax
     */
    private boolean dots() {
        Token tok = scanToken();
        while (tok.getId() == Id.DOT) {
            out.print('.'); file += ".";
            tok = scanToken();
        }
        putback(tok);
        return true;
    }

    /**
     * Chords are processed as a series of notes being played at once. They are started with an open brace {
     * and ended with a closed brace }. NotesList() is called to handle the series of notes.
     *
     * @return true if proper syntax
     */
    private boolean chord() {
        if(expect(Id.OPEN_CPAREN)) {
            out.print('{'); file += "{";
            if (notesList()
                    && expect(Id.CLOSED_CPAREN)) {
                out.print('}'); file += "}";
                return true;
            }
        }
        return false;
    }

    /**
     * NotesList processes a series of notes separated by commas. NotesList is recursive if more than one note
     * is detected.
     *
     * @return true if proper syntax
     */
    private boolean notesList() {
        if (note()) {
            Token tok = scanToken();
            if (tok.getId() == Id.COMMA) {
                out.print(','); file += ",";
                return notesList();
            }
            putback(tok);
            return true;
        }
        return false;
    }

    /**
     * Tag processes a command, as indicated by a back slash \. A Tag must have a tagname, but parameters are
     * not required. If a \key command is detected, the key is set by calling parameterGetKey();
     *
     * @return true if proper syntax
     */
    private boolean tag() {
        Token tok = scanToken();
        out.print('\\'); file += "\\";
        if (peak().getId() == Id.TAGNAME) {
            tok = scanToken();
            out.print(tok.getString()); file += tok.getString();
            if (tok.getString().equals("key")) {
                return parameterGetKey();
            }
            return parameter();
        }
        return false;
    }

    /**
     * ParameterGetKey detects the key as indicated within the \key command. Parses similarly to parameter(),
     * but the \key command must have a parameter, unlike other commands.
     *
     * @return true if proper syntax
     */
    private boolean parameterGetKey() {
        Token tok = scanToken();
        if (tok.getId() == Id.OPEN_ANGLE
                && expect(Id.QUOTE_MARK)) {
            out.print("<\""); file += "<\"";
            if(peak().getId() == Id.STRING) {
                tok = scanToken();
                Note.setKey(tok.getString()); //Sets the key within the Note class
                out.print(Note.getKey()); file += Note.getKey();
                if (expect(Id.QUOTE_MARK)
                        && expect(Id.CLOSED_ANGLE)) {
                    out.print("\">"); file += "\">";
                    return true;
                }
                return false;
            }
            return false;
        }
        putback(tok);
        return false;
    }

    /**
     * Parameter parses the parameter, if there is one. A parameter for a command is not required.
     * If an open angle bracket and quotation mark are detected, however, a String, closing quotation mark,
     * and angle bracket must follow: <"String">
     *
     * @return true if proper syntax
     */
    private boolean parameter() {
        Token tok = scanToken();
        if (tok.getId() == Id.OPEN_ANGLE
                && expect(Id.QUOTE_MARK)) {
            out.print("<\""); file += "<\"";
            if(peak().getId() == Id.STRING) {
                tok = scanToken();
                out.print(tok.getString()); file += tok.getString();
                if (expect(Id.QUOTE_MARK)
                        && expect(Id.CLOSED_ANGLE)) {
                    out.print("\">"); file += "\">";
                    return true;
                }
                return false;
            }
            return false;
        }
        putback(tok);
        return false;
    }
}
