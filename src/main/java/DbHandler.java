import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DbHandler {
    private static final String DB_URL = "jdbc:sqlite:game_data.db";

    public void saveToDatabase(String username, int score) {
        try (Connection connection = DriverManager.getConnection(DB_URL)) {
            String insertQuery = "INSERT INTO game_data (username, score) VALUES (?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
                preparedStatement.setString(1, username);
                preparedStatement.setInt(2, score);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int loadFromDatabase(String username) {
        try (Connection connection = DriverManager.getConnection(DB_URL)) {
            String selectQuery = "SELECT score FROM game_data WHERE username = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(selectQuery)) {
                preparedStatement.setString(1, username);
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    return resultSet.getInt("score");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0; // Ha nincs találat vagy valami hiba történik, visszaadjuk az alapértelmezett értéket
    }
}