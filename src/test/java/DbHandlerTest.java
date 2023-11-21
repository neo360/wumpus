import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DbHandlerTest {

    private Connection connection;
    private DbHandler dbHandler;

    @BeforeEach
    void setup() throws SQLException {
        String DB_URL = "jdbc:mysql://localhost:3306/wumpus_game_db";
        String DB_USER = "root";
        String DB_PASSWORD = "admin";

        connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        dbHandler = new DbHandler();
    }

    @AfterEach
    void tearDown() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    @Test
    void testSaveToDatabase() throws SQLException {
        String username = "test_user";
        int score = 100;

        dbHandler.saveToDatabase(username, score);

        PreparedStatement statement = connection.prepareStatement("SELECT score FROM game_data WHERE username = ?");
        statement.setString(1, username);
        ResultSet resultSet = statement.executeQuery();
        assertTrue(resultSet.next());
        assertEquals(score, resultSet.getInt("score"));
    }

    @Test
    void testSaveToDatabaseSQLException() {
        assertThrows(SQLException.class, () -> {
            String invalidURL = "invalid_url";
            dbHandler.saveToDatabase("test_user", 100);
            DriverManager.getConnection(invalidURL);
        });
    }

    @Test
    void testLoadFromDatabaseExistingUser() {
        String username = "existing_user";
        int score = 150;
        dbHandler.saveToDatabase(username, score);

        int loadedScore = dbHandler.loadFromDatabase(username);
        assertEquals(score, loadedScore);
    }

    @Test
    void testLoadFromDatabaseNonExistingUser() {
        int loadedScore = dbHandler.loadFromDatabase("non_existing_user");
        assertEquals(0, loadedScore);
    }

    @Test
    void testGetHighScores_ShouldReturnListOfPlayerScores() throws SQLException {
        DbHandler dbHandler = new DbHandler();
        Connection mockedConnection = mock(Connection.class);
        PreparedStatement mockedPreparedStatement = mock(PreparedStatement.class);
        ResultSet mockedResultSet = mock(ResultSet.class);

        List<PlayerScore> expectedScores = new ArrayList<>();
        expectedScores.add(new PlayerScore("user1", 100));
        expectedScores.add(new PlayerScore("user2", 90));

        when(mockedConnection.prepareStatement(anyString())).thenReturn(mockedPreparedStatement);
        when(mockedPreparedStatement.executeQuery()).thenReturn(mockedResultSet);
        when(mockedResultSet.next()).thenReturn(true).thenReturn(true).thenReturn(false); // Hamis, ha nincs további eredmény
        when(mockedResultSet.getString("username")).thenReturn("user1").thenReturn("user2");
        when(mockedResultSet.getInt("score")).thenReturn(100).thenReturn(90);

        List<PlayerScore> actualScores = dbHandler.getHighScores();

        assertEquals(actualScores.size(), actualScores.size());
        for (int i = 0; i < expectedScores.size(); i++) {
            assertEquals(actualScores.get(i).getUsername(), actualScores.get(i).getUsername());
            assertEquals(actualScores.get(i).getScore(), actualScores.get(i).getScore());
        }
    }
}
