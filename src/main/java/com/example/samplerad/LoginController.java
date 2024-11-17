package com.example.samplerad;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.event.ActionEvent;
import java.sql.*;


public class LoginController {
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private CheckBox rememberMe;
    @FXML private Button loginButton;

    @FXML
    public void initialize() {
        loginButton.setOnAction(this::handleLogin);
    }

    @FXML
    private void handleSignUpNavigation(ActionEvent event) {
        SceneNavigator.getInstance().navigateTo("SignUp.fxml", event);
    }

    @FXML
    private void handleForgotPassword(ActionEvent event) {
        // Implement forgot password functionality
    }

    @FXML
    private void handleLogin(ActionEvent event) {
        if (!validateInputs()) {
            return;
        }

        String email = emailField.getText();
        String password = passwordField.getText();

        // First, get the salt and hashed password from database
        String query = "SELECT * FROM users WHERE email = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                // Get the stored salt and hashed password
                String storedSalt = rs.getString("salt");
                String storedHashedPassword = rs.getString("password");

                // Hash the input password with the stored salt
                String hashedInputPassword = PasswordUtils.hashPassword(password, storedSalt);

                // Compare the hashed input password with stored hashed password
                if (hashedInputPassword.equals(storedHashedPassword)) {
                    // Login successful
                    UserSession.getInstance().setUser(
                            rs.getInt("user_id"),
                            rs.getString("full_name"),
                            email,
                            "USER",
                            "additionalArgument"
                    );

                    clearFields();

                    // Navigate to Menu.fxml
                    try {
                        SceneNavigator.getInstance().navigateTo("Menu.fxml", event);
                    } catch (Exception e) {
                        e.printStackTrace();
                        AlertUtils.showError("Navigation Error", "Could not load Menu page");
                    }
                } else {
                    AlertUtils.showError("Login Failed", "Invalid email or password");
                }
            } else {
                AlertUtils.showError("Login Failed", "Invalid email or password");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            AlertUtils.showError("Database Error", "Could not connect to database");
        }
    }

    private boolean validateInputs() {
        if (ValidationUtils.isNullOrEmpty(emailField.getText(), passwordField.getText())) {
            AlertUtils.showError("Validation Error", "Please fill in all fields");
            return false;
        }

        if (!ValidationUtils.isValidEmail(emailField.getText())) {
            AlertUtils.showError("Validation Error", "Please enter a valid email address");
            return false;
        }

        return true;
    }

    private void clearFields() {
        emailField.clear();
        passwordField.clear();
        rememberMe.setSelected(false);
    }
}