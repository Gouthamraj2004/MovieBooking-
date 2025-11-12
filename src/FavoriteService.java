import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.*;

public class FavoriteService {
    private final Scanner scanner = Main.getScanner();

    public void markUnmarkFavorite() throws SQLException {
        try (Connection conn = DBUtil.getConnection()) {
            System.out.print("Enter your name: ");
            String userName = scanner.nextLine();

            System.out.print("Enter the movie ID to toggle favorite: ");
            int movieId = Integer.parseInt(scanner.nextLine());

            // Check if already favorite
            String checkSql = "SELECT * FROM favorite WHERE movie_id = ? AND user_name = ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkSql);
            checkStmt.setInt(1, movieId);
            checkStmt.setString(2, userName);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                // Exists, so remove favorite
                String deleteSql = "DELETE FROM favorite WHERE movie_id = ? AND user_name = ?";
                PreparedStatement deleteStmt = conn.prepareStatement(deleteSql);
                deleteStmt.setInt(1, movieId);
                deleteStmt.setString(2, userName);
                deleteStmt.executeUpdate();
                System.out.println("Movie removed from favorites.");
            } else {
                // Add favorite
                String insertSql = "INSERT INTO favorite (movie_id, user_name) VALUES (?, ?)";
                PreparedStatement insertStmt = conn.prepareStatement(insertSql);
                insertStmt.setInt(1, movieId);
                insertStmt.setString(2, userName);
                insertStmt.executeUpdate();
                System.out.println("Movie added to favorites.");
            }
        }
    }

    public void viewFavorites() throws SQLException {
        try (Connection conn = DBUtil.getConnection()) {
            System.out.print("Enter your name to view favorites: ");
            String userName = scanner.nextLine();

            String sql = "SELECT m.movie_id, m.title FROM favorite f " +
                    "JOIN movie m ON f.movie_id = m.movie_id " +
                    "WHERE f.user_name = ?";

            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, userName);
            ResultSet rs = pst.executeQuery();

            System.out.println("Your Favorite Movies:");
            boolean hasFavorites = false;
            while (rs.next()) {
                hasFavorites = true;
                System.out.println(rs.getInt("movie_id") + ": " + rs.getString("title"));
            }
            if (!hasFavorites) {
                System.out.println("You have no favorite movies.");
            }
        }
    }
}