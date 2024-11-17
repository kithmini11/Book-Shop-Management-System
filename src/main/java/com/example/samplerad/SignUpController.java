package com.example.samplerad;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import java.sql.*;

public class SignUpController {
    @FXML private TextField nameField;
    @FXML private TextField emailField;
    @FXML private TextField phoneField;
    @FXML private TextField passwordField;
    @FXML private TextField confirmPasswordField;
    @FXML private Button signUpButton;

    @FXML
    private void handleLoginNavigation(ActionEvent event) {
        SceneNavigator.getInstance().navigateTo("Login.fxml", event);
    }

    @FXML
    private void handleSignUp(ActionEvent event) {
        if (!validateInputs()) {
            return;
        }

        String query = "INSERT INTO users (full_name, email, phone_number, password, salt) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            // Generate salt and hash password
            String salt = PasswordUtils.generateSalt();
            String hashedPassword = PasswordUtils.hashPassword(passwordField.getText(), salt);

            pstmt.setString(1, nameField.getText());
            pstmt.setString(2, emailField.getText());
            pstmt.setString(3, phoneField.getText());
            pstmt.setString(4, hashedPassword);
            pstmt.setString(5, salt);

            int result = pstmt.executeUpdate();
            if (result > 0) {
                ResultSet generatedKeys = pstmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int userId = generatedKeys.getInt(1);
                    UserSession.getInstance().setUser(userId, nameField.getText(), emailField.getText(), phoneField.getText(), "user");
                }
                AlertUtils.showInfo("Success", "Account created successfully");
                SceneNavigator.getInstance().navigateTo("Login.fxml", event);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            AlertUtils.showError("Error", "Could not create account");
        }
    }

    private boolean validateInputs() {
        // Validate empty fields
        if (ValidationUtils.isNullOrEmpty(
                nameField.getText(),
                emailField.getText(),
                phoneField.getText(),
                passwordField.getText(),
                confirmPasswordField.getText())) {
            AlertUtils.showError("Validation Error", "Please fill in all fields");
            return false;
        }

        // Validate email format
        if (!ValidationUtils.isValidEmail(emailField.getText())) {
            AlertUtils.showError("Validation Error", "Please enter a valid email address");
            return false;
        }

        // Validate phone number
        if (!ValidationUtils.isValidPhone(phoneField.getText())) {
            AlertUtils.showError("Validation Error", "Please enter a valid 10-digit phone number");
            return false;
        }

        // Validate password
        if (!ValidationUtils.isValidPassword(passwordField.getText())) {
            AlertUtils.showError("Validation Error", "Password must be at least 8 characters long");
            return false;
        }

        // Check if passwords match
        if (!passwordField.getText().equals(confirmPasswordField.getText())) {
            AlertUtils.showError("Validation Error", "Passwords do not match");
            return false;
        }

        return true;
    }

    // Method to clear all fields
    @FXML
    private void clearFields() {
        nameField.clear();
        emailField.clear();
        phoneField.clear();
        passwordField.clear();
        confirmPasswordField.clear();
    }
}