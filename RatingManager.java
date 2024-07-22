package Health;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class RatingManager {

    // Method to insert a new rating into the Rating table
    public static void insertRating(Connection con, int userId, int ratingCount, String ratingComment) throws SQLException {
        String insertQuery = "INSERT INTO Rating (RatingCount, RatingComment, UserId) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = con.prepareStatement(insertQuery)) {
            pstmt.setInt(1, ratingCount);
            pstmt.setString(2, ratingComment);
            pstmt.setInt(3, userId);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Rating recorded successfully.");
            } else {
                System.out.println("Failed to record rating.");
            }
        }
    }
}

