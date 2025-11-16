package org.com.librarysystem.core;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a library member (patron).
 * This is a core data-holding class.
 */
public class Patron {

    private String patronId;
    private String name;
    private String email;

    // This list will hold a permanent history of all loans
    private List<LendingRecord> borrowingHistory;

    // This list will hold unread notifications
    private List<String> notifications;

    /**
     * Constructor for creating a new patron.
     * This is where we initialize the lists!
     *
     * @param patronId The unique ID for the patron
     * @param name     The patron's full name
     * @param email    The patron's email address
     */
    public Patron(String patronId, String name, String email) {
        this.patronId = patronId;
        this.name = name;
        this.email = email;

        // --- THIS IS THE FIX ---
        // Initialize the lists as empty ArrayLists
        // so they are not null.
        this.borrowingHistory = new ArrayList<>();
        this.notifications = new ArrayList<>();
    }

    // --- Getters and Setters ---

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

    /**
     * Gets the patron's borrowing history.
     * @return A List of LendingRecords. (Will be empty, but not null)
     */
    public List<LendingRecord> getBorrowingHistory() {
        return borrowingHistory;
    }

    public void setBorrowingHistory(List<LendingRecord> borrowingHistory) {
        this.borrowingHistory = borrowingHistory;
    }

    /**
     * Gets the patron's unread notifications.
     * @return A List of strings. (Will be empty, but not null)
     */
    public List<String> getNotifications() {
        return notifications;
    }

    public void setNotifications(List<String> notifications) {
        this.notifications = notifications;
    }

    // --- Public Methods ---

    /**
     * Adds a new notification message to the patron's list.
     * This is called by the NotificationService.
     *
     * @param message The notification message
     */
    public void addNotification(String message) {
        // This check is good practice in case the list was null,
        // but our constructor fix prevents that.
        if (this.notifications == null) {
            this.notifications = new ArrayList<>();
        }
        this.notifications.add(message);
    }
}