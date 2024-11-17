package com.example.samplerad;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ProfileController {
    @FXML private Label nameLabel;
    @FXML private Label emailLabel;
    @FXML private Label phoneLabel;

    @FXML
    public void initialize() {
        UserSession session = UserSession.getInstance();
        nameLabel.setText(session.getFullName());
        emailLabel.setText(session.getEmail());
        phoneLabel.setText(session.getPhoneNumber());
    }
}