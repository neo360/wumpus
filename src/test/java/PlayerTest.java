import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

class PlayerTest {

    private GameBoard gameBoardMock;
    private Player player;
    private final PrintStream standardOut = System.out;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

    @BeforeEach
    public void setUp() {
        gameBoardMock = mock(GameBoard.class);
        player = new Player(gameBoardMock, 5); // 5 nyíllal inicializáljuk a játékost
    }

    @Test
    void testSetArrows() {
        player.setArrows(8);
        assertEquals(8, player.getArrows());
    }

    @Test
    void testSetScore() {
        player.setScore(10);
        assertEquals(10, player.getScore());
    }

    @Test
    void testMoveSuccessful() {
        // Teszteljük a sikeres mozgást
        when(gameBoardMock.getPlayerRow()).thenReturn(1);
        when(gameBoardMock.getPlayerCol()).thenReturn(1);
        when(gameBoardMock.getPlayerDirection()).thenReturn('N');
        when(gameBoardMock.getSize()).thenReturn(5);
        when(gameBoardMock.getCell(0, 1)).thenReturn('_');

        player.move();
        assertEquals(1, player.getNumberOfSteps());
    }

    @Test
    void testSetGameBoard() {

        player = new Player(null, 0);

        // Kezdetben a játékost null GameBoard-dal inicializáljuk
        assertNull(player.getGameBoard());

        // Játékost állítunk be GameBoard referenciával
        player.setGameBoard(gameBoardMock);

        // Ellenőrizzük, hogy a játékos most már a megfelelő GameBoard-ot tartalmazza-e
        assertEquals(gameBoardMock, player.getGameBoard());
    }

    @Test
    void testMoveSuccessfulNorth() {
        // Teszteljük a sikeres észak felé való mozgást
        when(gameBoardMock.getPlayerRow()).thenReturn(1);
        when(gameBoardMock.getPlayerCol()).thenReturn(1);
        when(gameBoardMock.getPlayerDirection()).thenReturn('N');
        when(gameBoardMock.getSize()).thenReturn(5);
        when(gameBoardMock.getCell(0, 1)).thenReturn('_');

        player.move();
        assertEquals(1, player.getNumberOfSteps());
    }

    @Test
    void testMoveSuccessfulSouth() {
        // Teszteljük a sikeres dél felé való mozgást
        when(gameBoardMock.getPlayerRow()).thenReturn(1);
        when(gameBoardMock.getPlayerCol()).thenReturn(1);
        when(gameBoardMock.getPlayerDirection()).thenReturn('S');
        when(gameBoardMock.getSize()).thenReturn(5);
        when(gameBoardMock.getCell(2, 1)).thenReturn('_');

        player.move();
        assertEquals(1, player.getNumberOfSteps());
    }

    // Teszt az északi mozgásra falnak ütközve
    @Test
    void testMoveFailedNorthHitWall() {
        when(gameBoardMock.getPlayerRow()).thenReturn(0);
        when(gameBoardMock.getPlayerCol()).thenReturn(2);
        when(gameBoardMock.getPlayerDirection()).thenReturn('N');
        when(gameBoardMock.getSize()).thenReturn(3);
        when(gameBoardMock.getCell(0, 2)).thenReturn('W');

        player.move();
        assertEquals(0, player.getNumberOfSteps());
    }

    // Teszt a déli mozgásra falnak ütközve
    @Test
    void testMoveFailedSouthHitWall() {
        when(gameBoardMock.getPlayerRow()).thenReturn(2);
        when(gameBoardMock.getPlayerCol()).thenReturn(2);
        when(gameBoardMock.getPlayerDirection()).thenReturn('S');
        when(gameBoardMock.getSize()).thenReturn(3);
        when(gameBoardMock.getCell(2, 2)).thenReturn('W');

        player.move();
        assertEquals(0, player.getNumberOfSteps());
    }

    // Teszt a nyugati mozgásra falnak ütközve
    @Test
    void testMoveFailedWestHitWall() {
        when(gameBoardMock.getPlayerRow()).thenReturn(2);
        when(gameBoardMock.getPlayerCol()).thenReturn(0);
        when(gameBoardMock.getPlayerDirection()).thenReturn('W');
        when(gameBoardMock.getSize()).thenReturn(3);
        when(gameBoardMock.getCell(2, 0)).thenReturn('W');

        player.move();
        assertEquals(0, player.getNumberOfSteps());
    }

    // Teszt az egyéb irányú mozgásra falnak ütközve
    @Test
    void testMoveFailedHitWallOtherDirections() {
        when(gameBoardMock.getPlayerRow()).thenReturn(0);
        when(gameBoardMock.getPlayerCol()).thenReturn(0);
        when(gameBoardMock.getPlayerDirection()).thenReturn('E');
        when(gameBoardMock.getSize()).thenReturn(3);
        when(gameBoardMock.getCell(0, 0)).thenReturn('W');

        int initialNumberOfSteps = player.getNumberOfSteps();
        player.move();
        assertEquals(1, player.getNumberOfSteps());
    }

    @Test
    void testTurnRightFromNorth() {
        when(gameBoardMock.getPlayerDirection()).thenReturn('N');

        int initialNumberOfSteps = player.getNumberOfSteps();
        player.turnRight();

        // Ellenőrizzük a játékos irányát és a lépésszám változását
        verify(gameBoardMock).setPlayerPosition(anyInt(), anyInt(), eq('E'));
        assertEquals(initialNumberOfSteps + 1, player.getNumberOfSteps());
    }

    @Test
    void testTurnRightFromSouth() {
        when(gameBoardMock.getPlayerDirection()).thenReturn('S');

        int initialNumberOfSteps = player.getNumberOfSteps();
        player.turnRight();

        // Ellenőrizzük a játékos irányát és a lépésszám változását
        verify(gameBoardMock).setPlayerPosition(anyInt(), anyInt(), eq('W'));
        assertEquals(initialNumberOfSteps + 1, player.getNumberOfSteps());
    }

    @Test
    void testTurnRightFromWest() {
        when(gameBoardMock.getPlayerDirection()).thenReturn('W');

        int initialNumberOfSteps = player.getNumberOfSteps();
        player.turnRight();

        // Ellenőrizzük a játékos irányát és a lépésszám változását
        verify(gameBoardMock).setPlayerPosition(anyInt(), anyInt(), eq('N'));
        assertEquals(initialNumberOfSteps + 1, player.getNumberOfSteps());
    }

    @Test
    void testTurnRightFromEast() {
        when(gameBoardMock.getPlayerDirection()).thenReturn('E');

        int initialNumberOfSteps = player.getNumberOfSteps();
        player.turnRight();

        // Ellenőrizzük a játékos irányát és a lépésszám változását
        verify(gameBoardMock).setPlayerPosition(anyInt(), anyInt(), eq('S'));
        assertEquals(initialNumberOfSteps + 1, player.getNumberOfSteps());
    }

    @Test
    void testTurnLeftFromNorth() {
        when(gameBoardMock.getPlayerDirection()).thenReturn('N');

        int initialNumberOfSteps = player.getNumberOfSteps();
        player.turnLeft();

        verify(gameBoardMock).setPlayerPosition(anyInt(), anyInt(), eq('W'));
        assertEquals(initialNumberOfSteps + 1, player.getNumberOfSteps());
    }

    @Test
    void testTurnLeftFromSouth() {
        when(gameBoardMock.getPlayerDirection()).thenReturn('S');

        int initialNumberOfSteps = player.getNumberOfSteps();
        player.turnLeft();

        verify(gameBoardMock).setPlayerPosition(anyInt(), anyInt(), eq('E'));
        assertEquals(initialNumberOfSteps + 1, player.getNumberOfSteps());
    }

    @Test
    void testTurnLeftFromWest() {
        when(gameBoardMock.getPlayerDirection()).thenReturn('W');

        int initialNumberOfSteps = player.getNumberOfSteps();
        player.turnLeft();

        verify(gameBoardMock).setPlayerPosition(anyInt(), anyInt(), eq('S'));
        assertEquals(initialNumberOfSteps + 1, player.getNumberOfSteps());
    }

    @Test
    void testTurnLeftFromEast() {
        when(gameBoardMock.getPlayerDirection()).thenReturn('E');

        int initialNumberOfSteps = player.getNumberOfSteps();
        player.turnLeft();

        verify(gameBoardMock).setPlayerPosition(anyInt(), anyInt(), eq('N'));
        assertEquals(initialNumberOfSteps + 1, player.getNumberOfSteps());
    }

    @Test
    void testSuccessfulShot() {
        when(gameBoardMock.getPlayerDirection()).thenReturn('N');
        when(gameBoardMock.getPlayerRow()).thenReturn(1);
        when(gameBoardMock.getPlayerCol()).thenReturn(1);
        when(gameBoardMock.getCell(0, 1)).thenReturn('U');
        // amikor a switch utasítás meghívásra kerül, az első esetet kell elérni (case 'N'), hogy csökkentse a 'row' értékét

        int initialScore = player.getScore();
        int initialArrows = player.getArrows();

        player.shoot();

        assertEquals(initialScore, player.getScore());
        assertEquals(initialArrows, player.getArrows());
        // Egyéb ellenőrzések a változásokhoz
    }

    @Test
    void testShootEast() {
        when(gameBoardMock.getPlayerRow()).thenReturn(1);
        when(gameBoardMock.getPlayerCol()).thenReturn(1);
        when(gameBoardMock.getPlayerDirection()).thenReturn('E');
        when(gameBoardMock.getSize()).thenReturn(3);
        when(gameBoardMock.getCell(1, 2)).thenReturn('_'); // 'U' field is not present in this cell

        int initialScore = player.getScore();
        int initialArrows = player.getArrows();

        player.shoot();

        assertEquals(initialScore, player.getScore()); // Score should remain unchanged
        assertEquals(initialArrows - 1, player.getArrows()); // One arrow is used
        // Other assertions if needed
    }

    @Test
    void testFailedShotHitWall() {
        when(gameBoardMock.getPlayerDirection()).thenReturn('S');
        when(gameBoardMock.getPlayerRow()).thenReturn(2);
        when(gameBoardMock.getPlayerCol()).thenReturn(2);
        when(gameBoardMock.getCell(3, 2)).thenReturn('W');

        int initialScore = player.getScore();
        int initialArrows = player.getArrows();

        player.shoot();

        assertEquals(initialScore, player.getScore());
        assertEquals(initialArrows, player.getArrows());
        // Egyéb ellenőrzések a változásokhoz
    }

    @Test
    void testFailedShotHitEdge() {
        when(gameBoardMock.getPlayerDirection()).thenReturn('W');
        when(gameBoardMock.getPlayerRow()).thenReturn(2);
        when(gameBoardMock.getPlayerCol()).thenReturn(0);

        int initialScore = player.getScore();
        int initialArrows = player.getArrows();

        player.shoot();

        assertEquals(initialScore, player.getScore());
        assertEquals(initialArrows, player.getArrows());
        // Egyéb ellenőrzések a változásokhoz
    }

    @Test
    void testPickUpGoldWhenPlayerStandsOnGold() {
        GameBoard gameBoardMock = mock(GameBoard.class);
        when(gameBoardMock.getPlayerRow()).thenReturn(1);
        when(gameBoardMock.getPlayerCol()).thenReturn(1);
        when(gameBoardMock.getCell(1, 1)).thenReturn('G');

        Player player = new Player(gameBoardMock, 3);
        int initialArrows = player.getArrows();

        player.pickUpGold();

        verify(gameBoardMock).setCell(1, 1, '_');
        assertEquals(initialArrows, player.getArrows()); // Arrows should remain unchanged
    }

    @Test
    void testPickUpGoldWhenNoGoldPresent() {
        GameBoard gameBoardMock = mock(GameBoard.class);
        when(gameBoardMock.getPlayerRow()).thenReturn(1);
        when(gameBoardMock.getPlayerCol()).thenReturn(1);
        when(gameBoardMock.getCell(1, 1)).thenReturn('_');

        Player player = new Player(gameBoardMock, 3);
        int initialArrows = player.getArrows();

        player.pickUpGold();

        verify(gameBoardMock, never()).setCell(anyInt(), anyInt(), anyChar());
        assertEquals(initialArrows, player.getArrows()); // Arrows should remain unchanged
    }

    @Test
    void testHasGoldWhenPlayerHasGold() {
        GameBoard gameBoardMock = mock(GameBoard.class);
        when(gameBoardMock.getPlayerRow()).thenReturn(1);
        when(gameBoardMock.getPlayerCol()).thenReturn(1);
        when(gameBoardMock.getBoard()).thenReturn(new char[][]{{'_', '_', '_'}, {'_', 'G', '_'}, {'_', '_', '_'}});

        Player player = new Player(gameBoardMock, 3);
        assertTrue(player.hasGold());
    }

    @Test
    void testHasGoldWhenPlayerDoesNotHaveGold() {
        GameBoard gameBoardMock = mock(GameBoard.class);
        when(gameBoardMock.getPlayerRow()).thenReturn(0);
        when(gameBoardMock.getPlayerCol()).thenReturn(0);

        // Hamis tábla beállítása, ahol nincs arany ('G')
        char[][] falseBoard = new char[][]{{'_', '_', '_'}, {'_', '_', '_'}, {'_', '_', '_'}};
        when(gameBoardMock.getBoard()).thenReturn(falseBoard);

        Player player = new Player(gameBoardMock, 3);
        assertFalse(player.hasGold());
    }

    @Test
    void testIsAliveWhenOnEmptyCell() {
        GameBoard gameBoardMock = mock(GameBoard.class);
        when(gameBoardMock.getPlayerRow()).thenReturn(0);
        when(gameBoardMock.getPlayerCol()).thenReturn(0);
        when(gameBoardMock.getBoard()).thenReturn(new char[][]{{'_', '_', '_'}, {'_', '_', '_'}, {'_', '_', '_'}});

        Player player = new Player(gameBoardMock, 3);
        assertTrue(player.isAlive());
    }

    @Test
    void testIsAliveWhenOnWumpusCell() {
        GameBoard gameBoardMock = mock(GameBoard.class);
        when(gameBoardMock.getPlayerRow()).thenReturn(0);
        when(gameBoardMock.getPlayerCol()).thenReturn(0);
        when(gameBoardMock.getBoard()).thenReturn(new char[][]{{'U', '_', '_'}, {'_', '_', '_'}, {'_', '_', '_'}});

        Player player = new Player(gameBoardMock, 3);
        assertFalse(player.isAlive());
    }

    @Test
    void testIsAliveWhenOnAnotherCell() {
        GameBoard gameBoardMock = mock(GameBoard.class);
        when(gameBoardMock.getPlayerRow()).thenReturn(0);
        when(gameBoardMock.getPlayerCol()).thenReturn(0);
        when(gameBoardMock.getBoard()).thenReturn(new char[][]{{'_', '_', '_'}, {'_', '_', '_'}, {'_', '_', 'P'}});

        Player player = new Player(gameBoardMock, 3);
        assertTrue(player.isAlive());
    }
    // További tesztek a maradék metódusokhoz (turnRight, turnLeft, shoot, pickUpGold, hasGold, isAlive, printStatus, stb.)
    // Ahhoz, hogy a tesztek 90%-os lefedettséget érjenek el, számos más forgatókönyvet is le kell fedni, ezek csak néhány példa.
}
