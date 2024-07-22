package Health;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.time.LocalDate;
public class WalkingStepsManager {

    public static void insertWalkingSteps(Connection con, int userId, int healthObservationId, int walkingStepCount) {
        LocalDate today = LocalDate.now();
        Date sqlDate = Date.valueOf(today);

        String insertQuery = "INSERT INTO WalkingSteps (WalkingStepCount, WalkingEntryDate, UserId, HealthObservationId) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = con.prepareStatement(insertQuery)) {
            pstmt.setInt(1, walkingStepCount);
            pstmt.setDate(2, sqlDate);
            pstmt.setInt(3, userId);
            pstmt.setInt(4, healthObservationId);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Walking steps recorded successfully.");
                generateWalkingStepsReport(con, userId, walkingStepCount);
            } else {
                System.out.println("Failed to record walking steps.");
            }
        } catch (SQLException e) {
            System.out.println("Error inserting walking steps: " + e.getMessage());
        }
    }

    public static void generateWalkingStepsReport(Connection con, int userId, int walkingStepCount) {
        try {
            int userAge = getUserAge(con, userId);
            int userBMI = getUserBMI(con, userId);

            String report = generateWalkingStepsReport(walkingStepCount, userAge, userBMI);
            updateWalkingStepsReport(con, userId, report);
        } catch (SQLException e) {
            System.out.println("Error generating walking steps report: " + e.getMessage());
        }
    }

    private static int getUserAge(Connection con, int userId) throws SQLException {
        String selectQuery = "SELECT userAge FROM Users WHERE UserId = ?";
        try (PreparedStatement pstmt = con.prepareStatement(selectQuery)) {
            pstmt.setInt(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("userAge");
                } else {
                    throw new SQLException("User not found.");
                }
            }
        }
    }

    private static int getUserBMI(Connection con, int userId) throws SQLException {
        String selectQuery = "SELECT userBMI FROM Users WHERE UserId = ?";
        try (PreparedStatement pstmt = con.prepareStatement(selectQuery)) {
            pstmt.setInt(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("userBMI");
                } else {
                    throw new SQLException("User not found.");
                }
            }
        }
    }

    private static String generateWalkingStepsReport(int walkingStepCount, int userAge, int userBMI) {
        int recommendedSteps = calculateRecommendedSteps(userAge, userBMI);

        if (walkingStepCount > recommendedSteps) {
            return "You took " + walkingStepCount + " steps today, which is considered a high intensity walk.";
        } else if (walkingStepCount < recommendedSteps) {
            return "You took " + walkingStepCount + " steps today, which is considered a low intensity walk. Aim for more!";
        } else {
            return "You took exactly " + walkingStepCount + " steps today, which is considered a medium intensity walk.";
        }
    }

    private static int calculateRecommendedSteps(int userAge, int userBMI) {
        // Example logic to calculate recommended steps
        // Adjust based on specific guidelines or requirements
        int recommendedSteps = 0;

        if (userAge < 30 && userBMI < 25) {
            recommendedSteps = 10000; // Example: younger and normal BMI
        } else if (userAge >= 30 && userBMI >= 25) {
            recommendedSteps = 8000; // Example: older and overweight
        } else {
            recommendedSteps = 8000; // Default recommendation
        }

        return recommendedSteps;
    }

    private static void updateWalkingStepsReport(Connection con, int userId, String report) throws SQLException {
        String updateQuery = "UPDATE WalkingSteps SET WalkingStepReport = ? WHERE UserId = ? AND WalkingEntryDate = ?";
        LocalDate today = LocalDate.now();
        Date sqlDate = Date.valueOf(today);

        try (PreparedStatement pstmt = con.prepareStatement(updateQuery)) {
            pstmt.setString(1, report);
            pstmt.setInt(2, userId);
            pstmt.setDate(3, sqlDate);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Walking steps report updated successfully.");
            } else {
                System.out.println("Failed to update walking steps report.");
            }
        }
    }

}
