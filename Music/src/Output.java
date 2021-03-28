import java.io.*;

/**
 * The Output class provides different ways of printing or saving the transposed GUIDO file.
 * Uses a PrintStream variable to print Strings, characters, and integers, or to place a transposed
 * file into a new file.
 */
public class Output {

    private PrintStream out;

    //Constructors
    public Output() {
        this(System.out);
    }

    public Output(PrintStream out) {
        this.out = out;
    }

    public Output(ByteArrayOutputStream os) {
        this.out = new PrintStream(os);
    }

    /**
     * Makes a second file the Output, so it will receive the text of the transposed file
     * @param filename the file to be filled with a transposed file
     */
    public Output(String filename) {
        try {
            this.out = new PrintStream(new File(filename));
        }
        catch (FileNotFoundException e) {
            System.err.print("No such file");
        }
    }

    //Methods
    public void print(String s) {
        out.print(s);
    }

    public void print(char c) {
        out.print(c);
    }

    public void print(int n) {
        out.print(n);
    }

    public static void main(String[] args) {
        Output o = new Output(System.out);
        o.print("Hello world\n");
    }
}