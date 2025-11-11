package org.com.librarysystem.enums;

public enum BookStatus {
    AVAILABLE,
    BORROWED,
    RESERVED, // Held for a specific patron
    MAINTENANCE,
    LOST
}