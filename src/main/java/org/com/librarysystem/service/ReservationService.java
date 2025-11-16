package org.com.librarysystem.service;

import org.com.librarysystem.core.Book;
import org.com.librarysystem.core.Patron;
import org.com.librarysystem.core.Reservation;
import org.com.librarysystem.enums.BookStatus;
import org.com.librarysystem.enums.ReservationStatus;
import org.com.librarysystem.patterns.singleton.Logger;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.UUID;

/**
 * Manages book reservations.
 *
 * NOTE: We have REMOVED the "implements Observer" part,
 * as it created a race condition. This class is now
 * called directly by LendingService.
 */
public class ReservationService {

    // Key: ISBN, Value: A queue of patrons waiting for that book
    private final Map<String, Queue<Reservation>> reservationQueues;
    private final NotificationService notificationSvc;
    private final Logger logger = Logger.getInstance();

    public ReservationService(NotificationService notificationSvc) {
        this.notificationSvc = notificationSvc;
        this.reservationQueues = new HashMap<>();
    }

    /**
     * Creates a new reservation for a book by a patron.
     */
    public void makeReservation(Patron patron, Book book) {
        if (patron == null || book == null) {
            throw new IllegalArgumentException("Patron and Book cannot be null.");
        }

        // Get or create the queue for this book's ISBN
        reservationQueues.putIfAbsent(book.getIsbn(), new LinkedList<>());

        // We assume Reservation has a constructor: (id, patron, book, status, date)
        String resId = "r-" + UUID.randomUUID().toString().substring(0, 8);
        Reservation newReservation = new Reservation(
                resId, patron, book, ReservationStatus.PENDING, LocalDate.now()
        );

        reservationQueues.get(book.getIsbn()).add(newReservation);
        logger.info("Reservation made for " + book.getIsbn() + " by " + patron.getPatronId());

        // --- OBSERVER LOGIC REMOVED ---
        // We no longer need to add this service as an observer
        // book.addObserver(this);
    }

    /**
     * This is the NEW method called by LendingService when a book is returned.
     * It checks if a reservation exists, processes it, and returns the
     * correct status (RESERVED or AVAILABLE) for the book item.
     *
     * @param book The book (title) that was returned.
     * @return The BookStatus that the physical item should be set to.
     */
    public BookStatus processBookReturn(Book book) {
        Queue<Reservation> queue = reservationQueues.get(book.getIsbn());

        if (queue != null && !queue.isEmpty()) {
            // A reservation exists! Process it.
            Reservation nextInLine = queue.poll(); // Dequeue the reservation
            nextInLine.setStatus(ReservationStatus.READY_FOR_PICKUP);

            // Send notification
            notificationSvc.sendNotification(nextInLine.getPatron(),
                    "Your reserved book '" + book.getTitle() + "' is ready for pickup!");

            // Tell LendingService to mark the physical item as RESERVED
            return BookStatus.RESERVED;

        } else {
            // No reservations pending.
            // Tell LendingService to mark the physical item as AVAILABLE
            return BookStatus.AVAILABLE;
        }
    }

    // --- The 'update' and 'handleBookReturn' methods are no longer needed ---
    // @Override
    // public void update(Book book) { ... }

    // public boolean handleBookReturn(Book book) { ... }
}