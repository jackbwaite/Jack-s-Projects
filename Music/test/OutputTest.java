import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class OutputTest {

    @org.junit.Test
    public void print() {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        Output out = new Output(os);
        out.print("hello, world!");
        try {
            Assertions.assertEquals("hello, world!", os.toString("UTF8"));
        } catch (UnsupportedEncodingException e) {
            System.err.println("Should never occur");
        }
    }
}