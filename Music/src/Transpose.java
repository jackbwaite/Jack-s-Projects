import java.util.Scanner;

/**
 * Transpose class is the interface for transposing sheet music using file names.
 */
public class Transpose {

    /**
     * Primary transposition method. Use this to transpose file1 a num of half steps into file2.
     * @param num number of half steps to transpose; can be negative
     * @param file1 GUIDO sheet music file to transpose
     * @param file2 empty file to receive transposed sheet music
     */
    static public void transpose(int num, String file1, String file2) {

        Note.setNumHalfSteps(num);
        String filename = file1;
        String otherFilename = file2;

        Parser parser = new Parser(filename, otherFilename);
        parser.printScore();
    }

    static public void transpose(int num, String file) {
        Note.setNumHalfSteps(num);
        Parser parser = new Parser(file);
        parser.printScore();
    }

    public static void main(String[] unused) {
        System.out.println("Enter the number of half steps:");
        Scanner sc = new Scanner(System.in);
        int numHalfSteps = sc.nextInt();
        Note.setNumHalfSteps(numHalfSteps);

        System.out.println("Enter a filename containing GUIDO music:");
        String filename = sc.next();
        System.out.println("Enter a filename for the output:");
        String otherFilename = sc.next();

        Parser par = new Parser(filename, otherFilename);
        par.printScore();
    }

    static void usage() {
        System.err.println("Usage: transpose NUM-HALF-STEPS MUSIC-FILE");
        System.err.println("This tool transposes a music partition written in the GUIDO format.");
        System.err.println("NUM-HALF-STEPS specifies the transposition value.");
        System.err.println("MUSIC-FILE is the path to the music piece to be transposed.");
    }
}
