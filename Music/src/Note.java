import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * The Note class serves to create Note objects when a note is encountered in the GUIDO file.
 */
public class Note {

    private static int numHalfSteps = 0; //Set by the Transpose Class or Test classes
    private static String key = "C";
    private static int staticOctave = 0; // staticOctave is for retaining the octave of a note that defines future notes' octaves

    private String noteName;
    private int octave;
    private String duration;
    private int numberOfDots = 0;

    /**
     * Constructors
     */

    /**
     * Construct a Note using the name, i.e. the pitch
     * @param name letter name/tone of the note
     */
    public Note(String name) {
        this.noteName = name;
        this.octave = staticOctave;
    }

    /**
     * Static Setters
     */

    /**
     * setNumHalfSteps sets the number of half steps to change the note by when transposing.
     * numHalfSteps is static so it can be applied to all notes in a file.
     * @param num the number of half steps
     */
    static void setNumHalfSteps(int num) {
        numHalfSteps = num;
    }

    /**
     * Sets the static key variable of the Note class to the detected key (called by Parser).
     * Transposes to the new key using numHalfSteps. New key will always prefer D& over C# and G& over F#.
     * @param k the String within the parameter for the \key command
     */
    static void setKey(String k) {
        String[] keys =        {"A", "B&", "B", "C", "D&", "D", "E&", "E", "F", "G&", "G", "A&"};
        String[] enharmonics = {"A", "B&", "B", "C", "C#", "D", "E&", "E", "F", "F#", "G", "A&"};
        int index = 0;
        for (int i = 0; i < 12; i++) {
            if (k.equals(keys[i]) || k.equals(enharmonics[i])) {
                index = i;
            }
        }
        if (numHalfSteps != 0) { //Change the key by a number of half steps; doesn't change if numHalfSteps = 0.
            index += numHalfSteps;
            if (index > 11) { //Cycle back through if index is out of range
                index -= 12;
            }
            if (index < 0) {
                index += 12;
            }
        }
        key = keys[index];
    }

    /**
     * Set the octave of the Note; stays the same unless changed by future notes
     * @param x the octave of the Note
     */
    static void setStaticOctave(int x) {
        staticOctave = x;
    }

    /**
     * Non-static Setters
     */

    /**
     * Change the value of the current Note
     * @param newNote value of the new note
     */
    void changeNote(String newNote) {
        this.noteName = newNote;
    }

    /**
     * Change the value of the Octave for this Note
     * @param octave new octave
     */
    void changeOctave(int octave) {
        this.octave = octave;
    }

    /**
     * Set the duration of this Note; not used for transposition
     * @param duration duration of note
     */
    void setDuration(String duration) {
        this.duration = duration;
    }

    /**
     * Set the number of dots on this Note; not used for transposition
     * @param dots number of dots
     */
    void setDots(int dots) {
        this.numberOfDots += dots;
    }

    /**
     * Static Getters
     */

    /**
     * @return numHalfSteps
     */
    static int getNumHalfSteps() {
        return numHalfSteps;
    }

    /**
     * @return key
     */
    static String getKey() {
        return key;
    }

    /**
     * @return staticOctave
     */
    static int getStaticOctave() {
        return staticOctave;
    }

    /**
     * Non-static Getters
     */

    /**
     * @return noteName
     */
    String getNote() {
        return noteName;
    }

    /**
     * @return octave
     */
    int getOctave() {
        return octave;
    }

    /**
     * @return duration
     */
    String getDuration() {
        return duration;
    }

    /**
     * @return numberOfDots
     */
    int getDots() {
        return numberOfDots;
    }

    /**
     * Transpose this Note by the given number of half steps by changing the noteName.
     */
    void transpose() {
        //The index will be the position of the new noteName in sharpChromatics[] or flatChromatics[].
        //Index initialized at zero and intended to be set to the current noteName. (This is potentially weak code.)
        int index = 0;

        // 1. Check for double-sharps and double-flats. Convert them into naturals for transposition.
        String[] doubleSharps = {"a##", "c##", "d##", "f##", "g##"};
        String[] doubleFlats = {"a&&", "b&&", "d&&", "e&&", "g&&"};
        for (int i = 0; i < 5; i++) {
            if (noteName.equals(doubleSharps[i])) {
                char s = noteName.charAt(0);
                s = (char) ((int) s + 1);
                if (s == 'h') {
                    s = 'a';
                }
                this.changeNote(s + "");
            }
            if (noteName.equals(doubleFlats[i])) {
                char s = noteName.charAt(0);
                s = (char) ((int) s - 1);
                if (s == 96) {
                    s = 'g';
                }
                this.changeNote(s + "");
            }
        }

        // 2. Check for an odd sharp or flat note, change it to a natural, and set the index.
        // An "odd" flat or sharp is one that would normally be written as a natural.
        if (noteName.equals("e#")) {
            this.changeNote("f");
            index = 5;
        }
        else if (noteName.equals("b#")) {
            this.changeNote("c");
            index = 0;
        }
        else if (noteName.equals("c&")) {
            this.changeNote("b");
            index = 11;
        }
        else if (noteName.equals("f&")) {
            this.changeNote("e");
            index = 4;
        }

        // 3. Check for an odd double-sharp or double-flat, change it to a natural, and set the index.
        if (noteName.equals("e##")) {
            this.changeNote("f#");
            index = 6;
        }
        else if (noteName.equals("b##")) {
            this.changeNote("c#");
            index = 1;
        }
        else if (noteName.equals("c&&")) {
            this.changeNote("b&");
            index = 10;
        }
        else if (noteName.equals("f&&")) {
            this.changeNote("e&");
            index = 3;
        }

        // 4. Set index to the current noteName in sharpChromatics[] or flatChromatics[]
        String[] sharpChromatics = {"c", "c#", "d", "d#", "e", "f", "f#", "g", "g#", "a", "a#", "b"};
        String[] flatChromatics = {"c", "d&", "d", "e&", "e", "f", "g&", "g", "a&", "a", "b&", "b"};
        for (int i = 0; i < 12; i++) {
            if (noteName.equals(sharpChromatics[i])) {
                index = i;
            } if (noteName.equals(flatChromatics[i])) {
                index = i;
            }
        }

        // 5. Shift the index by the number of half-steps. Shift back 12 if index is overshot, forward 12 if undershot.
        this.octave = staticOctave;
        index += numHalfSteps;
        if (index > 11) {
            index -= 12;
            this.octave += 1;
        }
        if (index < 0) {
            index += 12;
            this.octave -= 1;
        }

        // 6. Use the new key to determine which chromatic scale to use (sharps or flats). Note that C is sharp by default.
        String[] sharpKeys = {"A", "B", "C", "D", "E", "G"};
        String[] flatKeys = {"B&", "D&", "E&", "F", "G&", "A&"};
        boolean useSharpOrNonSharp = false;
        boolean useFlats = false;
        for (int i = 0; i < 6; i++) {
            if (this.key.equals(sharpKeys[i])) {
                useSharpOrNonSharp = true;
            }
            if (this.key.equals(flatKeys[i])) {
                useFlats = true;
            }
        }

        // 7. Execute transposition
        if (useFlats) { this.changeNote(flatChromatics[index]); }
        if (useSharpOrNonSharp) { this.changeNote(sharpChromatics[index]); }
    }

    @Override
    public String toString() {
        String s = "NOTE: " + this.noteName;
        s += "\nOCTAVE: " + this.octave;
        s += "\nDURATION: " + this.duration;
        s += "\nDOTS: " + this.numberOfDots;
        return s;
    }
}
