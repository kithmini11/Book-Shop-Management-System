package com.example.samplerad;

public class UserSession {
    private static UserSession instance;
    private int userId;
    private String fullName;
    private String email;
    private String phoneNumber;
    private String role;

    private UserSession() {}

    public static UserSession getInstance() {
        if (instance == null) {
            instance = new UserSession();
        }
        return instance;
    }

    public void setUser(int userId, String fullName, String email,String phoneNumber , String role) {
        this.userId = userId;
        this.fullName = fullName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.role = role;
    }

    // Add individual setters
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setRole(String role) {
        this.role = role;
    }

    // Getters
    public int getUserId() { return userId; }
    public String getFullName() { return fullName; }
    public String getEmail() { return email; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getRole() { return role; }

    public void clearSession() {
        userId = 0;
        fullName = null;
        email = null;
        phoneNumber = null;
        role = null;
    }

    public void logout() {
        clearSession();
        // Redirect to login page
    }
}