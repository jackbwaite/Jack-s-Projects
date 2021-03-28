import org.junit.Assert;
import org.junit.Test;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class ScannerTest {

    @Test
    public void getToken_ReturnsTokenWithCorrectId() {
        Input in = new Input(new ByteArrayInputStream("{".getBytes(StandardCharsets.UTF_8)));
        Scanner scan = new Scanner(in);
        Token t = new Token(scan.getNextToken());
        Token tok = new Token(Id.OPEN_CPAREN);
        Assert.assertEquals(tok.getId(), t.getId());
    }

    @Test
    public void getToken_CanReturnLettersAsToken() {
        Input in = new Input(new ByteArrayInputStream("a".getBytes(StandardCharsets.UTF_8)));
        Scanner scan = new Scanner(in);
        Token t = new Token(scan.getNextToken());
        Token tok = new Token('a');
        Assert.assertEquals(tok.getId(), t.getId());
        Assert.assertEquals(tok.getChar(), t.getChar());
    }

    @Test
    public void getToken_CanReturnNumbersAsToken() {
        Input in = new Input(new ByteArrayInputStream("0".getBytes(StandardCharsets.UTF_8)));
        Scanner scan = new Scanner(in);
        Token t = new Token(scan.getNextToken());
        Token tok = new Token(0);
        Assert.assertEquals(tok.getId(), t.getId());
        Assert.assertEquals(tok.getInt(), t.getInt());
    }

    @Test
    public void getString_CanReturnStringAsToken() {
        Input in = new Input(new ByteArrayInputStream("\"Hello World!\"".getBytes(StandardCharsets.UTF_8)));
        Scanner scan = new Scanner(in);
        scan.getNextToken(); //Pass the quote
        Token str = new Token(scan.getNextToken());
        Token tok = new Token("Hello World!");
        Assert.assertEquals(tok.getId(), str.getId());
        Assert.assertEquals(tok.getString(), str.getString());
    }

    @Test
    public void scanSimpleMozart() {
        Input in = new Input(getClass().getResourceAsStream("/full_simple_notes.gmn"));
        Scanner scan = new Scanner(in);
        ArrayList<Token> tokens = new ArrayList<>();
        while(scan.hasNext()) {
            Token tok = scan.getNextToken();
            tokens.add(tok);
        }
        for (Token t : tokens) {
            System.out.println(t.getId());
        }
    }

    @Test
    public void scanOneVoiceScore() {
        Input in = new Input(getClass().getResourceAsStream("/one_voice_only_notes.gmn"));
        Scanner scan = new Scanner(in);
        ArrayList<Token> tokens = new ArrayList<>();
        while(scan.hasNext()) {
            Token tok = scan.getNextToken();
            tokens.add(tok);
        }
        for (Token t : tokens) {
            System.out.println(t.getId());
        }
    }

    @Test
    public void peakAtNextToken() {
        Input in = new Input(new ByteArrayInputStream("<>".getBytes(StandardCharsets.UTF_8)));
        Scanner scan = new Scanner(in);
        Token a = scan.getNextToken();
        Token b = scan.peak();
        Token expA = new Token(Id.OPEN_ANGLE, '<');
        Token expB = new Token(Id.CLOSED_ANGLE, '>');
        Assert.assertEquals(expA.getId(), a.getId());
        Assert.assertEquals(expB.getId(), b.getId());
    }

    @Test
    public void testPutbackMethod_testMultipleTokens_testHasNext() {
        Input in = new Input(new ByteArrayInputStream("a&&#}{][,.-*/\\\"<>".getBytes(StandardCharsets.UTF_8)));
        Scanner scan = new Scanner(in);
        ArrayList<Token> tokenList = new ArrayList<Token>();
        while(scan.hasNext()) {
            tokenList.add(scan.getNextToken());
        }
        System.out.println(tokenList);

        scan.putback(tokenList.get(tokenList.size() - 1));
        Token test = scan.getNextToken();
        Assert.assertEquals((new Token(Id.CLOSED_ANGLE)).getId(), test.getId());

        Token tok = new Token(Id.REST);
        scan.putback(tok);
        Token test2 = scan.getNextToken();
        Assert.assertEquals((new Token(Id.REST)).getId(), test2.getId());
    }
}
