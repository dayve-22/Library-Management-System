package org.com.librarysystem.core;

import org.com.librarysystem.enums.BookStatus;

public class BookItem {
    private String barcode; // Unique ID for this copy
    private Book book; // Reference to the metadata
    private BookStatus status;
    private Branch currentBranch; // For multi-branch support

    // ... Constructors, Getters/Setters ...


    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public BookStatus getStatus() {
        return status;
    }

    public void setStatus(BookStatus status) {
        this.status = status;
    }

    public Branch getCurrentBranch() {
        return currentBranch;
    }

    public void setCurrentBranch(Branch currentBranch) {
        this.currentBranch = currentBranch;
    }
}
