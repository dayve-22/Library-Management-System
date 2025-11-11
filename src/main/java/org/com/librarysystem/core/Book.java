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

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
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
