import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DbHandler {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/wumpus_game_db";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "admin";

    public void saveToDatabase(String username, int score) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String insertQuery = "INSERT INTO game_data (username, score) VALUES (?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
                preparedStatement.setString(1, username);
                preparedStatement.setInt(2, score);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new UncheckedSqlException(e);
        }
    }

    public int loadFromDatabase(String username) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String selectQuery = "SELECT score FROM game_data WHERE username = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(selectQuery)) {
                preparedStatement.setString(1, username);
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    return resultSet.getInt("score");
                }
            }
        } catch (SQLException e) {
            throw new UncheckedSqlException(e);
        }
        return 0;
    }

    public List<PlayerScore> getHighestScoresForAllUsers() {
        List<PlayerScore> highestScores = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String selectQuery = "SELECT username, MAX(score) AS highest_score FROM game_data GROUP BY username";
            try (PreparedStatement preparedStatement = connection.prepareStatement(selectQuery)) {
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    String username = resultSet.getString("username");
                    int highestScore = resultSet.getInt("highest_score");
                    highestScores.add(new PlayerScore(username, highestScore));
                }
            }
        } catch (SQLException e) {
            throw new UncheckedSqlException(e);
        }

        return highestScores;
    }
}
