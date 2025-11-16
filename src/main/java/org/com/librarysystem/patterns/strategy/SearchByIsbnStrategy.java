package org.com.librarysystem.patterns.strategy;

import org.com.librarysystem.core.Book;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * A concrete strategy for searching a book by its ISBN.
 * Implements the SearchStrategy interface.
 * <p>
 * This is the most efficient search strategy as it performs a direct
 * O(1) key lookup on the book catalog map.
 */
public class SearchByIsbnStrategy implements SearchStrategy {

    /**
     * Searches the provided book catalog for a book matching the given ISBN.
     *
     * @param query       The exact ISBN to search for.
     * @param bookCatalog The master map of all book titles, keyed by ISBN.
     * @return A List containing the single matching Book, or an empty list if not found.
     */
    @Override
    public List<Book> search(String query, Map<String, Book> bookCatalog) {

        // No need to stream or iterate. The map's key *is* the ISBN.
        Book foundBook = bookCatalog.get(query);

        if (foundBook != null) {
            // Found the book. Return an immutable list containing just this one book.
            return List.of(foundBook);
        } else {
            // No book found with this ISBN. Return an immutable empty list.
            return Collections.emptyList();
        }
    }
}