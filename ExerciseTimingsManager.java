package Health;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class ExerciseTimingsManager {

    // Method to insert exercise timings for a user on a specific date
    public static void insertExerciseTimings(Connection con, int userId, int healthObservationId, int exerciseTotalHours) {
        LocalDate today = LocalDate.now();
        Date sqlDate = Date.valueOf(today);

        String insertQuery = "INSERT INTO ExerciseTimings (ExerciseTotalHours, ExerciseEntryDate, UserId, HealthObservationId) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = con.prepareStatement(insertQuery)) {
            pstmt.setInt(1, exerciseTotalHours);
            pstmt.setDate(2, sqlDate);
            pstmt.setInt(3, userId);
            pstmt.setInt(4, healthObservationId);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Exercise timings recorded successfully.");
                generateExerciseReport(con, userId, exerciseTotalHours);
            } else {
                System.out.println("Failed to record exercise timings.");
            }
        } catch (SQLException e) {
            System.out.println("Error inserting exercise timings: " + e.getMessage());
        }
    }

    // Method to calculate and generate exercise report based on age and BMI
    public static void generateExerciseReport(Connection con, int userId, int exerciseTotalHours) {
        try {
            int userAge = getUserAge(con, userId);
            int userBMI = getUserBMI(con, userId);

            // Determine recommended exercise hours based on age and BMI
            int recommendedExerciseHours = calculateRecommendedExerciseHours(userAge, userBMI);

            // Generate exercise report based on actual and recommended hours
            String report = generateReport(exerciseTotalHours, recommendedExerciseHours);

            // Update ExerciseTimings table with the generated report
            updateExerciseReport(con, userId, report);
        } catch (SQLException e) {
            System.out.println("Error generating exercise report: " + e.getMessage());
        }
    }

    // Method to retrieve user's age from database
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

    // Method to retrieve user's BMI from database
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

    // Method to calculate recommended exercise hours based on age and BMI
    private static int calculateRecommendedExerciseHours(int age, int bmi) {
        // Example calculation logic; adjust based on your specific requirements
        int recommendedHours = 0;
        if (age < 30 && bmi < 25) {
            recommendedHours = 2; // Example: younger and normal BMI
        } else if (age >= 30 && bmi >= 25) {
            recommendedHours = 1; // Example: older and overweight
        } else {
            recommendedHours = 1; // Default recommendation
        }
        return recommendedHours;
    }

    // Method to generate exercise report based on actual and recommended hours
    private static String generateReport(int actualHours, int recommendedHours) {
        String report;
        if (actualHours > recommendedHours) {
            report = "You exceeded the recommended exercise hours. Great job!";
        } else if (actualHours < recommendedHours) {
            report = "You did not meet the recommended exercise hours. Aim for more activity!";
        } else {
            report = "You met the recommended exercise hours. Keep up the good work!";
        }
        return report;
    }

    // Method to update ExerciseReport field in ExerciseTimings table
    private static void updateExerciseReport(Connection con, int userId, String report) throws SQLException {
        String updateQuery = "UPDATE ExerciseTimings SET ExerciseReport = ? WHERE UserId = ? AND ExerciseEntryDate = ?";
        LocalDate today = LocalDate.now();
        Date sqlDate = Date.valueOf(today);

        try (PreparedStatement pstmt = con.prepareStatement(updateQuery)) {
            pstmt.setString(1, report);
            pstmt.setInt(2, userId);
            pstmt.setDate(3, sqlDate);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Exercise report updated successfully.");
            } else {
                System.out.println("Failed to update exercise report.");
            }
        }
    }
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