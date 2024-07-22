package Health;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Scanner;

public class Login {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to Health Tracking System!");

        while (true) {
            System.out.println("\nSelect an option:");
            System.out.println("1. Login");
            System.out.println("2. Register");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline character after nextInt()

            switch (choice) {
                case 1:
                    loginUserFlow(scanner);
                    break;
                case 2:
                    registerUserFlow(scanner);
                    break;
                case 3:
                    System.out.println("Exiting the Health Tracking System. Goodbye!");
                    scanner.close();
                    System.exit(0);
                default:
                    System.out.println("Invalid choice. Please enter 1, 2, or 3.");
                    break;
            }
        }
    }

    // Method to handle login flow
    private static void loginUserFlow(Scanner scanner) {
        System.out.print("Enter your email: ");
        String userEmail = scanner.nextLine().trim(); // Get user input for email

        System.out.print("Enter your password: ");
        String userPassword = scanner.nextLine().trim(); // Get user input for password

        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/HealthTrackingSystem", "root", "Kasthu@123")) {
            if (loginUser(con, userEmail, userPassword)) {
                System.out.println("Login successful.");

                // Ask user if they want to view fitness level
                System.out.print("Do you want to view your fitness level? (yes/no): ");
                String viewFitnessOption = scanner.nextLine().trim().toLowerCase();

                if (viewFitnessOption.equals("yes")) {
                    displayFitnessLevel(con, userEmail);
                    HealthObservationManager.displayHealthObservationTypes(con);
                    System.out.print("Enter exercise total hours: ");
                    int exerciseTotalHours = scanner.nextInt();
                    scanner.nextLine(); // Consume newline character after nextInt()
                    int userId = ExerciseTimingsManager.getUserIdByEmail(con, userEmail); // Resolve userId
                    ExerciseTimingsManager.insertExerciseTimings(con, userId, 1, exerciseTotalHours);
                    ExerciseTimingsManager.generateExerciseReport(con, userId, exerciseTotalHours); 
                    int waterLevelInLitres = scanner.nextInt();
                    scanner.nextLine(); // Consume newline character after nextInt()
                    WaterDrinkingLevelManager.insertWaterDrinkingLevel(con, userId, 1, waterLevelInLitres);
                    WaterDrinkingLevelManager.generateWaterDrinkingLevelReport(con, userId, waterLevelInLitres);
                    System.out.print("Enter walking step count: ");
                    int walkingStepCount = scanner.nextInt();
                    scanner.nextLine(); // Consume newline character after nextInt()
                    WalkingStepsManager.insertWalkingSteps(con, userId, 1, walkingStepCount);
                    WalkingStepsManager.generateWalkingStepsReport(con, userId, walkingStepCount);
                    System.out.print("Do you want to set a checkup reminder? (yes/no): ");
                    String answer = scanner.nextLine().trim().toLowerCase();
                    if (answer.equals("yes")) {
                        String message = "Reminder: Your checkup is due soon!";
                        Date checkupDate = Date.valueOf(LocalDate.now().plusDays(60)); // Example checkup date in the future

                        CheckupReminderManager.insertCheckupReminder(con, userId, message, checkupDate);
                        CheckupReminderManager.checkAndRemindAfter30Days(con);
                    } else {
                        System.out.println("You can set a checkup reminder later from the main menu.");
                    }
                      System.out.println("Rate For this application and your feedback for improvement");
                      System.out.println("Rate this application (1-5): ");
                      int ratingCount = scanner.nextInt();
                      scanner.nextLine(); // Consume newline character
                      System.out.println("Your feedback for improvement: ");
                      String ratingComment = scanner.nextLine().trim();
                      RatingManager.insertRating(con,userId, ratingCount,ratingComment);
                } else {
                    System.out.println("Okay, you are successfully logged in.");
                    HealthObservationManager.displayHealthObservationTypes(con);
                    System.out.print("Enter exercise total hours: ");
                    int exerciseTotalHours = scanner.nextInt();
                    scanner.nextLine(); // Consume newline character after nextInt()
                    int userId = ExerciseTimingsManager.getUserIdByEmail(con, userEmail); // Resolve userId
                    ExerciseTimingsManager.insertExerciseTimings(con, userId, 1, exerciseTotalHours);
                    ExerciseTimingsManager.generateExerciseReport(con, userId, exerciseTotalHours); 
                    int waterLevelInLitres = scanner.nextInt();
                    scanner.nextLine(); // Consume newline character after nextInt()
                    WaterDrinkingLevelManager.insertWaterDrinkingLevel(con, userId, 1, waterLevelInLitres);
                    WaterDrinkingLevelManager.generateWaterDrinkingLevelReport(con, userId, waterLevelInLitres);
                    System.out.print("Enter walking step count: ");
                    int walkingStepCount = scanner.nextInt();
                    scanner.nextLine(); // Consume newline character after nextInt()
                    WalkingStepsManager.insertWalkingSteps(con, userId, 1, walkingStepCount);
                    WalkingStepsManager.generateWalkingStepsReport(con, userId, walkingStepCount);
                    System.out.print("Do you want to set a checkup reminder? (yes/no): ");
                    String answer = scanner.nextLine().trim().toLowerCase();
                    if (answer.equals("yes")) {
                        String message = "Reminder: Your checkup is due soon!";
                        Date checkupDate = Date.valueOf(LocalDate.now().plusDays(60)); // Example checkup date in the future

                        CheckupReminderManager.insertCheckupReminder(con, userId, message, checkupDate);
                        CheckupReminderManager.checkAndRemindAfter30Days(con);
                    } else {
                        System.out.println("You can set a checkup reminder later from the main menu.");
                    }
                    System.out.println("Rate For this application and your feedback for improvement");
                    System.out.println("Rate this application (1-5): ");
                    int ratingCount = scanner.nextInt();
                    scanner.nextLine(); // Consume newline character
                    System.out.println("Your feedback for improvement: ");
                    String ratingComment = scanner.nextLine().trim();
                    RatingManager.insertRating(con,userId, ratingCount,ratingComment);


                }

            } else {
                System.out.println("Login failed. Incorrect email or password.");
            }
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // Method to handle registration flow
    private static void registerUserFlow(Scanner scanner) {
        System.out.print("Enter your email: ");
        String userEmail = scanner.nextLine().trim(); // Get user input for email

        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/HealthTrackingSystem", "root", "Kasthu@123")) {
            if (!checkUserExists(con, userEmail)) {
                System.out.println("User does not exist. Registering...");

                // Prompt for additional user details
                System.out.print("Enter your name: ");
                String userName = scanner.nextLine().trim();

                System.out.print("Enter your age: ");
                int userAge = scanner.nextInt();
                scanner.nextLine(); // Consume newline character after nextInt()

                System.out.print("Enter your phone number: ");
                String userPhoneNumber = scanner.nextLine().trim();

                System.out.print("Enter your password: ");
                String userPassword = scanner.nextLine().trim();

                System.out.print("Enter your weight (in kg): ");
                int userWeight = scanner.nextInt();
                scanner.nextLine(); // Consume newline character after nextInt()

                System.out.print("Enter your height (in cm): ");
                int userHeight = scanner.nextInt();
                scanner.nextLine(); // Consume newline character after nextInt()

                int userBMI = calculateBMI(userWeight, userHeight);

                // Register the user in the database
                registerUser(con, userName, userEmail, userAge, userPhoneNumber, userPassword, userWeight, userHeight, userBMI);
                System.out.println("Registration successful. Please proceed to login.");

                // After registration, update fitness status
                FitnessLevelManager.updateFitnessStatus(con, userEmail);
                //Display HealthObservation type to the user

            } else {
                System.out.println("Account already exists. Proceeding to login.");
            }
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // Method to check if user already exists in the database
    private static boolean checkUserExists(Connection con, String userEmail) throws SQLException {
        String checkQuery = "SELECT * FROM Users WHERE userEmail = ?";
        try (PreparedStatement pstmt = con.prepareStatement(checkQuery)) {
            pstmt.setString(1, userEmail);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next(); // true if user exists, false otherwise
            }
        }
    }

    // Method to register a new user in the database
    private static void registerUser(Connection con, String userName, String userEmail, int userAge, String userPhoneNumber, String userPassword, int userWeight, int userHeight, int userBMI) throws SQLException {
        String insertQuery = "INSERT INTO Users (userName, userEmail, userAge, userPhoneNumber, userPassword, userWeight, userHeight, userBMI) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = con.prepareStatement(insertQuery)) {
            pstmt.setString(1, userName);
            pstmt.setString(2, userEmail);
            pstmt.setInt(3, userAge);
            pstmt.setString(4, userPhoneNumber);
            pstmt.setString(5, userPassword);
            pstmt.setInt(6, userWeight);
            pstmt.setInt(7, userHeight);
            pstmt.setInt(8, userBMI);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("User inserted successfully.");
            } else {
                System.out.println("Failed to insert user.");
            }
        }
    }

    // Method to login user and check if email and password match
    private static boolean loginUser(Connection con, String userEmail, String userPassword) throws SQLException {
        String checkQuery = "SELECT * FROM Users WHERE userEmail = ? AND userPassword = ?";
        try (PreparedStatement pstmt = con.prepareStatement(checkQuery)) {
            pstmt.setString(1, userEmail);
            pstmt.setString(2, userPassword);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next(); // true if login successful (user exists with given email and password), false otherwise
            }
        }
    }

    // Method to calculate BMI
    private static int calculateBMI(int weight, int height) {
        double heightInMeters = height / 100.0; // Converting height to meters assuming input in centimeters
        double bmi = weight / (heightInMeters * heightInMeters);
        return (int) bmi; // Return BMI as an integer
    }

    // Method to display fitness level for the logged-in user
    private static void displayFitnessLevel(Connection con, String userEmail) throws SQLException {
        String selectQuery = "SELECT FitnessStatus FROM FitnessLevel WHERE UserId = ?";
        try (PreparedStatement pstmt = con.prepareStatement(selectQuery)) {
            pstmt.setInt(1, FitnessLevelManager.getUserIdByEmail(con, userEmail));
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String fitnessStatus = rs.getString("FitnessStatus");
                    System.out.println("Your fitness level: " + fitnessStatus);
                } else {
                    System.out.println("Fitness level not available.");
                }
            }
        }
    }
   
}
