import javax.swing.SwingUtilities;
import java.util.Scanner;

public class Main {
    public static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MovieBookingGUI().setVisible(true);
        });
    }

    public static Scanner getScanner() {
        return scanner;
    }
}