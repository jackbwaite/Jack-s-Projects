import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.IOException;

/**
 * The {@code Input} class provides a single method ({@link #getChar()}) to read characters from an input.
 * <p>
 * To read characters from an {@code InputStream} (e.g., {@code System.in}), instantiate an {@code Input} object as
 * follows:
 * <pre>
 * Input in = new Input(inputStream);</pre>
 * To read characters from a file, instantiate an {@code Input} object as follows:
 * <pre>
 * Input in = new Input(filename);</pre>
 * Once instantiated, a function can read the next character as follows:
 * <pre>
 * in.getChar();</pre>
 * If the file does not exist or an error occurs while reading, this class prints an error message and exit with status 1.
 */
public class Input {

    private BufferedInputStream bin;
    private String filename;

    /**
     * Construct an {@code Input} object from an {@code InputStream}.
     * @param in  an {@code InputStream}
     */
    public Input(InputStream in) {
        this.bin = new BufferedInputStream(in);
        this.filename = "(in)";
    }

    /**
     * Construct an {@code Input} object from a filename.
     * @param filename  the name of a file (it must exist)
     */
    public Input(String filename) {
        this.filename = filename;
        FileInputStream fin;
        try {
            fin = new FileInputStream(this.filename);
            this.bin = new BufferedInputStream(fin);
        }
        catch (FileNotFoundException e) {
            fatal("No such file");
        }
    }

    /**
     * Get the next character from the {@code Input}.
     *
     * This function returns -1 when the end of file is encountered.
     *
     * @return the next character from the {@code Input}
     */
    public int getChar() {
        try {
            return bin.read();
        } catch (IOException e) {
            fatal("Read error");
        }
        return 0;
    }

    public void fatal(String msg) {
        System.err.println(filename + ": " + msg);
        System.exit(1);
    }

    public static void main(String[] args) {
        Input in = new Input(System.in);
        in.fatal("Hello world!");
    }

}