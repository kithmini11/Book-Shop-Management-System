package com.example.samplerad;

import javafx.beans.property.SimpleStringProperty;

public class Supplier {
    private final SimpleStringProperty supplierId;
    private final SimpleStringProperty name;
    private final SimpleStringProperty address;
    private final SimpleStringProperty email;
    private final SimpleStringProperty phone;

    public Supplier(String supplierId, String name, String address,
                    String email, String phone) {
        this.supplierId = new SimpleStringProperty(supplierId);
        this.name = new SimpleStringProperty(name);
        this.address = new SimpleStringProperty(address);
        this.email = new SimpleStringProperty(email);
        this.phone = new SimpleStringProperty(phone);
    }

    // Getters
    public String getSupplierId() { return supplierId.get(); }
    public String getName() { return name.get(); }
    public String getAddress() { return address.get(); }
    public String getEmail() { return email.get(); }
    public String getPhone() { return phone.get(); }

    // Property getters
    public SimpleStringProperty supplierIdProperty() { return supplierId; }
    public SimpleStringProperty nameProperty() { return name; }
    public SimpleStringProperty addressProperty() { return address; }
    public SimpleStringProperty emailProperty() { return email; }
    public SimpleStringProperty phoneProperty() { return phone; }

    // Setters
    public void setName(String value) { name.set(value); }
    public void setAddress(String value) { address.set(value); }
    public void setEmail(String value) { email.set(value); }
    public void setPhone(String value) { phone.set(value); }
}