package org.com.librarysystem.core;

import org.com.librarysystem.enums.BookType;
// We no longer need to import Observer
// import org.com.librarysystem.patterns.observer.Observer;

import java.util.List;
import java.util.ArrayList;

public class Book {
    private String isbn;
    private String title;
    private String author;
    private int publicationYear;
    private BookType type;


    public Book(String isbn, String title, String author, int publicationYear, BookType type) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.publicationYear = publicationYear;
        this.type = type;
        // No need to initialize observer list
    }

    // --- Getters and Setters ---

    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }
    public int getPublicationYear() { return publicationYear; }
    public void setPublicationYear(int publicationYear) { this.publicationYear = publicationYear; }
    public BookType getType() { return type; }
    public void setType(BookType type) { this.type = type; }

    // --- 'addObserver', 'removeObserver', and 'notifyObservers' METHODS REMOVED ---
}