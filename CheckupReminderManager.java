package Health;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;

public class CheckupReminderManager {

    public static void insertCheckupReminder(Connection con, int userId, String message, Date checkupDate) {
        String insertQuery = "INSERT INTO CheckupReminder (CheckupReminderMessage, CheckupReminderDate, UserId) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = con.prepareStatement(insertQuery)) {
            pstmt.setString(1, message);
            pstmt.setDate(2, checkupDate);
            pstmt.setInt(3, userId);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Checkup reminder recorded successfully.");
            } else {
                System.out.println("Failed to record checkup reminder.");
            }
        } catch (SQLException e) {
            System.out.println("Error inserting checkup reminder: " + e.getMessage());
        }
    }

    public static void checkAndRemindAfter30Days(Connection con) {
        LocalDate thirtyDaysAgo = LocalDate.now().minusDays(30);
        Date thirtyDaysAgoSql = Date.valueOf(thirtyDaysAgo);

        String updateQuery = "UPDATE CheckupReminder SET LastReminderDate = ? WHERE CheckupReminderDate <= ? AND LastReminderDate IS NULL";
        try (PreparedStatement pstmt = con.prepareStatement(updateQuery)) {
            pstmt.setDate(1, Date.valueOf(LocalDate.now())); // Set current date as LastReminderDate
            pstmt.setDate(2, thirtyDaysAgoSql);

            int rowsAffected = pstmt.executeUpdate();
            System.out.println(rowsAffected + " reminders updated.");
        } catch (SQLException e) {
            System.out.println("Error updating reminders: " + e.getMessage());
        }
    }
}

