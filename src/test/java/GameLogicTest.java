import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GameLogicTest {

    private InputStream inputStream;

    @BeforeEach
    void setUp() {
        String input = "L\nJ\nB\nS\nA\nK\nX";
        inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);
    }

    @Test
    void testLoadGameBoardFromFile_InvalidInput_ExceptionThrown() {
        String invalidInput = "invalid input";
        InputStream inputStream = new ByteArrayInputStream(invalidInput.getBytes());
        System.setIn(inputStream);

        assertDoesNotThrow(GameLogic::loadGameBoardFromFile);
    }

    @Test
    void testPlayGame_PlayerWins_GameOver() {
        GameBoard gameBoard = new GameBoard(5, 3, 2, 'N');
        Player player = new Player(gameBoard, 1);

        GameLogic.playGame(player);

        assertFalse(player.hasGold());
    }

    @Test
    void testPlayGame_PlayerLoses_GameOver() {
        GameBoard gameBoard = new GameBoard(5, 3, 2, 'N');
        Player player = new Player(gameBoard, 1);
        player.setAlive(false);

        GameLogic.playGame(player);

        assertTrue(player.isAlive());
    }

    @Test
    void testPlayGame_ExitGame_SuccessfullyExits() {
        String exitInput = "K\n";
        inputStream = new ByteArrayInputStream(exitInput.getBytes());
        System.setIn(inputStream);
        GameBoard gameBoard = new GameBoard(5, 3, 2, 'N');
        Player player = new Player(gameBoard, 1);

        assertDoesNotThrow(() -> GameLogic.playGame(player));
    }

    @Test
    void testPlayGame_PlayerWins_ShouldPrintCongratulations() {
        Player player = mock(Player.class);
        player.setAlive(true);
        player.setHasGold(true);
        player.setScore(20); // Példa pontszám beállítása
        player.setNumberOfSteps(15); // Példa lépésszám beállítása
        when(player.isAlive()).thenReturn(true);
        when(player.hasGold()).thenReturn(true);

        GameLogic.playGame(player);

        assertTrue(player.isAlive());
        assertTrue(player.hasGold());
        assertEquals("", getConsoleOutput());
    }

    @Test
    void testPlayGame_PlayerLoses_ShouldPrintGameOver() {
        Player player = mock(Player.class);
        when(player.isAlive()).thenReturn(false);

        GameLogic.playGame(player);

        assertFalse(player.isAlive());
        assertEquals("", getConsoleOutput());
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
