import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class GameBoardTest {
    private GameBoard gameBoard;

    @BeforeEach
    public void setUp() {
        // Inicializáljuk a GameBoard példányt az egységtesztek előtt
        gameBoard = new GameBoard(3, 0, 0, 'N'); // Példaként méret: 3x3, kezdő pozíció: (0, 0), irány: Észak
    }

    @Test
    void testGetters() {
        assertEquals(3, gameBoard.getSize());
        assertEquals(0, gameBoard.getPlayerRow());
        assertEquals(0, gameBoard.getPlayerCol());
        assertEquals('N', gameBoard.getPlayerDirection());
        assertEquals(0, gameBoard.getArrows());
    }

    @Test
    void testSetPlayerPosition() {
        gameBoard.setPlayerPosition(1, 1, 'S');
        assertEquals(1, gameBoard.getPlayerRow());
        assertEquals(1, gameBoard.getPlayerCol());
        assertEquals('S', gameBoard.getPlayerDirection());
    }

    @Test
    void testGetCell() {
        gameBoard.setCell(1, 1, 'W');
        assertEquals('W', gameBoard.getCell(1, 1));
    }

    @Test
    void testSetRow() {
        gameBoard.setRow(0, "WWU");
        assertEquals('W', gameBoard.getCell(0, 0));
        assertEquals('W', gameBoard.getCell(0, 1));
        assertEquals('U', gameBoard.getCell(0, 2));
        assertEquals(1, gameBoard.getArrows()); // Ellenőrizzük az nyílak számát
    }

    @Test
    void testSetRowInvalidRowIndex() {
        // Teszteljük az invalid sorindexet (negatív érték)
        gameBoard.setRow(-1, "WUU");
        assertEquals(0, gameBoard.getArrows()); // Nem változtatja meg a nyílak számát
    }

    @Test
    void testGetBoardNotNull() {
        // Teszteljük, hogy a getBoard() nem ad vissza null értéket
        assertNotNull(gameBoard.getBoard());
    }
}
