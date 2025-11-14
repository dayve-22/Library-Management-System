package org.com.librarysystem.patterns.facade;

// Core models
import org.com.librarysystem.core.Book;
import org.com.librarysystem.core.BookItem;
import org.com.librarysystem.core.Branch;
import org.com.librarysystem.core.Patron;
import org.com.librarysystem.enums.BookType;

// Services
import org.com.librarysystem.service.BookManagementService;
import org.com.librarysystem.service.LendingService;
import org.com.librarysystem.service.PatronManagementService;
import org.com.librarysystem.service.ReservationService;

// Patterns
import org.com.librarysystem.patterns.singleton.Logger;
import org.com.librarysystem.patterns.strategy.SearchService;
import org.com.librarysystem.patterns.strategy.SearchStrategy;

// Java utilities
import java.util.List;


public class LibraryFacade {

    // --- 1. Service References ---
    private final LendingService lendingService;
    private final BookManagementService bookManagementService;
    private final PatronManagementService patronManagementService;
    private final SearchService searchService;
    private final ReservationService reservationService;

    private final Logger logger = Logger.getInstance();

    public LibraryFacade(LendingService lendingService,
                         BookManagementService bookManagementService,
                         PatronManagementService patronManagementService,
                         SearchService searchService,
                         ReservationService reservationService) {
        this.lendingService = lendingService;
        this.bookManagementService = bookManagementService;
        this.patronManagementService = patronManagementService;
        this.searchService = searchService;
        this.reservationService = reservationService;
    }

    public Book addNewBook(String title, String author, String isbn, int year, BookType type) {
        logger.info("Facade: Adding new book title with ISBN " + isbn);
        // We assume the Book constructor exists and is public
        Book book = new Book(isbn, title, author, year, type);
        bookManagementService.addBook(book);
        return book;
    }


    public BookItem addBookItem(Book book, Branch branch) {
        logger.info("Facade: Adding new physical item for book " + book.getIsbn());
        return bookManagementService.addBookItem(book, branch);
    }


    public List<Book> searchBooks(String query, SearchStrategy strategy) {
        logger.info("Facade: Executing search with query '" + query + "'");
        // Set the strategy on the search service
        searchService.setStrategy(strategy);
        return searchService.executeSearch(query, bookManagementService.getBookCatalog());
    }

    // --- 4. Patron Management Methods ---


    public Patron addNewPatron(String name, String email) {
        logger.info("Facade: Registering new patron '" + name + "'");
        return patronManagementService.addPatron(name, email);
    }


    public void checkoutBook(String patronId, String barcode) {
        logger.info("Facade: Attempting checkout for patron " + patronId + " and item " + barcode);
        try {
            lendingService.checkoutBook(patronId, barcode);
            logger.info("Facade: Checkout successful.");
        } catch (Exception e) {
            logger.error("Facade: Checkout failed. " + e.getMessage());
            // Re-throw the exception so the 'Main' class can handle it
            throw e;
        }
    }


    public void returnBook(String barcode) {
        logger.info("Facade: Attempting return for item " + barcode);
        try {
            lendingService.returnBook(barcode);
            logger.info("Facade: Return successful.");
        } catch (Exception e) {
            logger.error("Facade: Return failed. " + e.getMessage());
            throw e;
        }
    }

    public void reserveBook(String patronId, String isbn) {
        logger.info("Facade: Attempting reservation for patron " + patronId + " and book " + isbn);
        try {
            // The facade's job is to find the *objects* the service needs
            Patron patron = patronManagementService.getPatronById(patronId);
            Book book = bookManagementService.getBookByIsbn(isbn);

            // Robustness checks
            if (patron == null) {
                throw new IllegalArgumentException("Patron not found with ID: " + patronId);
            }
            if (book == null) {
                throw new IllegalArgumentException("Book not found with ISBN: " + isbn);
            }

            reservationService.makeReservation(patron, book);
            logger.info("Facade: Reservation successful.");
        } catch (Exception e) {
            logger.error("Facade: Reservation failed. " + e.getMessage());
            throw e;
        }
    }
}
