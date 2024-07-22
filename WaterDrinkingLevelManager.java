package Health;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Date;
import java.time.LocalDate;

public class WaterDrinkingLevelManager {

    public static void insertWaterDrinkingLevel(Connection con, int userId, int healthObservationId, int waterLevelInLitres) {
        LocalDate today = LocalDate.now();
        Date sqlDate = Date.valueOf(today);

        String insertQuery = "INSERT INTO WaterDrinkingLevel (WaterLevelInLitres, WaterEntryDate, UserId, HealthObservationId) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = con.prepareStatement(insertQuery)) {
            pstmt.setInt(1, waterLevelInLitres);
            pstmt.setDate(2, sqlDate);
            pstmt.setInt(3, userId);
            pstmt.setInt(4, healthObservationId);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Water drinking level recorded successfully.");
                generateWaterDrinkingLevelReport(con, userId, waterLevelInLitres);
            } else {
                System.out.println("Failed to record water drinking level.");
            }
        } catch (SQLException e) {
            System.out.println("Error inserting water drinking level: " + e.getMessage());
        }
    }

    public static void generateWaterDrinkingLevelReport(Connection con, int userId, int waterLevelInLitres) {
        try {
            int recommendedIntake = calculateRecommendedWaterIntake(userId);
            String report = generateWaterDrinkingReport(waterLevelInLitres, recommendedIntake);
            updateWaterDrinkingLevelReport(con, userId, report);
        } catch (SQLException e) {
            System.out.println("Error generating water drinking level report: " + e.getMessage());
        }
    }

    private static int calculateRecommendedWaterIntake(int userId) throws SQLException {
        // Example logic to calculate recommended waterIntake
        // Replace with your specific calculation based on user's profile or guidelines
        return 2000; // Example: 2 liters recommended per day
    }

    private static String generateWaterDrinkingReport(int actualLitres, int recommendedLitres) {
        String report;
        if (actualLitres > recommendedLitres) {
            report = "You exceeded the recommended water intake. Stay hydrated!";
        } else if (actualLitres < recommendedLitres) {
            report = "You did not meet the recommended water intake. Drink more water!";
        } else {
            report = "You met the recommended water intake. Well done!";
        }
        return report;
    }

    private static void updateWaterDrinkingLevelReport(Connection con, int userId, String report) throws SQLException {
        String updateQuery = "UPDATE WaterDrinkingLevel SET WaterDrinkingLevelReport = ? WHERE UserId = ? AND WaterEntryDate = ?";
        LocalDate today = LocalDate.now();
        Date sqlDate = Date.valueOf(today);

        try (PreparedStatement pstmt = con.prepareStatement(updateQuery)) {
            pstmt.setString(1, report);
            pstmt.setInt(2, userId);
            pstmt.setDate(3, sqlDate);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Water drinking level report updated successfully.");
            } else {
                System.out.println("Failed to update water drinking level report.");
            }
        }
    }

}
