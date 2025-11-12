import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MovieService {

    public void viewMoviesAndShowtimes() throws SQLException {
        try (Connection conn = DBUtil.getConnection()) {
            String movieSql = "SELECT * FROM movie";
            PreparedStatement movieStmt = conn.prepareStatement(movieSql);
            ResultSet moviesRs = movieStmt.executeQuery();

            System.out.println("Movies & Showtimes:");
            while (moviesRs.next()) {
                int movieId = moviesRs.getInt("movie_id");
                String title = moviesRs.getString("title");
                System.out.println("\n" + movieId + ": " + title);

                String showSql = "SELECT * FROM showtime WHERE movie_id = ?";
                PreparedStatement pst = conn.prepareStatement(showSql);
                pst.setInt(1, movieId);
                ResultSet showRs = pst.executeQuery();
                while (showRs.next()) {
                    System.out.println("  Showtime ID: " + showRs.getInt("showtime_id") +
                            ", Time: " + showRs.getTimestamp("date_time") +
                            ", Seats Available: " + showRs.getInt("available_seats"));
                }
            }
        }
    }
}