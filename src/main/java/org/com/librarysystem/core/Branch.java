package org.com.librarysystem.core;

import org.com.librarysystem.patterns.singleton.Logger;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Represents a single library branch.
 * It manages its own physical inventory of BookItems.
 */
public class Branch {

    private String branchId;
    private String name;
    // Each branch manages its own physical inventory
    // Key: BookItem barcode, Value: BookItem object
    private Map<String, BookItem> branchInventory;

    private static final Logger logger = Logger.getInstance();

    // --- Constructors ---

    /**
     * Default constructor for frameworks or manual initialization.
     * Initializes the inventory.
     */
    public Branch() {
        this.branchInventory = new HashMap<>();
    }

    /**
     * Creates a new Branch with a specified name and a generated ID.
     *
     * @param name The name of the branch (e.g., "Main Street Branch").
     */
    public Branch(String name) {
        this.branchId = "br-" + UUID.randomUUID().toString().substring(0, 8);
        this.name = name;
        this.branchInventory = new HashMap<>();
        logger.info("New branch created: " + name + " (ID: " + this.branchId + ")");
    }

    /**
     * Full constructor for creating a branch with a known ID.
     *
     * @param branchId The specific ID for this branch.
     * @param name     The name of the branch.
     */
    public Branch(String branchId, String name) {
        this.branchId = branchId;
        this.name = name;
        this.branchInventory = new HashMap<>();
    }

    // --- Inventory Management Methods ---

    /**
     * Adds a physical book item to this branch's inventory.
     * This is typically called by the BookManagementService.
     *
     * @param item The BookItem to add.
     */
    public void addBookItem(BookItem item) {
        if (item == null || item.getBarcode() == null) {
            logger.warn("Attempted to add a null item or item with no barcode to branch " + name);
            return;
        }

        // Set the item's location to this branch
        item.setCurrentBranch(this);

        branchInventory.put(item.getBarcode(), item);
        logger.info("Item " + item.getBarcode() + " added to branch " + name);
    }

    /**
     * Removes a physical book item from this branch's inventory.
     * This is typically called by the BookManagementService (e.g., when a book is lost)
     * or by a transfer service.
     *
     * @param item The BookItem to remove.
     */
    public void removeBookItem(BookItem item) {
        if (item == null || item.getBarcode() == null) {
            logger.warn("Attempted to remove a null item or item with no barcode from branch " + name);
            return;
        }

        BookItem removedItem = branchInventory.remove(item.getBarcode());
        if (removedItem != null) {
            // Unset the item's location
            removedItem.setCurrentBranch(null);
            logger.info("Item " + item.getBarcode() + " removed from branch " + name);
        } else {
            logger.warn("Item " + item.getBarcode() + " not found in branch " + name + " for removal.");
        }
    }

    /**
     * Retrieves a specific book item from this branch's inventory.
     *
     * @param barcode The barcode of the item to find.
     * @return The BookItem, or null if not found at this branch.
     */
    public BookItem getBookItem(String barcode) {
        return branchInventory.get(barcode);
    }

    // --- Getters and Setters ---

    public String getBranchId() {
        return branchId;
    }

    public void setBranchId(String branchId) {
        this.branchId = branchId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns an unmodifiable view of the branch's inventory
     * to prevent accidental outside modification.
     *
     * @return An unmodifiable Map of the branch's inventory.
     */
    public Map<String, BookItem> getBranchInventory() {
        return Collections.unmodifiableMap(branchInventory);
    }

    /**
     * Sets the entire inventory map. This is generally discouraged in favor of
     * addBookItem and removeBookItem, but can be used for initialization.
     *
     * @param branchInventory The map to set as the inventory.
     */
    public void setBranchInventory(Map<String, BookItem> branchInventory) {
        this.branchInventory = branchInventory;
    }
}