import org.junit.Assert;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

public class ParserTest {

    @Test
    public void parseScore() {
        Input in = new Input(getClass().getResourceAsStream("/full_simple_notes.gmn"));
        Parser parser = new Parser(in);
        boolean b = parser.score();
        Assert.assertEquals(true, b);
    }

    @Test
    public void parseOneVoice() {
        Input in = new Input(getClass().getResourceAsStream("/one_voice_only_notes.gmn"));
        Parser parser = new Parser(in);
        boolean b = parser.score();
        Assert.assertEquals(true, b);
    }

    @Test
    public void parse16thNote() {
        Input in = new Input(getClass().getResourceAsStream("/16th_note.gmn"));
        Parser parser = new Parser(in);
        boolean b = parser.score();
        Assert.assertEquals(true, b);
    }

    @Test
    public void parseTripleFlat() {
        Input in = new Input(getClass().getResourceAsStream("/tripleFlat.gmn"));
        Parser parser = new Parser(in);
        boolean b = parser.score();
        Assert.assertEquals(true, b);
    }

    @Test
    public void parseThreeEmptyVoices() {
        Input in = new Input(getClass().getResourceAsStream("/empty_voices.gmn"));
        Parser parser = new Parser(in);
        boolean b = parser.score();
        Assert.assertEquals(true, b);
    }

    @Test
    public void expect() {
        Input in = new Input(new ByteArrayInputStream("<".getBytes(StandardCharsets.UTF_8)));
        Parser parse = new Parser(in);
        boolean b = parse.expect(Id.OPEN_ANGLE);
        Assert.assertEquals(true, b);
    }

    @Test
    public void peak() {
        Input in = new Input(new ByteArrayInputStream("<>".getBytes(StandardCharsets.UTF_8)));
        Parser parse = new Parser(in);
        Token b = parse.peak();
        Assert.assertEquals(Id.OPEN_ANGLE, b.getId());
    }
}
