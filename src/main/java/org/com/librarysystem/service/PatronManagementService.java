package org.com.librarysystem.service;

import org.com.librarysystem.core.Patron;
import org.com.librarysystem.patterns.singleton.Logger;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Manages the collection of all patrons (members) in the library system.
 * Handles adding, updating, and retrieving patron information.
 */
public class PatronManagementService {

    // Storage for all patrons, keyed by their unique patronId.
    private final Map<String, Patron> patrons;
    private final Logger logger = Logger.getInstance();

    /**
     * Initializes the PatronManagementService.
     */
    public PatronManagementService() {
        this.patrons = new HashMap<>();
        logger.info("PatronManagementService initialized.");
    }

    /**
     * Registers a new patron in the system.
     * Generates a unique patron ID.
     *
     * @param name  The full name of the patron.
     * @param email The patron's email address.
     * @return The newly created Patron object.
     */
    public Patron addPatron(String name, String email) {
        if (name == null || name.trim().isEmpty() || email == null || email.trim().isEmpty()) {
            logger.warn("Attempted to add patron with invalid name or email.");
            throw new IllegalArgumentException("Patron name and email must not be null or empty.");
        }

        // (Optional) Check for duplicate email
        if (isEmailInUse(email)) {
            logger.warn("Attempted to add patron with duplicate email: " + email);
            throw new IllegalStateException("A patron with this email already exists.");
        }

        // Generate a unique ID
        String patronId = "p-" + UUID.randomUUID().toString().substring(0, 8);

        // We assume Patron has a constructor: (patronId, name, email)
        Patron newPatron = new Patron(patronId, name, email);

        patrons.put(patronId, newPatron);
        logger.info("Added new patron: " + name + " (ID: " + patronId + ")");

        return newPatron;
    }

    /**
     * Retrieves a patron by their unique ID.
     *
     * @param patronId The ID of the patron to find.
     * @return The Patron object, or null if not found.
     */
    public Patron getPatronById(String patronId) {
        return patrons.get(patronId);
    }

    /**
     * Updates the information for an existing patron.
     *
     * @param updatedPatron The patron object containing the new information.
     */
    public void updatePatron(Patron updatedPatron) {
        if (updatedPatron == null || updatedPatron.getPatronId() == null) {
            throw new IllegalArgumentException("Cannot update a null patron or patron with no ID.");
        }

        if (!patrons.containsKey(updatedPatron.getPatronId())) {
            logger.warn("Attempted to update a patron that doesn't exist: " + updatedPatron.getPatronId());
            throw new IllegalArgumentException("No patron found with this ID to update.");
        }

        patrons.put(updatedPatron.getPatronId(), updatedPatron);
        logger.info("Updated information for patron: " + updatedPatron.getPatronId());
    }

    /**
     * Helper method to check if an email is already registered.
     */
    private boolean isEmailInUse(String email) {
        return patrons.values().stream()
                .anyMatch(patron -> patron.getEmail().equalsIgnoreCase(email));
    }

    /**
     * Returns an unmodifiable view of the patrons map.
     *
     * @return An unmodifiable Map of all patrons.
     */
    public Map<String, Patron> getPatrons() {
        return Collections.unmodifiableMap(patrons);
    }
}