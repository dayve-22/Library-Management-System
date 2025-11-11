package org.com.librarysystem; // Using the package from your file

// Import core models
import org.com.librarysystem.core.Book;
import org.com.librarysystem.core.BookItem;
import org.com.librarysystem.core.Patron;
import org.com.librarysystem.enums.BookType;

// Import services
import org.com.librarysystem.service.BookManagementService;
import org.com.librarysystem.service.LendingService;
import org.com.librarysystem.service.NotificationService;
import org.com.librarysystem.service.PatronManagementService;
import org.com.librarysystem.service.ReservationService;

// Import patterns
import org.com.librarysystem.patterns.facade.LibraryFacade;
import org.com.librarysystem.patterns.singleton.Logger;
import org.com.librarysystem.patterns.strategy.SearchByTitleStrategy;
import org.com.librarysystem.patterns.strategy.SearchService;

import java.util.List;

/**
 * Main driver class to run a demo of the Library Management System.
 */
public class Main {

    public static void main(String[] args) {
        // --- 1. SYSTEM SETUP (Manual Dependency Injection) ---
        // Get the single Logger instance
        Logger logger = Logger.getInstance();
        logger.info("Library System starting up...");

        // Initialize all individual services
        BookManagementService bookSvc = new BookManagementService();
        PatronManagementService patronSvc = new PatronManagementService();
        NotificationService notificationSvc = new NotificationService();

        // Inject dependencies: ReservationService needs NotificationService
        ReservationService reservationSvc = new ReservationService(notificationSvc);

        // Inject dependencies: LendingService needs all three
        LendingService lendingSvc = new LendingService(bookSvc, patronSvc, reservationSvc);

        // Initialize the SearchService
        SearchService searchSvc = new SearchService();

        // Initialize the master Facade, injecting all services
        // (This is the only object our 'main' method should talk to)
        LibraryFacade library = new LibraryFacade(
                lendingSvc,
                bookSvc,
                patronSvc,
                searchSvc,
                reservationSvc
        );

        logger.info("System setup complete. Starting demo...");

        // --- 2. ADDING BOOKS AND PATRONS ---
        System.out.println("\n--- 1. Adding Books and Patrons ---");

        // Add a book title
        Book duneBook = library.addNewBook("Dune", "Frank Herbert", "978-0441172719", 1965, BookType.REGULAR);

        // Add two physical copies of that book
        BookItem duneCopy1 = library.addBookItem(duneBook, null); // null branch = main
        BookItem duneCopy2 = library.addBookItem(duneBook, null);

        System.out.println("Added book: '" + duneBook.getTitle() + "' with 2 copies.");

        // Add patrons
        Patron patron1 = library.addNewPatron("Alice", "alice@example.com");
        Patron patron2 = library.addNewPatron("Bob", "bob@example.com");
        System.out.println("Added patron: " + patron1.getName());
        System.out.println("Added patron: " + patron2.getName());


        // --- 3. SEARCHING (Strategy Pattern) ---
        System.out.println("\n--- 2. Searching for a Book ---");
        List<Book> results = library.searchBooks("Dune", new SearchByTitleStrategy());
        System.out.println("Found " + results.size() + " book(s) with title 'Dune'.");
        System.out.println("Title: " + results.get(0).getTitle() + ", Author: " + results.get(0).getAuthor());


        // --- 4. CHECKOUT ---
        System.out.println("\n--- 3. Checking out a Book ---");
        System.out.println("Status of copy 1 before checkout: " + duneCopy1.getStatus());
        library.checkoutBook(patron1.getPatronId(), duneCopy1.getBarcode());
        System.out.println("Alice checks out " + duneCopy1.getBarcode());
        System.out.println("Status of copy 1 after checkout: " + duneCopy1.getStatus());


        // --- 5. RESERVATION (Observer Pattern Demo) ---
        System.out.println("\n--- 4. Reserving a Book ---");

        // Alice checks out the *last* copy
        library.checkoutBook(patron1.getPatronId(), duneCopy2.getBarcode());
        System.out.println("Alice checks out the last copy: " + duneCopy2.getBarcode());

        // Now, Bob tries to check one out, but can't.
        System.out.println("Bob tries to check out copy 1 (which Alice has)...");
        try {
            library.checkoutBook(patron2.getPatronId(), duneCopy1.getBarcode());
        } catch (IllegalStateException e) {
            logger.warn("Checkout failed (as expected): " + e.getMessage());
        }

        // So, Bob reserves the *book title*
        library.reserveBook(patron2.getPatronId(), duneBook.getIsbn());
        System.out.println("Bob reserves the book title '" + duneBook.getTitle() + "'.");


        // --- 6. RETURN (Triggers Observer) ---
        System.out.println("\n--- 5. Returning a Book (Triggers Reservation) ---");
        System.out.println("Alice returns copy 1: " + duneCopy1.getBarcode());
        library.returnBook(duneCopy1.getBarcode());

        // The return should have triggered the ReservationService (Observer)
        // to notify Bob and set the book's status to RESERVED.
        System.out.println("Status of returned copy 1: " + duneCopy1.getStatus()); // Should be RESERVED

        // Check Bob's notifications
        System.out.println("Bob's notifications: " + patron2.getNotifications().get(0));

        logger.info("Demo complete.");
    }
}

/**
 * NOTE: For this to work, your LibraryFacade will need a few new methods
 * that I'm assuming exist, like:
 * * public class LibraryFacade {
 * // ... other services
 * private ReservationService reservationService;
 * * // Constructor updated to accept ReservationService
 * public LibraryFacade(..., ReservationService rs) {
 * //...
 * this.reservationService = rs;
 * }
 * * // Helper to add a new book
 * public Book addNewBook(String title, String author, String isbn, int year, BookType type) {
 * Book book = new Book(isbn, title, author, year, type);
 * bookManagementService.addBook(book);
 * return book;
 * }
 * * // Helper to add a book item
 * public BookItem addBookItem(Book book, Branch branch) {
 * return bookManagementService.addBookItem(book, branch);
 * }
 * * // Helper to add a new patron
 * public Patron addNewPatron(String name, String email) {
 * return patronManagementService.addPatron(name, email);
 * }
 * * // Helper for search
 * public List<Book> searchBooks(String query, SearchStrategy strategy) {
 * searchService.setStrategy(strategy);
 * return searchService.executeSearch(query, bookManagementService.getBookCatalog());
 * }
 * * // Helper for reservation
 * public void reserveBook(String patronId, String isbn) {
 * Patron patron = patronManagementService.getPatronById(patronId);
 * Book book = bookManagementService.getBookByIsbn(isbn);
 * reservationService.makeReservation(patron, book);
 * }
 * }
 */