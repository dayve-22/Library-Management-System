package org.com.librarysystem.patterns.strategy;

import org.com.librarysystem.core.Book;

import java.util.List;
import java.util.Map;

// The Context class that uses a strategy
public class SearchService {
    private SearchStrategy strategy;

    // The client can set which strategy to use at runtime
    public void setStrategy(SearchStrategy strategy) {
        this.strategy = strategy;
    }

    public List<Book> executeSearch(String query, Map<String, Book> catalog) {
        if (strategy == null) {
            throw new IllegalStateException("Search strategy not set.");
        }
        return strategy.search(query, catalog);
    }
}
