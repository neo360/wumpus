import org.junit.jupiter.api.*;
import org.mockito.*;

import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DbHandlerTest {

    @Test
    void testSaveToDatabase() throws SQLException {
        // Mock a Connection, PreparedStatement, and ResultSet
        Connection connectionMock = mock(Connection.class);
        PreparedStatement preparedStatementMock = mock(PreparedStatement.class);

        // Mock the DriverManager to return the connectionMock
        Mockito.lenient().when(DriverManager.getConnection(Mockito.anyString())).thenReturn(connectionMock);

        DbHandler dbHandler = new DbHandler();
        dbHandler.saveToDatabase("test_user", 100);

        // Verify that the PreparedStatement was executed with the correct parameters
        verify(connectionMock).prepareStatement("INSERT INTO game_data (username, score) VALUES (?, ?)");
        verify(preparedStatementMock).setString(1, "test_user");
        verify(preparedStatementMock).setInt(2, 100);
        verify(preparedStatementMock).executeUpdate();
    }

    @Test
    void testLoadFromDatabase() throws SQLException {
        // Mock a Connection, PreparedStatement, and ResultSet
        Connection connectionMock = mock(Connection.class);
        PreparedStatement preparedStatementMock = mock(PreparedStatement.class);
        ResultSet resultSetMock = mock(ResultSet.class);

        // Mock the DriverManager to return the connectionMock
        Mockito.lenient().when(DriverManager.getConnection(Mockito.anyString())).thenReturn(connectionMock);
        Mockito.lenient().when(connectionMock.prepareStatement(Mockito.anyString())).thenReturn(preparedStatementMock);
        Mockito.lenient().when(preparedStatementMock.executeQuery()).thenReturn(resultSetMock);
        Mockito.lenient().when(resultSetMock.next()).thenReturn(true);
        Mockito.lenient().when(resultSetMock.getInt("score")).thenReturn(150);

        DbHandler dbHandler = new DbHandler();
        int score = dbHandler.loadFromDatabase("test_user");

        assertEquals(150, score);
        // Verify that the PreparedStatement was executed with the correct parameters
        verify(connectionMock).prepareStatement("SELECT score FROM game_data WHERE username = ?");
        verify(preparedStatementMock).setString(1, "test_user");
        verify(preparedStatementMock).executeQuery();
        verify(resultSetMock).next();
        verify(resultSetMock).getInt("score");
    }
}
