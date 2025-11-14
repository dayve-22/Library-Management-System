package org.com.librarysystem.patterns.strategy;

import org.com.librarysystem.core.Book;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

// Concrete Strategy 1
public class SearchByTitleStrategy implements SearchStrategy {
    @Override
    public List<Book> search(String query, Map<String, Book> bookCatalog) {
        return bookCatalog.values().stream()
                .filter(book -> book.getTitle().toLowerCase().contains(query.toLowerCase()))
                .collect(Collectors.toList());
    }
}