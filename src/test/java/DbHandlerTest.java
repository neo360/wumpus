import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;

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
            DriverManager.getConnection(invalidURL); // SQLException-ra szimulálás
        });
    }

    @Test
    void testLoadFromDatabaseExistingUser() throws SQLException {
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
}
