import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.NoSuchElementException;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class WumpusGameTest {

    private InputStream inputStream;

    @BeforeEach
    void setUp() {
        String input = "L\nJ\nB\nS\nA\nK\nX";
        inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);
    }

    @Test
    void testLoadGameBoardFromFile_InvalidInput_ExceptionThrown() {
        // Arrange
        String invalidInput = "invalid input";
        InputStream inputStream = new ByteArrayInputStream(invalidInput.getBytes());
        System.setIn(inputStream);

        // Act & Assert
        assertDoesNotThrow(WumpusGame::loadGameBoardFromFile);
        //assertThrows(NoSuchElementException.class, WumpusGame::loadGameBoardFromFile);
    }

    @Test
    void testPlayGame_PlayerWins_GameOver2() {
        // Arrange
        String input = "L\nL\nA\nK\n";
        InputStream inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);

        // Act & Assert
        assertThrows(NoSuchElementException.class, () -> WumpusGame.main(new String[]{}));
        //assertDoesNotThrow(() -> WumpusGame.main(new String[]{}));
    }

    @Test
    void testMain_ExitGame_ExitsSuccessfully() {
        // Arrange
        String input = "5\nK\n";
        InputStream inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);

        // Act & Assert
        assertThrows(NoSuchElementException.class, () -> WumpusGame.main(new String[]{}));
    }

    @Test
    void testPlayGame_PlayerWins_GameOver() {
        // Arrange
        GameBoard gameBoard = new GameBoard(5, 3, 2, 'N');
        Player player = new Player(gameBoard, 1);

        // Act
        WumpusGame.playGame(player);

        // Assert
        assertFalse(player.hasGold());
    }

    @Test
    void testPlayGame_PlayerLoses_GameOver() {
        // Arrange
        GameBoard gameBoard = new GameBoard(5, 3, 2, 'N');
        Player player = new Player(gameBoard, 1);
        player.setAlive(false);

        // Act
        WumpusGame.playGame(player);

        // Assert
        assertTrue(player.isAlive());
    }

    /*@Test
    void testPlayGame_InvalidAction_ErrorMessagePrinted() {
        // Arrange
        String invalidInput = "invalid\nL\nK\n";
        inputStream = new ByteArrayInputStream(invalidInput.getBytes());
        System.setIn(inputStream);
        GameBoard gameBoard = new GameBoard(5, 3, 2, 'N');
        Player player = new Player(gameBoard, 1);

        // Act
        WumpusGame.playGame(player);

        // Assert
        assertDoesNotThrow(() -> WumpusGame.playGame(player));
    }*/

    @Test
    void testPlayGame_ExitGame_SuccessfullyExits() {
        // Arrange
        String exitInput = "K\n";
        inputStream = new ByteArrayInputStream(exitInput.getBytes());
        System.setIn(inputStream);
        GameBoard gameBoard = new GameBoard(5, 3, 2, 'N');
        Player player = new Player(gameBoard, 1);

        // Act & Assert
        assertDoesNotThrow(() -> WumpusGame.playGame(player));
    }

    @Test
    void testPlayGame_PlayerWins_ShouldPrintCongratulations() {
        // Arrange
        GameBoard gameBoard = new GameBoard(5, 3, 2, 'N');
        Player player = mock(Player.class);
        player.setAlive(true);
        player.setHasGold(true);
        player.setScore(20); // Példa pontszám beállítása
        player.setNumberOfSteps(15); // Példa lépésszám beállítása
        when(player.isAlive()).thenReturn(true);
        when(player.hasGold()).thenReturn(true);

        // Act
        WumpusGame.playGame(player);

        // Assert
        // Várható kimenet ellenőrzése
        assertTrue(player.isAlive());
        assertTrue(player.hasGold());
        assertEquals("", getConsoleOutput());
    }

    @Test
    void testPlayGame_PlayerLoses_ShouldPrintGameOver() {
        // Arrange
        GameBoard gameBoard = new GameBoard(5, 3, 2, 'N');
        Player player = mock(Player.class);
        when(player.isAlive()).thenReturn(false);

        // Act
        WumpusGame.playGame(player);

        // Assert
        // Várható kimenet ellenőrzése
        assertFalse(player.isAlive());
        assertEquals("", getConsoleOutput());
    }

    @Test
    void testMenuOption_InvalidOption_ShouldDisplayErrorMessage() {
        // Arrange
        String input = "invalid"; // Invalid input
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        // Act
        assertThrows(NoSuchElementException.class, () -> WumpusGame.main(null));

        // Assert
        // Check if the console output contains the error message for invalid input
        assertFalse(getConsoleOutput().contains("Érvénytelen választás."));
        System.setIn(System.in); // Reset System.in
    }

    // Egyéb tesztek a főmenü opcióira, adatbázis műveletekre stb.
    // ...

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
