package com.example.samplerad;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.input.MouseEvent;
import javafx.collections.ObservableList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MenuController {
    @FXML private Pane salesManagementPane;
    @FXML private Pane inventoryManagementPane;
    @FXML private Pane supplierRecordsPane;
    @FXML private Pane customerRecordsPane;
    @FXML private Pane orders;
    @FXML private Pane userProfilePane;

    @FXML private TableView<Sales> salesTableView;
    @FXML private TableColumn<Sales, String> bookTitleColumn;
    @FXML private TableColumn<Sales, Integer> quantityColumn;
    @FXML private TableColumn<Sales, Integer> pricePerUnitColumn;
    @FXML private TableColumn<Sales, String> customerColumn;
    @FXML private TableColumn<Sales, Integer> totalAmountColumn;

    private final ObservableList<Sales> salesList = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        // Initialize columns
        bookTitleColumn.setCellValueFactory(new PropertyValueFactory<>("bookTitle"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        pricePerUnitColumn.setCellValueFactory(new PropertyValueFactory<>("pricePerUnit"));
        customerColumn.setCellValueFactory(new PropertyValueFactory<>("customerid"));
        totalAmountColumn.setCellValueFactory(new PropertyValueFactory<>("totalAmount"));

        // Set table items
        salesTableView.setItems(salesList);




        // Load initial data
        loadSalesData();
    }



    @FXML
    private void navigateToSalesManagement(MouseEvent event) {
        SceneNavigator.getInstance().navigateTo("Sales Management.fxml", event);
    }

    @FXML
    private void navigateToInventoryManagement(MouseEvent event) {
        SceneNavigator.getInstance().navigateTo("Stock Records.fxml", event);
    }

    @FXML
    private void navigateToSupplierRecords(MouseEvent event) {
        SceneNavigator.getInstance().navigateTo("Supplier Records.fxml", event);
    }

    @FXML
    private void navigateToCustomerRecords(MouseEvent event){
        SceneNavigator.getInstance().navigateTo("Customer Records.fxml", event);
    }

    @FXML
    private void navigateToOrders(MouseEvent event) {
        SceneNavigator.getInstance().navigateTo("Customer Records.fxml", event);
    }

    @FXML
    private void navigateToUserProfile(MouseEvent event) {
        SceneNavigator.getInstance().navigateTo("User Profile.fxml", event);
    }

    @FXML
    private void handleQuickAccessSupplier(MouseEvent event) {
        SceneNavigator.getInstance().navigateTo("Supplier Records.fxml", event);

    }

    @FXML
    private void handleQuickAccessCustomer(MouseEvent event) {
        SceneNavigator.getInstance().navigateTo("Customer Records.fxml", event);
    }

    @FXML
    private void handleQuickAccessInventory(MouseEvent event) {
        SceneNavigator.getInstance().navigateTo("Stock Records.fxml", event);
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

}