package org.com.librarysystem.service;

import org.com.librarysystem.core.Book;
import org.com.librarysystem.core.BookItem;
import org.com.librarysystem.core.LendingRecord;
import org.com.librarysystem.core.Patron;
import org.com.librarysystem.enums.BookStatus;
import org.com.librarysystem.enums.BookType;
import org.com.librarysystem.patterns.singleton.Logger;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class LendingService {

    private final BookManagementService bookSvc;
    private final PatronManagementService patronSvc;
    private final ReservationService reservationSvc; // To notify on return
    private final List<LendingRecord> activeLoans;

    private static final Logger logger = Logger.getInstance(); // Logging
    private static final int STANDARD_LOAN_DAYS = 30;

    /**
     * Constructor uses Dependency Injection.
     *
     * @param b BookManagementService instance
     * @param p PatronManagementService instance
     * @param r ReservationService instance
     */
    public LendingService(BookManagementService b, PatronManagementService p, ReservationService r) {
        this.bookSvc = b;
        this.patronSvc = p;
        this.reservationSvc = r;
        this.activeLoans = new ArrayList<>();
    }

    /**
     * Checks out a book item to a patron.
     *
     * @param patronId The ID of the patron
     * @param barcode  The barcode of the specific book item
     */
    public void checkoutBook(String patronId, String barcode) {
        BookItem item = bookSvc.getBookItemByBarcode(barcode);
        if (item == null) {
            logger.error("Checkout failed: No book item found with barcode " + barcode);
            throw new IllegalArgumentException("No book item found with barcode " + barcode);
        }

        Patron patron = patronSvc.getPatronById(patronId);
        if (patron == null) {
            logger.error("Checkout failed: No patron found with ID " + patronId);
            throw new IllegalArgumentException("No patron found with ID " + patronId);
        }

        // --- Validation Logic ---
        if (item.getBook().getType() == BookType.REFERENCE) {
            logger.warn("Attempt to check out reference book: " + barcode);
            throw new IllegalStateException("Reference books cannot be checked out.");
        }
        if (item.getStatus() != BookStatus.AVAILABLE) {
            logger.warn("Book not available: " + barcode + " (Status: " + item.getStatus() + ")");
            throw new IllegalStateException("Book is not available.");
        }

        // --- Process the Loan ---
        item.setStatus(BookStatus.BORROWED);

        LocalDate checkoutDate = LocalDate.now();
        LocalDate dueDate = calculateDueDate(checkoutDate);
        String recordId = UUID.randomUUID().toString(); // Generate a unique ID for the loan

        // We assume LendingRecord has a constructor:
        // (recordId, bookItemBarcode, patronId, checkoutDate, dueDate)
        LendingRecord record = new LendingRecord(recordId, barcode, patronId, checkoutDate, dueDate);

        activeLoans.add(record);
        patron.getBorrowingHistory().add(record); // Add to patron's permanent history

        logger.info("Book checked out: " + barcode + " to " + patronId);
    }

    /**
     * Returns a book item to the library.
     *
     * @param barcode The barcode of the specific book item
     */
    public void returnBook(String barcode) {
        BookItem item = bookSvc.getBookItemByBarcode(barcode);
        if (item == null) {
            logger.error("Return failed: No book item found with barcode " + barcode);
            throw new IllegalArgumentException("No book item found with barcode " + barcode);
        }

        if (item.getStatus() != BookStatus.BORROWED) {
            logger.warn("Return failed: Book " + barcode + " is not currently checked out.");
            throw new IllegalStateException("Book is not currently checked out. Status: " + item.getStatus());
        }

        // --- Find and update LendingRecord ---
        Optional<LendingRecord> recordOpt = activeLoans.stream()
                .filter(record -> record.getBookItemBarcode().equals(barcode) && record.getReturnDate() == null)
                .findFirst();

        if (!recordOpt.isPresent()) {
            logger.error("CRITICAL: No active loan record found for borrowed item: " + barcode);
            throw new IllegalStateException("Data inconsistency: No active loan record found for borrowed item.");
        }

        LendingRecord record = recordOpt.get();
        record.setReturnDate(LocalDate.now());
        activeLoans.remove(record);

        logger.info("Loan record updated for item " + barcode);

        // --- THIS IS THE UPDATED LOGIC ---
        // Directly ask ReservationService to process the return
        // and tell us what the new status should be.
        BookStatus newStatus = reservationSvc.processBookReturn(item.getBook());

        // Set the status returned by the service
        item.setStatus(newStatus);

        if (newStatus == BookStatus.RESERVED) {
            logger.info("Book returned and held for reservation: " + barcode);
        } else {
            logger.info("Book returned and available: " + barcode);
        }
    }
    /**
     * Helper method to calculate the due date.
     * (Could be expanded with rules for different BookTypes or PatronTypes)
     */
    private LocalDate calculateDueDate(LocalDate checkoutDate) {
        return checkoutDate.plusDays(STANDARD_LOAN_DAYS);
    }

    /**
     * Gets a list of all currently active loans.
     */
    public List<LendingRecord> getActiveLoans() {
        return new ArrayList<>(activeLoans); // Return a copy
    }

    /**
     * Gets all overdue loans.
     */
    public List<LendingRecord> getOverdueLoans() {
        LocalDate today = LocalDate.now();
        return activeLoans.stream()
                .filter(loan -> loan.getDueDate().isBefore(today))
                .collect(Collectors.toList());
    }
}