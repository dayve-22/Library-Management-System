package org.com.librarysystem.core;

import java.time.LocalDate;

public class LendingRecord {
    private String recordId;
    private String bookItemBarcode;
    private String patronId;
    private LocalDate checkoutDate;
    private LocalDate dueDate;
    private LocalDate returnDate; // Null if not returned

    // ... Constructors, Getters/Setters ...


    public LendingRecord(String recordId, String bookItemBarcode, String patronId, LocalDate checkoutDate, LocalDate dueDate) {
        this.recordId = recordId;
        this.bookItemBarcode = bookItemBarcode;
        this.patronId = patronId;
        this.checkoutDate = checkoutDate;
        this.dueDate = dueDate;
    }

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    public String getBookItemBarcode() {
        return bookItemBarcode;
    }

    public void setBookItemBarcode(String bookItemBarcode) {
        this.bookItemBarcode = bookItemBarcode;
    }

    public String getPatronId() {
        return patronId;
    }

    public void setPatronId(String patronId) {
        this.patronId = patronId;
    }

    public LocalDate getCheckoutDate() {
        return checkoutDate;
    }

    public void setCheckoutDate(LocalDate checkoutDate) {
        this.checkoutDate = checkoutDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }
}
