package com.example.samplerad;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.event.ActionEvent;
import java.sql.*;

public class CustomerController {
    @FXML private TextField customerIdField;
    @FXML private TextField nameField;
    @FXML private TextField addressField;
    @FXML private TextField phoneNumberField;
    @FXML private TextField emailField;
    @FXML private TextField loyaltyStatusField;
    @FXML private TableView<Customer> customerTableView;
    @FXML private TableColumn<Customer, String> customerIdColumn;
    @FXML private TableColumn<Customer, String> nameColumn;
    @FXML private TableColumn<Customer, String> addressColumn;
    @FXML private TableColumn<Customer, String> phoneNumberColumn;
    @FXML private TableColumn<Customer, String> emailColumn;
    @FXML private TableColumn<Customer, String> loyaltyStatusColumn;

    private final ObservableList<Customer> customerList = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        customerIdColumn.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        phoneNumberColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        loyaltyStatusColumn.setCellValueFactory(new PropertyValueFactory<>("loyaltyStatus"));

        customerTableView.setItems(customerList);

        customerTableView.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> {
                    if (newSelection != null) {
                        showCustomerDetails(newSelection);
                    }
                });

        loadCustomerData();
    }

    @FXML
    private void handleInsert(ActionEvent event) {
        if (!validateInputs()) {
            return;
        }

        String query = "INSERT INTO customer_records (name, address, phone_number, email, loyalty_status) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, nameField.getText());
            pstmt.setString(2, addressField.getText());
            pstmt.setString(3, phoneNumberField.getText());
            pstmt.setString(4, emailField.getText());
            pstmt.setString(5, loyaltyStatusField.getText());

            int result = pstmt.executeUpdate();
            if (result > 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    String customerId = rs.getString(1);
                    customerList.add(new Customer(
                            customerId,
                            nameField.getText(),
                            addressField.getText(),
                            phoneNumberField.getText(),
                            emailField.getText(),
                            loyaltyStatusField.getText()
                    ));
                    AlertUtils.showInfo("Success", "Customer added successfully");
                    clearFields();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            AlertUtils.showError("Error", "Could not insert customer record");
        }
    }

    @FXML
    private void handleUpdate(ActionEvent event) {
        if (customerIdField.getText().isEmpty()) {
            AlertUtils.showError("Error", "Please select a customer to update");
            return;
        }

        if (!validateInputs()) {
            return;
        }

        String query = "UPDATE customer_records SET name = ?, address = ?, " +
                "phone_number = ?, email = ?, loyalty_status = ? WHERE customer_id = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, nameField.getText());
            pstmt.setString(2, addressField.getText());
            pstmt.setString(3, phoneNumberField.getText());
            pstmt.setString(4, emailField.getText());
            pstmt.setString(5, loyaltyStatusField.getText());
            pstmt.setString(6, customerIdField.getText());

            int result = pstmt.executeUpdate();
            if (result > 0) {
                loadCustomerData();
                AlertUtils.showInfo("Success", "Customer updated successfully");
                clearFields();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            AlertUtils.showError("Error", "Could not update customer record");
        }
    }

    @FXML
    private void handleDelete(ActionEvent event) {
        if (customerIdField.getText().isEmpty()) {
            AlertUtils.showError("Error", "Please select a customer to delete");
            return;
        }

        if (!AlertUtils.showConfirmation("Confirm Delete",
                "Are you sure you want to delete this customer?")) {
            return;
        }

        String query = "DELETE FROM customer_records WHERE customer_id = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, customerIdField.getText());
            int result = pstmt.executeUpdate();
            if (result > 0) {
                customerList.removeIf(customer ->
                        customer.getCustomerId().equals(customerIdField.getText()));
                AlertUtils.showInfo("Success", "Customer deleted successfully");
                clearFields();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            AlertUtils.showError("Error", "Could not delete customer record");
        }
    }

    @FXML
    private void handleReset(ActionEvent event) {
        clearFields();
    }

    @FXML
    private void handleback(ActionEvent event) {
        SceneNavigator.getInstance().navigateTo("Menu.fxml", event);
    }

    private void loadCustomerData() {
        String query = "SELECT * FROM customer_records";
        customerList.clear();
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                customerList.add(new Customer(
                        rs.getString("customer_id"),
                        rs.getString("name"),
                        rs.getString("address"),
                        rs.getString("phone_number"),
                        rs.getString("email"),
                        rs.getString("loyalty_status")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            AlertUtils.showError("Error", "Could not load customer data");
        }
    }

    private void showCustomerDetails(Customer customer) {
        customerIdField.setText(customer.getCustomerId());
        nameField.setText(customer.getName());
        addressField.setText(customer.getAddress());
        phoneNumberField.setText(customer.getPhoneNumber());
        emailField.setText(customer.getEmail());
        loyaltyStatusField.setText(customer.getLoyaltyStatus());
    }

    private void clearFields() {
        customerIdField.clear();
        nameField.clear();
        addressField.clear();
        phoneNumberField.clear();
        emailField.clear();
        loyaltyStatusField.clear();
    }

    private boolean validateInputs() {
        if (ValidationUtils.isNullOrEmpty(
                nameField.getText(),
                addressField.getText(),
                phoneNumberField.getText(),
                emailField.getText(),
                loyaltyStatusField.getText())) {
            AlertUtils.showError("Validation Error", "Please fill in all fields");
            return false;
        }

        if (!ValidationUtils.isValidEmail(emailField.getText())) {
            AlertUtils.showError("Validation Error", "Please enter a valid email address");
            return false;
        }

        if (!ValidationUtils.isValidPhone(phoneNumberField.getText())) {
            AlertUtils.showError("Validation Error", "Please enter a valid phone number");
            return false;
        }

        return true;
    }
}