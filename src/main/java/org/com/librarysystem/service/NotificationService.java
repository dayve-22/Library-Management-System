package org.com.librarysystem.service;

import org.com.librarysystem.core.Patron;
import org.com.librarysystem.patterns.singleton.Logger;

/**
 * Handles sending notifications to patrons.
 *
 * In this simple implementation, it adds a message to the patron's
 * internal notification list. In a real system, this service would
 * be responsible for sending emails, SMS, or push notifications.
 */
public class NotificationService {

    private final Logger logger = Logger.getInstance();

    public NotificationService() {
        logger.info("NotificationService initialized.");
    }

    /**
     * Sends a notification message to a patron.
     *
     * @param patron  The patron to notify.
     * @param message The message to send.
     */
    public void sendNotification(Patron patron, String message) {
        if (patron == null) {
            logger.warn("Attempted to send notification to a null patron.");
            return;
        }

        if (message == null || message.trim().isEmpty()) {
            logger.warn("Attempted to send a null or empty message to patron: " + patron.getPatronId());
            return;
        }

        // In our LLD, "sending" just means adding to the patron's internal list.
        patron.addNotification(message);

        logger.info("Notification sent to " + patron.getName() + " (ID: " + patron.getPatronId() + "): " + message);

        // --- Future Extension ---
        // This is where you would add integration with an email service:
        //
        // try {
        //   EmailClient.send(patron.getEmail(), "Library Notification", message);
        //   logger.info("Notification email successfully sent to " + patron.getEmail());
        // } catch (EmailException e) {
        //   logger.error("Failed to send email to " + patron.getEmail(), e);
        // }
    }
}