package org.com.librarysystem.patterns.strategy;

import org.com.librarysystem.core.Book;

import java.util.List;
import java.util.Map;

// Concrete Strategy 2
public class SearchByAuthorStrategy implements SearchStrategy {
    @Override
    public List<Book> search(String query, Map<String, Book> bookCatalog) {
        // ... similar logic for author ...
    }
}
