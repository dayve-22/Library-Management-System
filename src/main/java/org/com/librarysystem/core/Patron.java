package org.com.librarysystem.core;

import java.util.List;

public class Patron {
    private String patronId;
    private String name;
    private String email;
    private List<LendingRecord> borrowingHistory;
    private List<String> notifications; // For reservation alerts

    // ... Constructors, Getters/Setters ...

    public Patron(String patronId, String name, String email) {
        this.patronId = patronId;
        this.name = name;
        this.email = email;
    }

    public String getPatronId() {
        return patronId;
    }

    public void setPatronId(String patronId) {
        this.patronId = patronId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<LendingRecord> getBorrowingHistory() {
        return borrowingHistory;
    }

    public void setBorrowingHistory(List<LendingRecord> borrowingHistory) {
        this.borrowingHistory = borrowingHistory;
    }

    public List<String> getNotifications() {
        return notifications;
    }

    public void setNotifications(List<String> notifications) {
        this.notifications = notifications;
    }

    public void addNotification(String message) {
        this.notifications.add(message);
    }
}
