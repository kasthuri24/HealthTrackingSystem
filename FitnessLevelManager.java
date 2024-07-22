package Health;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
public class FitnessLevelManager {
    // Method to update fitness status based on BMI
    public static void updateFitnessStatus(Connection con, String userEmail) {
        try {
            String selectQuery = "SELECT userBMI FROM Users WHERE userEmail = ?";
            String updateQuery = "INSERT INTO FitnessLevel (FitnessStatus, UserId) VALUES (?, ?)";
            try (PreparedStatement selectStmt = con.prepareStatement(selectQuery);
                 PreparedStatement updateStmt = con.prepareStatement(updateQuery)) {

                selectStmt.setString(1, userEmail);
                ResultSet rs = selectStmt.executeQuery();

                if (rs.next()) {
                    int userBMI = rs.getInt("userBMI");
                    String fitnessStatus = calculateFitnessStatus(userBMI);

                    updateStmt.setString(1, fitnessStatus);
                    updateStmt.setInt(2, getUserIdByEmail(con, userEmail));

                    int rowsAffected = updateStmt.executeUpdate();
                    if (rowsAffected > 0) {
                        System.out.println("Fitness level updated successfully.");
                    } else {
                        System.out.println("Failed to update fitness level.");
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Error updating fitness level: " + e.getMessage());
        }
    }

    // Helper method to calculate fitness status based on BMI value
    private static String calculateFitnessStatus(int bmi) {
        if (bmi < 18.5) {
            return "Underweight";
        } else if (bmi >= 18.5 && bmi <= 24.9) {
            return "Healthy Weight";
        } else if (bmi >= 25.0 && bmi <= 29.9) {
            return "Overweight";
        } else {
            return "Obesity";
        }
    }
    // Helper method to retrieve UserId from Users table using userEmail
    static int getUserIdByEmail(Connection con, String userEmail) throws SQLException {
        String getUserIdQuery = "SELECT UserId FROM Users WHERE userEmail = ?";
        try (PreparedStatement pstmt = con.prepareStatement(getUserIdQuery)) {
            pstmt.setString(1, userEmail);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("UserId");
                } else {
                    throw new SQLException("User not found for email: " + userEmail);
                }
            }
        }
    }
}
