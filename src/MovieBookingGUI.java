import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MovieBookingGUI extends JFrame {
    private MovieService movieService = new MovieService();
    private BookingService bookingService = new BookingService();
    private FavoriteService favoriteService = new FavoriteService();

    public MovieBookingGUI() {
        setTitle("Movie Booking System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);

        // Create buttons
        JButton viewMoviesButton = new JButton("View Movies & Showtimes");
        JButton bookTicketsButton = new JButton("Book Tickets");
        JButton bookingHistoryButton = new JButton("View Booking History");
        JButton favoriteButton = new JButton("Mark/Unmark Favorite");
        JButton viewFavoritesButton = new JButton("View Favorites");
        JButton recentlyBookedButton = new JButton("Recently Booked Movies");

        // Add action listeners
        viewMoviesButton.addActionListener(e -> showMovies());
        bookTicketsButton.addActionListener(e -> bookTickets());
        bookingHistoryButton.addActionListener(e -> viewBookingHistory());
        favoriteButton.addActionListener(e -> markUnmarkFavorite());
        viewFavoritesButton.addActionListener(e -> viewFavorites());
        recentlyBookedButton.addActionListener(e -> viewRecentlyBooked());

        // Layout
        setLayout(new GridLayout(6, 1, 10, 10));
        add(viewMoviesButton);
        add(bookTicketsButton);
        add(bookingHistoryButton);
        add(favoriteButton);
        add(viewFavoritesButton);
        add(recentlyBookedButton);
    }

    private void showMovies() {
        try {
            movieService.viewMoviesAndShowtimes();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void bookTickets() {
        try {
            bookingService.bookTickets();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void viewBookingHistory() {
        try {
            bookingService.viewBookingHistory();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void markUnmarkFavorite() {
        try {
            favoriteService.markUnmarkFavorite();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void viewFavorites() {
        try {
            favoriteService.viewFavorites();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void viewRecentlyBooked() {
        try {
            bookingService.viewRecentlyBooked();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MovieBookingGUI().setVisible(true);
        });
    }
}
