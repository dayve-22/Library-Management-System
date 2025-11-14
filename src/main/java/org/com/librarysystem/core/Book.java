package org.com.librarysystem.core;

import org.com.librarysystem.enums.BookType;
import org.com.librarysystem.patterns.observer.Observer;

import java.util.ArrayList;
import java.util.List;

public class Book {
    private String isbn;
    private String title;
    private String author;
    private int publicationYear;
    private BookType type;

    // For Observer Pattern
    private transient List<Observer> observers = new ArrayList<>();

    // ... Constructors, Getters/Setters ...

    public Book(String isbn, String title, String author, int publicationYear, BookType type) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.publicationYear = publicationYear;
        this.type = type;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getPublicationYear() {
        return publicationYear;
    }

    public void setPublicationYear(int publicationYear) {
        this.publicationYear = publicationYear;
    }

    public BookType getType() {
        return type;
    }

    public void setType(BookType type) {
        this.type = type;
    }

    public List<Observer> getObservers() {
        return observers;
    }

    public void setObservers(List<Observer> observers) {
        this.observers = observers;
    }

    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    // Called by LendingService when a copy is returned
    public void notifyObservers() {
        for (Observer observer : observers) {
            observer.update(this); // Notify that 'this' book has an event
        }
    }
}
