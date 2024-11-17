package com.example.samplerad;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.event.ActionEvent;
import java.io.File;
import java.io.FileInputStream;
import java.sql.*;

public class UserProfileController {
    @FXML private TextField nameField;
    @FXML private TextField emailField;
    @FXML private TextField phoneField;
    @FXML private PasswordField passwordField;
    @FXML private ImageView profileImageView;
    @FXML private Button chooseImageButton;
    @FXML private Button saveButton;
    @FXML private Button removeImageButton;
    @FXML private Button deleteAccountButton;
    @FXML private Button back;
    @FXML private Label name;
    @FXML private Label email;
    @FXML private Label telephone;
    @FXML private Button logout;

    private File selectedImageFile;
    private final UserSession userSession = UserSession.getInstance();

    @FXML
    private void initialize() {
        loadUserProfile();
    }

    private void loadUserProfile() {
        String query = "SELECT * FROM users WHERE user_id = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, userSession.getUserId());
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                nameField.setText(rs.getString("full_name"));
                emailField.setText(rs.getString("email"));
                phoneField.setText(rs.getString("phone_number"));
                passwordField.setText(rs.getString("password"));
                name.setText(rs.getString("full_name"));
                email.setText(rs.getString("email"));
                telephone.setText(rs.getString("phone_number"));

                byte[] imageData = rs.getBytes("profile_photo");
                if (imageData != null) {
                    Image image = new Image(new java.io.ByteArrayInputStream(imageData));
                    profileImageView.setImage(image);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            AlertUtils.showError("Error", "Could not load user profile");
        }
    }

    @FXML
    private void handleChooseImage(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Profile Image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );

        File file = fileChooser.showOpenDialog(chooseImageButton.getScene().getWindow());
        if (file != null) {
            selectedImageFile = file;
            Image image = new Image(file.toURI().toString());
            profileImageView.setImage(image);
        }
    }

    @FXML
    private void handleRemoveImage(ActionEvent event) {
        selectedImageFile = null;
        profileImageView.setImage(null);
    }

    @FXML
    private void handleSaveProfile(ActionEvent event) {
        if (!validateInputs()) {
            return;
        }

        String query = "UPDATE users SET full_name = ?, email = ?, phone_number = ?, password = ?, profile_photo = ? WHERE user_id = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, nameField.getText());
            pstmt.setString(2, emailField.getText());
            pstmt.setString(3, phoneField.getText());
            pstmt.setString(4, passwordField.getText());

            if (selectedImageFile != null) {
                FileInputStream fis = new FileInputStream(selectedImageFile);
                pstmt.setBinaryStream(5, fis, selectedImageFile.length());
            } else {
                pstmt.setNull(5, Types.BLOB);
            }

            pstmt.setInt(6, userSession.getUserId());

            int result = pstmt.executeUpdate();
            if (result > 0) {
                userSession.setFullName(nameField.getText());
                userSession.setEmail(emailField.getText());

                AlertUtils.showInfo("Success", "Profile updated successfully");
            }
        } catch (Exception e) {
            e.printStackTrace();
            AlertUtils.showError("Error", "Could not update profile");
        }
    }

    @FXML
    private void handleDeleteAccount(ActionEvent event) {
        if (!AlertUtils.showConfirmation("Delete Account",
                "Are you sure you want to delete your account? This action cannot be undone.")) {
            return;
        }

        String query = "DELETE FROM users WHERE user_id = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            int userId = userSession.getUserId();
            System.out.println("Attempting to delete account for user ID: " + userId); // Debug statement

            pstmt.setInt(1, userId);

            int result = pstmt.executeUpdate();
            if (result > 0) {
                AlertUtils.showInfo("Success", "Account deleted successfully");
                userSession.clearSession();
                SceneNavigator.getInstance().navigateTo("Login.fxml", event);
            } else {
                System.out.println("No account found for user ID: " + userId);
            }
        } catch (SQLException e) {
            System.err.println("SQL Exception: " + e.getMessage());
            AlertUtils.showError("Error", "Could not delete account");
        }
    }

    @FXML
    private void handleback(ActionEvent event) {
        SceneNavigator.getInstance().navigateTo("Menu.fxml", event);
    }

    @FXML private void handlelogout(ActionEvent event) {
        userSession.logout();
        SceneNavigator.getInstance().navigateTo("Login.fxml", event);
    }


    private boolean validateInputs() {
        if (ValidationUtils.isNullOrEmpty(
                nameField.getText(),
                emailField.getText(),
                phoneField.getText())) {
            AlertUtils.showError("Validation Error", "Please fill in all required fields");
            return false;
        }

        if (!ValidationUtils.isValidEmail(emailField.getText())) {
            AlertUtils.showError("Validation Error", "Please enter a valid email address");
            return false;
        }

        if (!ValidationUtils.isValidPhone(phoneField.getText())) {
            AlertUtils.showError("Validation Error", "Please enter a valid phone number");
            return false;
        }

        return true;
    }
}