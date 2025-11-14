package org.com.librarysystem.service;

import org.com.librarysystem.core.Book;
import org.com.librarysystem.core.Patron;
import org.com.librarysystem.core.Reservation;
import org.com.librarysystem.enums.ReservationStatus;
import org.com.librarysystem.patterns.observer.Observer;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class ReservationService implements Observer {
    private Map<String, Queue<Reservation>> reservationQueues; // Key: ISBN
    private NotificationService notificationSvc;

    public ReservationService(NotificationService notificationSvc) {
        this.notificationSvc = notificationSvc;
        this.reservationQueues = new HashMap<>();
    }

    public void makeReservation(Patron patron, Book book) {
        // Add reservation to the queue
        reservationQueues.putIfAbsent(book.getIsbn(), new LinkedList<>());
        reservationQueues.get(book.getIsbn()).add(new Reservation(patron, book));

        // Subscribe this service to the book's events
        book.addObserver(this);
    }

    // This is the notification from the Subject (Book)
    @Override
    public void update(Book book) {
        // A book of this type was returned. Check our queue.
        Queue<Reservation> queue = reservationQueues.get(book.getIsbn());
        if (queue != null && !queue.isEmpty()) {
            Reservation nextInLine = queue.poll();
            nextInLine.setStatus(ReservationStatus.READY_FOR_PICKUP);

            // Send notification to the patron
            notificationSvc.sendNotification(nextInLine.getPatron(),
                    "Your reserved book '" + book.getTitle() + "' is ready for pickup!");
        }
    }

    // Called by LendingService on return
    public boolean handleBookReturn(Book book) {
        book.notifyObservers(); // This will trigger the update() method above

        // Check if a reservation was successfully processed
        Queue<Reservation> queue = reservationQueues.get(book.getIsbn());
        return queue != null && queue.peek().getStatus() == ReservationStatus.READY_FOR_PICKUP;
    }
}
