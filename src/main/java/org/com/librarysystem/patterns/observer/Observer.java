package org.com.librarysystem.patterns.observer;

import org.com.librarysystem.core.Book;

public interface Observer {
    void update(Book book);
}