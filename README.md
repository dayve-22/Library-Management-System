classDiagram
    direction LR

    class LibraryFacade {
        +checkoutBook(patronId, bookItemId)
        +returnBook(bookItemId)
        +addPatron(name, email)
        +addBook(title, author, isbn, ...)
        +searchBook(query, strategy)
        +reserveBook(patronId, bookIsbn)
    }

    class BookManagementService {
        -Map~String, Book~ bookCatalog
        -Map~String, BookItem~ bookItems
        +addBook(Book)
        +addBookItem(isbn) BookItem
        +removeBookItem(bookItemId)
        +updateBook(Book)
    }

    class PatronManagementService {
        -Map~String, Patron~ patrons
        +addPatron(name, email) Patron
        +updatePatron(Patron)
        +getPatron(patronId) Patron
    }

    class LendingService {
        -List~LendingRecord~ activeLoans
        -BookManagementService bookSvc
        -PatronManagementService patronSvc
        +checkout(patronId, bookItemId)
        +returnBook(bookItemId)
    }

    class ReservationService {
        -Map~String, Queue~Reservation~~ reservationQueues
        -NotificationService notificationSvc
        +makeReservation(patron, book)
        +handleReturn(book)
        +update(Book)
    }

    class SearchService {
        -SearchStrategy strategy
        +setStrategy(SearchStrategy)
        +search(query, catalog) List~Book~
    }

    class SearchStrategy {
        <<Interface>>
        +search(query, catalog) List~Book~
    }

    class SearchByTitleStrategy {
        +search(query, catalog) List~Book~
    }
    class SearchByAuthorStrategy {
        +search(query, catalog) List~Book~
    }
    class SearchByIsbnStrategy {
        +search(query, catalog) List~Book~
    }

    class Book {
        -String isbn
        -String title
        -String author
        -int publicationYear
        -BookType type
        -List~Observer~ observers
        +addObserver(Observer)
        +notifyObservers()
    }

    class BookItem {
        -String barcode
        -Book book
        -BookStatus status
        -Branch currentBranch
    }

    class Patron {
        -String patronId
        -String name
        -String email
        -List~LendingRecord~ borrowingHistory
        -List~String~ notifications
        +addNotification(message)
    }

    class LendingRecord {
        -String recordId
        -String bookItemId
        -String patronId
        -LocalDate checkoutDate
        -LocalDate dueDate
        -LocalDate returnDate
    }

    class Reservation {
        -String reservationId
        -Patron patron
        -Book book
        -ReservationStatus status
    }

    class Branch {
        -String branchId
        -String name
        -Map~String, BookItem~ branchInventory
    }

    class NotificationService {
        +sendNotification(Patron, message)
    }
    
    class Logger {
        <<Singleton>>
        +getInstance() Logger
        +info(message)
        +warn(message)
        +error(message)
    }


    LibraryFacade ..> BookManagementService
    LibraryFacade ..> PatronManagementService
    LibraryFacade ..> LendingService
    LibraryFacade ..> ReservationService
    LibraryFacade ..> SearchService

    LendingService ..> BookManagementService
    LendingService ..> PatronManagementService
    LendingService ..> ReservationService : notif. on return

    ReservationService ..> NotificationService
    ReservationService ..> Patron
    ReservationService ..> Book : Observes
    
    SearchService *-- SearchStrategy
    SearchByTitleStrategy ..|> SearchStrategy
    SearchByAuthorStrategy ..|> SearchStrategy
    SearchByIsbnStrategy ..|> SearchStrategy

    BookItem *-- Book
    BookItem *-- Branch
    
    Branch *-- "1..*" BookItem : has

    Reservation *-- Patron
    Reservation *-- Book

    LendingRecord -- BookItem
    LendingRecord -- Patron
    
    Patron "1" -- "0..*" LendingRecord : has history
