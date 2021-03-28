/**
Enum class for all token types to categorize characters used in the GUIDO language
 */
public enum Id {
    OPEN_CPAREN, // { beginning of the file or beginning of a chord
    CLOSED_CPAREN, // } end of the file or end of a chord
    OPEN_BRACKET, // [ start of a new voice
    CLOSED_BRACKET, // ] end of a new voice
    OPEN_ANGLE, // < indicates following text contains the parameters of a command
    CLOSED_ANGLE, // > closes parameters
    COMMA, // , separated notes and commands
    HYPHEN, // - negative sign
    SLASH, // / division sign used for note value
    SHARP, // # indicates the note is sharp
    FLAT, // & indicates the note is flat
    DOT, // . indicates a dotted note, increases note value by 1/2 current value
    ASTERISK, // * multiplication sign used for note value
    REST, // _ indicates a rest
    BACK_SLASH, // \ indicates the start of a command
    QUOTE_MARK, // " start of a String
    INTEGER, // integer value for note values or commands
    CHARACTER, // usually a letter name of a note
    STRING, // contents of a String, e.g. a parameter
    TAGNAME, // the name of the command being called
    STOP, // -1 end of the file
    NULL_TOKEN // no token found
}
