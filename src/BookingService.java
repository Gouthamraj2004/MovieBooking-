import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.*;

public class BookingService {
    private final Scanner scanner = Main.getScanner();

    public void bookTickets() throws SQLException {
        try (Connection conn = DBUtil.getConnection()) {
            System.out.print("Enter your name: ");
            String userName = scanner.nextLine();

            showAllMoviesAndShowtimes(conn);

            System.out.print("Enter the showtime ID to book: ");
            int showtimeId = Integer.parseInt(scanner.nextLine());

            System.out.print("Number of seats to book: ");
            int seatsToBook = Integer.parseInt(scanner.nextLine());

            int availableSeats = getAvailableSeats(conn, showtimeId);
            if (availableSeats == -1) {
                System.out.println("Invalid showtime ID!");
                return;
            }
            if (seatsToBook > availableSeats) {
                System.out.println("Sorry, not enough seats available!");
                return;
            }

            insertBooking(conn, showtimeId, userName, seatsToBook);
            updateAvailableSeats(conn, showtimeId, seatsToBook);

            System.out.println("Booking successful! Enjoy your movie.");
        }
    }

    private void showAllMoviesAndShowtimes(Connection conn) throws SQLException {
        String movieSql = "SELECT * FROM movie";
        PreparedStatement movieStmt = conn.prepareStatement(movieSql);
        ResultSet moviesRs = movieStmt.executeQuery();

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

    private int getAvailableSeats(Connection conn, int showtimeId) throws SQLException {
        String seatCheckSql = "SELECT available_seats FROM showtime WHERE showtime_id = ?";
        PreparedStatement seatCheckStmt = conn.prepareStatement(seatCheckSql);
        seatCheckStmt.setInt(1, showtimeId);
        ResultSet seatRes = seatCheckStmt.executeQuery();

        if (seatRes.next()) {
            return seatRes.getInt("available_seats");
        }
        return -1; // invalid showtime
    }

    private void insertBooking(Connection conn, int showtimeId, String userName, int seats) throws SQLException {
        String bookingSql = "INSERT INTO booking (showtime_id, user_name, seats_booked, booking_time) VALUES (?, ?, ?, NOW())";
        PreparedStatement bookingStmt = conn.prepareStatement(bookingSql);
        bookingStmt.setInt(1, showtimeId);
        bookingStmt.setString(2, userName);
        bookingStmt.setInt(3, seats);
        bookingStmt.executeUpdate();
    }

    private void updateAvailableSeats(Connection conn, int showtimeId, int seats) throws SQLException {
        String updateSeatsSql = "UPDATE showtime SET available_seats = available_seats - ? WHERE showtime_id = ?";
        PreparedStatement updateSeatsStmt = conn.prepareStatement(updateSeatsSql);
        updateSeatsStmt.setInt(1, seats);
        updateSeatsStmt.setInt(2, showtimeId);
        updateSeatsStmt.executeUpdate();
    }

    public void viewBookingHistory() throws SQLException {
        try (Connection conn = DBUtil.getConnection()) {
            System.out.print("Enter your name to view booking history: ");
            String userName = scanner.nextLine();

            String sql = "SELECT b.booking_id, m.title, s.date_time, b.seats_booked, b.booking_time " +
                    "FROM booking b JOIN showtime s ON b.showtime_id = s.showtime_id " +
                    "JOIN movie m ON s.movie_id = m.movie_id " +
                    "WHERE b.user_name = ? ORDER BY b.booking_time DESC";

            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, userName);
            ResultSet rs = pst.executeQuery();

            System.out.println("Your Booking History:");
            while (rs.next()) {
                System.out.println("Booking ID: " + rs.getInt("booking_id") +
                        ", Movie: " + rs.getString("title") +
                        ", Showtime: " + rs.getTimestamp("date_time") +
                        ", Seats: " + rs.getInt("seats_booked") +
                        ", Booked On: " + rs.getTimestamp("booking_time"));
            }
        }
    }

    public void viewRecentlyBooked() throws SQLException {
        try (Connection conn = DBUtil.getConnection()) {
            System.out.print("Enter your name to view recently booked movies: ");
            String userName = scanner.nextLine();

            String sql = "SELECT DISTINCT m.title " +
                    "FROM booking b JOIN showtime s ON b.showtime_id = s.showtime_id " +
                    "JOIN movie m ON s.movie_id = m.movie_id " +
                    "WHERE b.user_name = ? " +
                    "ORDER BY b.booking_time DESC LIMIT 5";

            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, userName);
            ResultSet rs = pst.executeQuery();

            System.out.println("Recently Booked Movies:");
            int count = 0;
            List<String> movies = new ArrayList<>();
            while (rs.next()) {
                movies.add(rs.getString("title"));
                count++;
            }
            if (count == 0) {
                System.out.println("You have no recent bookings.");
            } else {
                for (String movie : movies) {
                    System.out.println(movie);
                }
            }
        }
    }
}