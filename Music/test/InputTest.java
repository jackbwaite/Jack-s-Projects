import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class InputTest {

    @Test
    public void getChar() {
        Input in = new Input(new ByteArrayInputStream("hello world!".getBytes(StandardCharsets.UTF_8)));
        Assertions.assertEquals('h', in.getChar());
        Assertions.assertEquals('e', in.getChar());
        Assertions.assertEquals('l', in.getChar());
        Assertions.assertEquals('l', in.getChar());
        Assertions.assertEquals('o', in.getChar());
        Assertions.assertEquals(' ', in.getChar());
        Assertions.assertEquals('w', in.getChar());
        Assertions.assertEquals('o', in.getChar());
        Assertions.assertEquals('r', in.getChar());
        Assertions.assertEquals('l', in.getChar());
        Assertions.assertEquals('d', in.getChar());
        Assertions.assertEquals('!', in.getChar());
        Assertions.assertEquals(-1, in.getChar());
    }

}