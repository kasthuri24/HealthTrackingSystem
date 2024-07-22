package Health;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class HealthObservationManager {

    // Method to display available health observation types
    public static void displayHealthObservationTypes(Connection con) {
        try {
            String selectQuery = "SELECT HealthObservationType FROM HealthObservationCategory";
            try (PreparedStatement pstmt = con.prepareStatement(selectQuery);
                 ResultSet rs = pstmt.executeQuery()) {
                System.out.println("Available Health Observation Types:");
                while (rs.next()) {
                    String observationType = rs.getString("HealthObservationType");
                    System.out.println("- " + observationType);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving health observation types: " + e.getMessage());
        }
    }
}