import org.junit.Test;
import org.junit.jupiter.api.Assertions;

public class TransposeTester {

    @Test
    public void testNote() {
        Note.setNumHalfSteps(3);
        Note note = new Note("a");
        note.transpose();
        Assertions.assertEquals(note.getNote(), "c");
    }

    @Test
    public void testOneVoice() {
        Parser p = new Parser(new Input(getClass().getResourceAsStream("/correctly_transposed_2steps.gmn")));
        p.printScore();

        Note.setNumHalfSteps(2);

        Parser p2 = new Parser(new Input(getClass().getResourceAsStream("/one_voice_only_notes.gmn")));
        p2.printScore();

        Assertions.assertEquals(p.getFile(), p2.getFile());
    }

    @Test
    public void testMozart() {
        Note.setNumHalfSteps(0);

        Parser p = new Parser(new Input(getClass().getResourceAsStream("/correctly_transposed_mozart_-4steps.gmn")));
        p.printScore();

        Note.setNumHalfSteps(-4);

        Parser p2 = new Parser(new Input(getClass().getResourceAsStream("/full_simple_notes.gmn")));
        p2.printScore();

        Assertions.assertEquals(p2.getFile(), p.getFile());
    }

    /**
     * Note that this test method will fill a desired file with the transpose sheet music, so you'll have
     * to fill in your own file path.
     */
    @Test
    public void fillScore() {
        Note.setNumHalfSteps(2);

        Parser p = new Parser("C:\\Users\\jackb\\IdeaProjects\\Music\\resources\\bach.gmn",
                "C:\\Users\\jackb\\IdeaProjects\\Music\\resources\\fill_with_transposed_mozart_-4steps.gmn");
        p.printScore();
    }
}
