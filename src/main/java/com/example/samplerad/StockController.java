package com.example.samplerad;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.event.ActionEvent;
import java.sql.*;

public class StockController {
    @FXML private TextField stockIdField;
    @FXML private TextField supplierIdField;
    @FXML private TextField bookIdField;
    @FXML private TextField authorNameField;
    @FXML private TextField bookNameField;
    @FXML private TextField marketprice;
    @FXML private TextField sellingprice;
    @FXML private TextField quantity;

    @FXML private TableView<Stock> stockTableView;
    @FXML private TableColumn<Stock, String> stockIdColumn;
    @FXML private TableColumn<Stock, String> supplierIdColumn;
    @FXML private TableColumn<Stock, String> bookIdColumn;
    @FXML private TableColumn<Stock, String> authorNameColumn;
    @FXML private TableColumn<Stock, String> bookNameColumn;
    @FXML private TableColumn<Stock, Integer> marketpricecolumn;
    @FXML private TableColumn<Stock, Integer> sellingpricecolumn;
    @FXML private TableColumn<Stock, Integer> quantitycolumn;

    private final ObservableList<Stock> stockList = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        // Initialize columns
        stockIdColumn.setCellValueFactory(new PropertyValueFactory<>("stockId"));
        supplierIdColumn.setCellValueFactory(new PropertyValueFactory<>("supplierId"));
        bookIdColumn.setCellValueFactory(new PropertyValueFactory<>("bookId"));
        authorNameColumn.setCellValueFactory(new PropertyValueFactory<>("authorName"));
        bookNameColumn.setCellValueFactory(new PropertyValueFactory<>("bookName"));
        marketpricecolumn.setCellValueFactory(new PropertyValueFactory<>("marketprice"));
        sellingpricecolumn.setCellValueFactory(new PropertyValueFactory<>("sellingprice"));
        quantitycolumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        // Set table items
        stockTableView.setItems(stockList);

        // Add selection listener
        stockTableView.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> {
                    if (newSelection != null) {
                        showStockDetails(newSelection);
                    }
                });

        // Load initial data
        loadStockData();
    }

    @FXML
    private void handleInsert(ActionEvent event) {
        if (!validateInputs()) {
            return;
        }

        String query = "INSERT INTO stock_records (stock_id, supplier_id, book_id, " +
                "author_name, book_name, market_price, selling_price, quantity) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, stockIdField.getText());
            pstmt.setString(2, supplierIdField.getText());
            pstmt.setString(3, bookIdField.getText());
            pstmt.setString(4, authorNameField.getText());
            pstmt.setString(5, bookNameField.getText());
            pstmt.setInt(6, Integer.parseInt(marketprice.getText()));
            pstmt.setInt(7, Integer.parseInt(sellingprice.getText()));
            pstmt.setInt(8, Integer.parseInt(quantity.getText()));

            int result = pstmt.executeUpdate();
            if (result > 0) {
                stockList.add(new Stock(
                        stockIdField.getText(),
                        supplierIdField.getText(),
                        bookIdField.getText(),
                        authorNameField.getText(),
                        bookNameField.getText(),
                        Integer.parseInt(marketprice.getText()),
                        Integer.parseInt(sellingprice.getText()),
                        Integer.parseInt(quantity.getText())
                ));
                AlertUtils.showInfo("Success", "Stock record added successfully");
                clearFields();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            AlertUtils.showError("Error", "Could not insert stock record");
        }
    }

    @FXML
    private void handleUpdate(ActionEvent event) {
        if (!validateInputs()) {
            return;
        }

        String query = "UPDATE stock_records SET supplier_id = ?, book_id = ?, " +
                "author_name = ?, book_name = ?, market_price = ?, " +
                "selling_price = ?, quantity = ? WHERE stock_id = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, supplierIdField.getText());
            pstmt.setString(2, bookIdField.getText());
            pstmt.setString(3, authorNameField.getText());
            pstmt.setString(4, bookNameField.getText());
            pstmt.setInt(5, Integer.parseInt(marketprice.getText()));
            pstmt.setInt(6, Integer.parseInt(sellingprice.getText()));
            pstmt.setInt(7, Integer.parseInt(quantity.getText()));
            pstmt.setString(8, stockIdField.getText());

            int result = pstmt.executeUpdate();
            if (result > 0) {
                loadStockData(); // Refresh table
                AlertUtils.showInfo("Success", "Stock record updated successfully");
                clearFields();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            AlertUtils.showError("Error", "Could not update stock record");
        }
    }

    @FXML
    private void handleDelete(ActionEvent event) {
        if (stockIdField.getText().isEmpty()) {
            AlertUtils.showError("Error", "Please select a record to delete");
            return;
        }

        if (!AlertUtils.showConfirmation("Confirm Delete",
                "Are you sure you want to delete this stock record?")) {
            return;
        }

        String query = "DELETE FROM stock_records WHERE stock_id = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, stockIdField.getText());

            int result = pstmt.executeUpdate();
            if (result > 0) {
                stockList.removeIf(stock ->
                        stock.getStockId().equals(stockIdField.getText()));
                AlertUtils.showInfo("Success", "Stock record deleted successfully");
                clearFields();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            AlertUtils.showError("Error", "Could not delete stock record");
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

    private void loadStockData() {
        String query = "SELECT * FROM stock_records";
        stockList.clear();

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                stockList.add(new Stock(
                        rs.getString("stock_id"),
                        rs.getString("supplier_id"),
                        rs.getString("book_id"),
                        rs.getString("author_name"),
                        rs.getString("book_name"),
                        rs.getInt("market_price"),
                        rs.getInt("selling_price"),
                        rs.getInt("quantity")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            AlertUtils.showError("Error", "Could not load stock data");
        }
    }

    private void showStockDetails(Stock stock) {
        stockIdField.setText(stock.getStockId());
        supplierIdField.setText(stock.getSupplierId());
        bookIdField.setText(stock.getBookId());
        authorNameField.setText(stock.getAuthorName());
        bookNameField.setText(stock.getBookName());
        marketprice.setText(String.valueOf(stock.getMarketprice()));
        sellingprice.setText(String.valueOf(stock.getSellingprice()));
        quantity.setText(String.valueOf(stock.getQuantity()));
    }

    private void clearFields() {
        stockIdField.clear();
        supplierIdField.clear();
        bookIdField.clear();
        authorNameField.clear();
        bookNameField.clear();
        marketprice.clear();
        sellingprice.clear();
        quantity.clear();
    }

    private boolean validateInputs() {
        if (ValidationUtils.isNullOrEmpty(
                stockIdField.getText(),
                supplierIdField.getText(),
                bookIdField.getText(),
                authorNameField.getText(),
                bookNameField.getText(),
                marketprice.getText(),
                sellingprice.getText(),
                quantity.getText())) {
            AlertUtils.showError("Validation Error", "Please fill in all fields");
            return false;
        }

        if (!ValidationUtils.isValidPrice(marketprice.getText())) {
            AlertUtils.showError("Validation Error", "Invalid market price");
            return false;
        }

        return true;
    }
}
