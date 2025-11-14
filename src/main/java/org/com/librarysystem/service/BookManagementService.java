package org.com.librarysystem.service;

import org.com.librarysystem.core.Book;
import org.com.librarysystem.core.BookItem;
import org.com.librarysystem.core.Branch;
import org.com.librarysystem.enums.BookStatus;
import org.com.librarysystem.patterns.singleton.Logger;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Manages the master catalog of book titles and the inventory of physical book items.
 * This class can be treated as a Singleton or as a single instance managed by the Facade.
 */
public class BookManagementService {

    // Master catalog of all book titles
    private final Map<String, Book> bookCatalog; // Key: ISBN

    // Master list of all physical items
    private final Map<String, BookItem> bookItems; // Key: Barcode

    private final Logger logger = Logger.getInstance();

    /**
     * Initializes the Book Management Service.
     */
    public BookManagementService() {
        this.bookCatalog = new HashMap<>();
        this.bookItems = new HashMap<>();
        logger.info("BookManagementService initialized.");
    }

    /**
     * Adds a new book title (metadata) to the master catalog.
     *
     * @param book The Book object to add.
     */
    public void addBook(Book book) {
        if (book == null || book.getIsbn() == null) {
            logger.warn("Attempted to add a null book or book with no ISBN.");
            throw new IllegalArgumentException("Book and ISBN must not be null.");
        }

        if (bookCatalog.containsKey(book.getIsbn())) {
            logger.warn("Attempted to add duplicate book with ISBN: " + book.getIsbn());
            throw new IllegalStateException("Book with this ISBN already exists.");
        }

        bookCatalog.put(book.getIsbn(), book);
        logger.info("Added new book title to catalog: " + book.getTitle());
    }

    /**
     * Creates and adds a new physical copy (BookItem) of an existing book title.
     *
     * @param book The book title this item is a copy of.
     * @param branch The branch where this item will be located (can be null).
     * @return The newly created BookItem.
     */
    public BookItem addBookItem(Book book, Branch branch) {
        if (book == null || !bookCatalog.containsKey(book.getIsbn())) {
            logger.error("Attempted to add item for a book not in the catalog. ISBN: " + (book != null ? book.getIsbn() : "null"));
            throw new IllegalArgumentException("Book must exist in the catalog before adding an item.");
        }

        // Generate a unique barcode
        String barcode = "bc-" + UUID.randomUUID().toString().substring(0, 8);

        // We assume BookItem has a constructor:
        // (barcode, book, status, branch)
        BookItem newItem = new BookItem(barcode, book, BookStatus.AVAILABLE, branch);

        bookItems.put(barcode, newItem);

        // (Optional) If we are tracking inventory per-branch
        if (branch != null) {
            branch.addBookItem(newItem);
        }

        logger.info("Added new item (copy) for book '" + book.getTitle() + "' with barcode " + barcode);
        return newItem;
    }

    /**
     * Retrieves a book title by its ISBN.
     *
     * @param isbn The ISBN of the book.
     * @return The Book object, or null if not found.
     */
    public Book getBookByIsbn(String isbn) {
        return bookCatalog.get(isbn);
    }

    /**
     * Retrieves a specific physical book item by its unique barcode.
     *
     * @param barcode The barcode of the item.
     * @return The BookItem object, or null if not found.
     */
    public BookItem getBookItemByBarcode(String barcode) {
        return bookItems.get(barcode);
    }

    /**
     * Removes a physical book item from the inventory (e.g., if lost or damaged).
     *
     * @param barcode The barcode of the item to remove.
     * @return true if the item was removed, false otherwise.
     */
    public boolean removeBookItem(String barcode) {
        BookItem item = bookItems.remove(barcode);

        if (item != null) {
            // (Optional) Remove from branch inventory
            if (item.getCurrentBranch() != null) {
                item.getCurrentBranch().removeBookItem(item);
            }
            logger.info("Removed book item: " + barcode);
            return true;
        } else {
            logger.warn("Could not remove book item: No item found with barcode " + barcode);
            return false;
        }
    }

    /**
     * Updates the metadata of an existing book title.
     *
     * @param updatedBook The book object containing the new information.
     */
    public void updateBook(Book updatedBook) {
        if (updatedBook == null || updatedBook.getIsbn() == null) {
            throw new IllegalArgumentException("Book and ISBN must not be null.");
        }

        if (!bookCatalog.containsKey(updatedBook.getIsbn())) {
            logger.warn("Attempted to update a book that doesn't exist: " + updatedBook.getIsbn());
            throw new IllegalArgumentException("No book found with this ISBN to update.");
        }

        bookCatalog.put(updatedBook.getIsbn(), updatedBook);
        logger.info("Updated book metadata for ISBN: " + updatedBook.getIsbn());
    }

    /**
     * Returns an unmodifiable view of the entire book catalog.
     * This is useful for the SearchService.
     *
     * @return An unmodifiable Map of the book catalog.
     */
    public Map<String, Book> getBookCatalog() {
        return Collections.unmodifiableMap(bookCatalog);
    }

    /**
     * Returns an unmodifiable view of all physical book items.
     *
     * @return An unmodifiable Map of all book items.
     */
    public Map<String, BookItem> getBookItems() {
        return Collections.unmodifiableMap(bookItems);
    }
}