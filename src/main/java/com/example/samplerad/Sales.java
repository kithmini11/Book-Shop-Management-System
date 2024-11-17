package com.example.samplerad;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Sales {
    private final SimpleStringProperty bookTitle;
    private final SimpleIntegerProperty quantity;
    private final SimpleIntegerProperty pricePerUnit;
    private final SimpleIntegerProperty totalAmount;
    private final SimpleStringProperty customerid;
    private final SimpleStringProperty stockid;

    public Sales(String bookTitle, int quantity, int pricePerUnit,
                  int totalAmount, String customerid, String stockid) {
        this.bookTitle = new SimpleStringProperty(bookTitle);
        this.quantity = new SimpleIntegerProperty(quantity);
        this.pricePerUnit = new SimpleIntegerProperty(pricePerUnit);
        this.totalAmount = new SimpleIntegerProperty(totalAmount);
        this.customerid = new SimpleStringProperty(customerid);
        this.stockid = new SimpleStringProperty(stockid);
    }

    // Getters
    public String getBookTitle() { return bookTitle.get(); }
    public int getQuantity() { return quantity.get(); }
    public int getPricePerUnit() { return pricePerUnit.get(); }
    public int getTotalAmount() { return totalAmount.get(); }
    public String getCustomerid() { return customerid.get(); }
    public String getStockid() { return stockid.get(); }

    // Property getters for data binding
    public SimpleStringProperty bookTitleProperty() { return bookTitle; }
    public SimpleIntegerProperty quantityProperty() { return quantity; }
    public SimpleIntegerProperty pricePerUnitProperty() { return pricePerUnit; }

    public SimpleIntegerProperty totalAmountProperty() { return totalAmount; }
    public SimpleStringProperty customeridProperty() { return customerid; }
    public SimpleStringProperty stockidProperty() { return stockid; }

    // Setters
    public void setBookTitle(String value) { bookTitle.set(value); }
    public void setQuantity(int value) {
        quantity.set(value);
        updateTotalAmount(); // Update total whenever quantity changes
    }
    public void setPricePerUnit(int value) {
        pricePerUnit.set(value);
        updateTotalAmount(); // Update total whenever price per unit changes
    }

    public void setCustomerid(String value) { customerid.set(value); }
    public void setStockid(String value) { stockid.set(value); }

    // Method to update total amount based on quantity and price per unit
    private void updateTotalAmount() {
        totalAmount.set(quantity.get() * pricePerUnit.get());
    }
}
