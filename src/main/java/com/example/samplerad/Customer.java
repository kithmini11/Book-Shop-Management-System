package com.example.samplerad;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Customer {
    private final StringProperty customerId;
    private final StringProperty name;
    private final StringProperty address;
    private final StringProperty phoneNumber;
    private final StringProperty email;
    private final StringProperty loyaltyStatus;

    public Customer(String customerId, String name, String address,
                    String phoneNumber, String email, String loyaltyStatus) {
        this.customerId = new SimpleStringProperty(customerId);
        this.name = new SimpleStringProperty(name);
        this.address = new SimpleStringProperty(address);
        this.phoneNumber = new SimpleStringProperty(phoneNumber);
        this.email = new SimpleStringProperty(email);
        this.loyaltyStatus = new SimpleStringProperty(loyaltyStatus);
    }

    // Getters
    public String getCustomerId() { return customerId.get(); }
    public String getName() { return name.get(); }
    public String getAddress() { return address.get(); }
    public String getPhoneNumber() { return phoneNumber.get(); }
    public String getEmail() { return email.get(); }
    public String getLoyaltyStatus() { return loyaltyStatus.get(); }

    // Property getters
    public StringProperty customerIdProperty() { return customerId; }
    public StringProperty nameProperty() { return name; }
    public StringProperty addressProperty() { return address; }
    public StringProperty phoneNumberProperty() { return phoneNumber; }
    public StringProperty emailProperty() { return email; }
    public StringProperty loyaltyStatusProperty() { return loyaltyStatus; }

    // Setters
    public void setName(String name) { this.name.set(name); }
    public void setAddress(String address) { this.address.set(address); }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber.set(phoneNumber); }
    public void setEmail(String email) { this.email.set(email); }
    public void setLoyaltyStatus(String loyaltyStatus) { this.loyaltyStatus.set(loyaltyStatus); }
}