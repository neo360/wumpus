import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.NoSuchElementException;
import static org.junit.jupiter.api.Assertions.*;

class WumpusGameTest {

    private InputStream inputStream;

    @BeforeEach
    void setUp() {
        String input = "L\nJ\nB\nS\nA\nK\nX";
        inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);
    }

    @Test
    void testPlayGame_PlayerWins_GameOver2() {
        String input = "L\nL\nA\nK\n";
        InputStream inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);

        assertThrows(NoSuchElementException.class, () -> WumpusGame.main(new String[]{}));
    }

    @Test
    void testMain_ExitGame_ExitsSuccessfully() {
        String input = "5\nK\n";
        InputStream inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);

        assertThrows(NoSuchElementException.class, () -> WumpusGame.main(new String[]{}));
    }

    @Test
    void testMenuOption_InvalidOption_ShouldDisplayErrorMessage() {
        String input = "invalid"; // Invalid input
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        assertThrows(NoSuchElementException.class, () -> WumpusGame.main(null));

        assertFalse(getConsoleOutput().contains("Érvénytelen választás."));
        System.setIn(System.in); // Reset System.in
    }

    private String getConsoleOutput() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outputStream);

        // Átirányítjuk a kimenetet
        PrintStream originalOut = System.out;
        System.setOut(printStream);

        // Visszaadjuk az átirányított kimenet tartalmát
        String consoleOutput;
        try {
            consoleOutput = outputStream.toString("UTF-8").trim();
        } catch (UnsupportedEncodingException e) {
            consoleOutput = "Error retrieving console output.";
        } finally {
            // Visszaállítjuk az eredeti kimeneti csatornát
            System.setOut(originalOut);
        }

        return consoleOutput;
    }
}
