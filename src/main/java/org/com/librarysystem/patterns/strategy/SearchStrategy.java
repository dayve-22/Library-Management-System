package org.com.librarysystem.patterns.strategy;

import org.com.librarysystem.core.Book;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public interface SearchStrategy {
    List<Book> search(String query, Map<String, Book> bookCatalog);
}






