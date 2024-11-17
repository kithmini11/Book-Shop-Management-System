package com.example.samplerad;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.event.ActionEvent;
import java.sql.*;

public class SupplierController {
    @FXML private TextField supplierIdField;
    @FXML private TextField nameField;
    @FXML private TextField addressField;
    @FXML private TextField emailField;
    @FXML private TextField phoneField;

    @FXML private TableView<Supplier> supplierTableView;
    @FXML private TableColumn<Supplier, String> supplierIdColumn;
    @FXML private TableColumn<Supplier, String> nameColumn;
    @FXML private TableColumn<Supplier, String> addressColumn;
    @FXML private TableColumn<Supplier, String> emailColumn;
    @FXML private TableColumn<Supplier, String> phoneColumn;

    private final ObservableList<Supplier> supplierList = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        // Initialize columns
        supplierIdColumn.setCellValueFactory(new PropertyValueFactory<>("supplierId"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));

        // Set table items
        supplierTableView.setItems(supplierList);

        // Add selection listener
        supplierTableView.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> {
                    if (newSelection != null) {
                        showSupplierDetails(newSelection);
                    }
                });

        // Load initial data
        loadSupplierData();
    }

    @FXML
    private void handleInsert(ActionEvent event) {
        if (!validateInputs()) {
            return;
        }

        String query = "INSERT INTO supplier_records (supplier_id, supplier_name, " +
                "supplier_address, supplier_email, supplier_phone) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, supplierIdField.getText());
            pstmt.setString(2, nameField.getText());
            pstmt.setString(3, addressField.getText());
            pstmt.setString(4, emailField.getText());
            pstmt.setString(5, phoneField.getText());

            int result = pstmt.executeUpdate();
            if (result > 0) {
                supplierList.add(new Supplier(
                        supplierIdField.getText(),
                        nameField.getText(),
                        addressField.getText(),
                        emailField.getText(),
                        phoneField.getText()
                ));
                AlertUtils.showInfo("Success", "Supplier record added successfully");
                clearFields();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            AlertUtils.showError("Error", "Could not insert supplier record");
        }
    }

    @FXML
    private void handleUpdate(ActionEvent event) {
        if (!validateInputs()) {
            return;
        }

        String query = "UPDATE supplier_records SET supplier_name = ?, " +
                "supplier_address = ?, supplier_email = ?, supplier_phone = ? " +
                "WHERE supplier_id = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, nameField.getText());
            pstmt.setString(2, addressField.getText());
            pstmt.setString(3, emailField.getText());
            pstmt.setString(4, phoneField.getText());
            pstmt.setString(5, supplierIdField.getText());

            int result = pstmt.executeUpdate();
            if (result > 0) {
                loadSupplierData(); // Refresh table
                AlertUtils.showInfo("Success", "Supplier record updated successfully");
                clearFields();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            AlertUtils.showError("Error", "Could not update supplier record");
        }
    }

    @FXML
    private void handleDelete(ActionEvent event) {
        if (supplierIdField.getText().isEmpty()) {
            AlertUtils.showError("Error", "Please select a supplier to delete");
            return;
        }

        if (!AlertUtils.showConfirmation("Confirm Delete",
                "Are you sure you want to delete this supplier?")) {
            return;
        }

        String query = "DELETE FROM supplier_records WHERE supplier_id = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, supplierIdField.getText());

            int result = pstmt.executeUpdate();
            if (result > 0) {
                supplierList.removeIf(supplier ->
                        supplier.getSupplierId().equals(supplierIdField.getText()));
                AlertUtils.showInfo("Success", "Supplier record deleted successfully");
                clearFields();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            AlertUtils.showError("Error", "Could not delete supplier record");
        }
    }

    @FXML
    private void handleback(ActionEvent event) {
        SceneNavigator.getInstance().navigateTo("Menu.fxml", event);
    }

    @FXML
    private void handleReset(ActionEvent event) {
        clearFields();
    }

    private void loadSupplierData() {
        String query = "SELECT * FROM supplier_records";
        supplierList.clear();

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                supplierList.add(new Supplier(
                        rs.getString("supplier_id"),
                        rs.getString("supplier_name"),
                        rs.getString("supplier_address"),
                        rs.getString("supplier_email"),
                        rs.getString("supplier_phone")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            AlertUtils.showError("Error", "Could not load supplier data");
        }
    }

    private void showSupplierDetails(Supplier supplier) {
        supplierIdField.setText(supplier.getSupplierId());
        nameField.setText(supplier.getName());
        addressField.setText(supplier.getAddress());
        emailField.setText(supplier.getEmail());
        phoneField.setText(supplier.getPhone());
    }

    private void clearFields() {
        supplierIdField.clear();
        nameField.clear();
        addressField.clear();
        emailField.clear();
        phoneField.clear();
    }

    private boolean validateInputs() {
        if (ValidationUtils.isNullOrEmpty(
                supplierIdField.getText(),
                nameField.getText(),
                addressField.getText(),
                emailField.getText(),
                phoneField.getText())) {
            AlertUtils.showError("Validation Error", "Please fill in all fields");
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