package org.com.librarysystem.patterns.strategy;

import org.com.librarysystem.core.Book;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * A concrete strategy for searching books by their author.
 * Implements the SearchStrategy interface.
 */
public class SearchByAuthorStrategy implements SearchStrategy {

    /**
     * Searches the provided book catalog for books whose author matches the query.
     * The search is case-insensitive and checks for partial matches (contains).
     *
     * @param query       The author name (or part of it) to search for.
     * @param bookCatalog The master map of all book titles, keyed by ISBN.
     * @return A List of Book objects that match the author query.
     */
    @Override
    public List<Book> search(String query, Map<String, Book> bookCatalog) {
        // Convert the query to lower case once for efficiency
        String lowerCaseQuery = query.toLowerCase();

        // Stream the values (all Book objects) from the catalog
        return bookCatalog.values().stream()
                .filter(book -> {
                    // Check for null author to prevent NullPointerException
                    if (book.getAuthor() == null) {
                        return false;
                    }
                    // Perform a case-insensitive "contains" search
                    return book.getAuthor().toLowerCase().contains(lowerCaseQuery);
                })
                .collect(Collectors.toList()); // Collect the matching books into a list
    }
}