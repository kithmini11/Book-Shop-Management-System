package com.example.samplerad;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Stock {
    private final SimpleStringProperty stockId;
    private final SimpleStringProperty supplierId;
    private final SimpleStringProperty bookId;
    private final SimpleStringProperty authorName;
    private final SimpleStringProperty bookName;
    private final SimpleIntegerProperty marketprice;
    private final SimpleIntegerProperty sellingprice;
    private final SimpleIntegerProperty quantity;

    public Stock(String stockId, String supplierId, String bookId,
                 String authorName, String bookName, int marketprice, int sellingprice, int quantity) {
        this.stockId = new SimpleStringProperty(stockId);
        this.supplierId = new SimpleStringProperty(supplierId);
        this.bookId = new SimpleStringProperty(bookId);
        this.authorName = new SimpleStringProperty(authorName);
        this.bookName = new SimpleStringProperty(bookName);
        this.marketprice = new SimpleIntegerProperty(marketprice);
        this.sellingprice = new SimpleIntegerProperty(sellingprice);
        this.quantity = new SimpleIntegerProperty(quantity);
    }

    // Getters
    public String getStockId() { return stockId.get(); }
    public String getSupplierId() { return supplierId.get(); }
    public String getBookId() { return bookId.get(); }
    public String getAuthorName() { return authorName.get(); }
    public String getBookName() { return bookName.get(); }
    public int getMarketprice() { return marketprice.get(); }
    public int getSellingprice() { return sellingprice.get(); }
    public int getQuantity() { return quantity.get(); }

    // Property getters
    public SimpleStringProperty stockIdProperty() { return stockId; }
    public SimpleStringProperty supplierIdProperty() { return supplierId; }
    public SimpleStringProperty bookIdProperty() { return bookId; }
    public SimpleStringProperty authorNameProperty() { return authorName; }
    public SimpleStringProperty bookNameProperty() { return bookName; }
    public SimpleIntegerProperty marketpriceProperty() { return marketprice; }
    public SimpleIntegerProperty sellingpriceProperty() { return sellingprice; }
    public SimpleIntegerProperty quantityProperty() { return quantity; }

    // Setters
    public void setSupplierId(String value) { supplierId.set(value); }
    public void setBookId(String value) { bookId.set(value); }
    public void setAuthorName(String value) { authorName.set(value); }
    public void setBookName(String value) { bookName.set(value); }
    public void setMarketprice(int value) { marketprice.set(value); }
    public void setSellingprice(int value) { sellingprice.set(value); }
    public void setQuantity(int value) { quantity.set(value); }
}
