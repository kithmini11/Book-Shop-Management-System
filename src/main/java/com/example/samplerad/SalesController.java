package com.example.samplerad;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.event.ActionEvent;
import java.sql.*;

public class SalesController {
    @FXML private TextField bookTitleField;
    @FXML private TextField quantityField;
    @FXML private TextField pricePerUnitField;
    @FXML private TextField stockidField;
    @FXML private TextField totalAmountField;
    @FXML private TextField customeridField;

    @FXML private TableView<Sales> salesTableView;
    @FXML private TableColumn<Sales, String> bookIDColumn;
    @FXML private TableColumn<Sales, Integer> quantityColumn;
    @FXML private TableColumn<Sales, Integer> pricePerUnitColumn;
    @FXML private TableColumn<Sales, String> customeridColumn;
    @FXML private TableColumn<Sales, Integer> totalAmountColumn;

    private final ObservableList<Sales> salesList = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        // Initialize columns
        bookIDColumn.setCellValueFactory(new PropertyValueFactory<>("bookTitle"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        pricePerUnitColumn.setCellValueFactory(new PropertyValueFactory<>("pricePerUnit"));
        customeridColumn.setCellValueFactory(new PropertyValueFactory<>("customerid"));
        totalAmountColumn.setCellValueFactory(new PropertyValueFactory<>("totalAmount"));

        // Set table items
        salesTableView.setItems(salesList);

        // Add listeners for automatic total calculation
        quantityField.textProperty().addListener((obs, oldVal, newVal) -> calculateTotal());
        pricePerUnitField.textProperty().addListener((obs, oldVal, newVal) -> calculateTotal());

        // Add selection listener
        salesTableView.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> {
                    if (newSelection != null) {
                        showSalesDetails(newSelection);
                    }
                });

        // Load initial data
        loadSalesData();
    }

    private void calculateTotal() {
        try {
            int quantity = Integer.parseInt(quantityField.getText());
            int price = Integer.parseInt(pricePerUnitField.getText());
            totalAmountField.setText(String.valueOf(quantity * price));
        } catch (NumberFormatException e) {
            totalAmountField.setText("0");
        }
    }

    @FXML
    private void handleInsert(ActionEvent event) {
        if (!validateInputs()) {
            return;
        }

        // Insert query for sales_records table
        String insertSalesQuery = "INSERT INTO sales_records (book_title, quantity, price_per_unit, total_amount, stock_id, customer_id) VALUES (?, ?, ?, ?, ?, ?)";

        // Update query for stock_records table to decrease quantity
        String updateStockQuery = "UPDATE stock_records SET quantity = quantity - ? WHERE book_id = ? AND stock_id = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection()) {
            // Start a transaction
            conn.setAutoCommit(false);

            // Insert the sales record
            try (PreparedStatement insertStmt = conn.prepareStatement(insertSalesQuery)) {
                int quantity = Integer.parseInt(quantityField.getText());
                int price = Integer.parseInt(pricePerUnitField.getText());
                int total = Integer.parseInt(totalAmountField.getText());

                insertStmt.setString(1, bookTitleField.getText());
                insertStmt.setInt(2, quantity);
                insertStmt.setInt(3, price);
                insertStmt.setInt(4, total);
                insertStmt.setString(5, stockidField.getText());
                insertStmt.setString(6, customeridField.getText());

                insertStmt.executeUpdate();
            }

            // Update the stock quantity
            try (PreparedStatement updateStmt = conn.prepareStatement(updateStockQuery)) {
                int quantity = Integer.parseInt(quantityField.getText());

                updateStmt.setInt(1, quantity);
                updateStmt.setString(2, bookTitleField.getText()); // Assuming bookTitleField contains the book ID for stock_records
                updateStmt.setString(3, stockidField.getText());

                updateStmt.executeUpdate();
            }

            // Commit transaction
            conn.commit();

            // Add to salesList and show success alert
            salesList.add(new Sales(
                    bookTitleField.getText(),
                    Integer.parseInt(quantityField.getText()),
                    Integer.parseInt(pricePerUnitField.getText()),
                    Integer.parseInt(totalAmountField.getText()),
                    customeridField.getText(),
                    stockidField.getText()
            ));

            AlertUtils.showInfo("Success", "Sales record added successfully, and stock updated.");
            clearFields();

        } catch (SQLException e) {
            e.printStackTrace();
            AlertUtils.showError("Error", "Could not insert sales record or update stock: " + e.getMessage());
        }
    }

    @FXML
    private void handleUpdate(ActionEvent event) {
        if (!validateInputs()) {
            return;
        }

        Sales selectedSales = salesTableView.getSelectionModel().getSelectedItem();
        if (selectedSales == null) {
            AlertUtils.showError("Error", "Please select a record to update");
            return;
        }

        String query = "UPDATE sales_records SET quantity = ?, price_per_unit = ?, " +
                "sales_person = ?, total_amount = ? WHERE book_title = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            int total = Integer.parseInt(totalAmountField.getText());
            int quantity = Integer.parseInt(quantityField.getText());
            int price = Integer.parseInt(pricePerUnitField.getText());

            pstmt.setInt(1, quantity);
            pstmt.setInt(2, price);
            pstmt.setString(3, customeridField.getText());
            pstmt.setInt(4, total);
            pstmt.setString(5, bookTitleField.getText());

            int result = pstmt.executeUpdate();
            if (result > 0) {
                loadSalesData(); // Refresh table
                AlertUtils.showInfo("Success", "Sales record updated successfully");
                clearFields();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            AlertUtils.showError("Error", "Could not update sales record");
        }
    }

    @FXML
    private void handleDelete(ActionEvent event) {
        Sales selectedSales = salesTableView.getSelectionModel().getSelectedItem();
        if (selectedSales == null) {
            AlertUtils.showError("Error", "Please select a record to delete");
            return;
        }

        if (!AlertUtils.showConfirmation("Confirm Delete",
                "Are you sure you want to delete this sales record?")) {
            return;
        }

        String query = "DELETE FROM sales_records WHERE book_title = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, selectedSales.getBookTitle());

            int result = pstmt.executeUpdate();
            if (result > 0) {
                salesList.remove(selectedSales);
                AlertUtils.showInfo("Success", "Sales record deleted successfully");
                clearFields();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            AlertUtils.showError("Error", "Could not delete sales record");
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

    private void loadSalesData() {
        String query = "SELECT book_title,quantity,price_per_unit,customer_id,total_amount ,stock_id FROM sales_records";
        salesList.clear();

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                salesList.add(new Sales(
                        rs.getString("book_title"),
                        rs.getInt("quantity"),
                        rs.getInt("price_per_unit"),
                        rs.getInt("total_amount"),
                        rs.getString("customer_id"),
                        rs.getString("stock_id")

                        ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            AlertUtils.showError("Error", "Could not load sales data");
        }
    }

    private void showSalesDetails(Sales sales) {
        bookTitleField.setText(sales.getBookTitle());
        quantityField.setText(String.valueOf(sales.getQuantity()));
        pricePerUnitField.setText(String.valueOf(sales.getPricePerUnit()));
        customeridField.setText(sales.getCustomerid());
        totalAmountField.setText(String.valueOf(sales.getTotalAmount()));
    }

    private void clearFields() {
        bookTitleField.clear();
        quantityField.clear();
        pricePerUnitField.clear();
        customeridField.clear();
        totalAmountField.clear();
        stockidField.clear();
    }

    private boolean validateInputs() {
        if (ValidationUtils.isNullOrEmpty(
                bookTitleField.getText(),
                quantityField.getText(),
                pricePerUnitField.getText(),
                customeridField.getText())) {
            AlertUtils.showError("Validation Error", "Please fill in all fields");
            return false;
        }

        try {
            int quantity = Integer.parseInt(quantityField.getText());
            if (quantity <= 0) {
                AlertUtils.showError("Validation Error", "Quantity must be greater than 0");
                return false;
            }
        } catch (NumberFormatException e) {
            AlertUtils.showError("Validation Error", "Invalid quantity format");
            return false;
        }

        try {
            int price = Integer.parseInt(pricePerUnitField.getText());
            if (price <= 0) {
                AlertUtils.showError("Validation Error", "Price must be greater than 0");
                return false;
            }
        } catch (NumberFormatException e) {
            AlertUtils.showError("Validation Error", "Invalid price format");
            return false;
        }

        return true;
    }
}
