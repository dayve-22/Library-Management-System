### 📚 Library Management System

This project is a Java-based Library Management System, designed as a Low-Level Design (LLD) exercise. It demonstrates core Object-Oriented Programming (OOP) concepts, SOLID principles, and the application of key design patterns to manage books, patrons, and the lending process.

This system is built with a focus on clean, maintainable, and extensible code, separating concerns between data models, business logic (services), and core patterns.

**Core Features**

**Book Management**: Add new book titles and physical copies (items) to the catalog.

**Patron Management**: Register new patrons and update their information.

**Search**: A flexible search system to find books by Title, Author, or ISBN.

**Lending**: Full checkout and return workflow.

**Reservation System**: Patrons can reserve a book title. When a copy is returned, the first patron in the queue is notified, and the book is held for them.

**Logging**: All major events are logged using a Singleton Logger.

**class Diagram**

This diagram shows the relationships between the core classes and services. The LibraryFacade acts as the single entry point for the Main class, hiding the complexity of the service layer.

classDiagram
    direction LR

    class LibraryFacade {
        +checkoutBook(patronId, bookItemId)
        +returnBook(bookItemId)
        +addNewPatron(name, email)
        +addNewBook(title, author, isbn, ...)
        +searchBooks(query, strategy)
        +reserveBook(patronId, bookIsbn)
    }

    class BookManagementService {
        -Map~String, Book~ bookCatalog
        -Map~String, BookItem~ bookItems
        +addBook(Book)
        +addBookItem(Book) BookItem
        +getBookByIsbn(String) Book
        +getBookItemByBarcode(String) BookItem
    }

    class PatronManagementService {
        -Map~String, Patron~ patrons
        +addPatron(name, email) Patron
        +updatePatron(Patron)
        +getPatronById(String) Patron
    }

    class LendingService {
        -List~LendingRecord~ activeLoans
        -BookManagementService bookSvc
        -PatronManagementService patronSvc
        -ReservationService reservationSvc
        +checkoutBook(patronId, barcode)
        +returnBook(barcode)
    }

    class ReservationService {
        -Map~String, Queue~Reservation~~ reservationQueues
        -NotificationService notificationSvc
        +makeReservation(patron, book)
        +processBookReturn(book) BookStatus
    }

    class SearchService {
        -SearchStrategy strategy
        +setStrategy(SearchStrategy)
        +executeSearch(query, catalog) List~Book~
    }

    class SearchStrategy {
        <<Interface>>
        +search(query, catalog) List~Book~
    }
    class SearchByTitleStrategy
    class SearchByAuthorStrategy
    class SearchByIsbnStrategy

    class Book {
        -String isbn
        -String title
        -String author
        -BookType type
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
    
    class NotificationService {
        +sendNotification(Patron, message)
    }
    
    class Logger {
        <<Singleton>>
        +getInstance() Logger
        +info(message)
    }

    LibraryFacade ..> BookManagementService
    LibraryFacade ..> PatronManagementService
    LibraryFacade ..> LendingService
    LibraryFacade ..> ReservationService
    LibraryFacade ..> SearchService

    LendingService ..> BookManagementService
    LendingService ..> PatronManagementService
    LendingService ..> ReservationService : calls processBookReturn()

    ReservationService ..> NotificationService
    ReservationService ..> Patron
    ReservationService ..> Book
    
    SearchService *-- SearchStrategy
    SearchByTitleStrategy ..|> SearchStrategy
    SearchByAuthorStrategy ..|> SearchStrategy
    SearchByIsbnStrategy ..|> SearchStrategy

    BookItem *-- Book
    Patron *-- "0..*" LendingRecord
    LendingRecord -- BookItem


**Design Patterns Used**

This project explicitly implements the following design patterns:

**Facade Pattern:**

Class: LibraryFacade

Purpose: Provides a simple, unified interface to the entire subsystem. The Main class only needs to interact with this facade, which then delegates calls to the appropriate services (LendingService, PatronManagementService, etc.). This decouples the client from the complex internal logic.

**Strategy Pattern:**

Interface: SearchStrategy

Implementations: SearchByTitleStrategy, SearchByAuthorStrategy, SearchByIsbnStrategy

Context: SearchService

Purpose: Allows the search algorithm to be selected at runtime. The LibraryFacade can easily execute different searches by passing a different strategy object to the SearchService, adhering to the Open-Closed Principle.

**Singleton Pattern:**

Class: Logger

Purpose: Ensures that only one instance of the Logger is created throughout the application. All services and classes access this single instance via Logger.getInstance(), providing a global, consistent logging mechanism.

**SOLID Principles**

Single Responsibility Principle (SRP): Each class has one primary responsibility.

LendingService handles checkouts and returns.

PatronManagementService handles adding/updating patrons.

BookManagementService handles the book catalog.

ReservationService handles the wait-list logic.

NotificationService handles sending alerts (and could be swapped out for an email service).

Open/Closed Principle (OCP): The system is open for extension but closed for modification.

The Strategy Pattern is a perfect example. We can add a new SearchByGenreStrategy without modifying the SearchService or LibraryFacade.

Lispov Substitution Principle (LSP): All SearchStrategy implementations are substitutable for the SearchStrategy interface.

Interface Segregation Principle (ISP): The SearchStrategy interface is small and focused on a single method (search).

Dependency Inversion Principle (DIP): Services depend on abstractions, not concretions.

The LendingService doesn't create its own BookManagementService; instead, it is "injected" (passed in via the constructor). This makes the system modular and easy to test (we could pass in a "mock" service).

**How to Run the Demo**

Clone the repository to your local machine.

Open the project in your IDE (e.g., IntelliJ IDEA).

Locate the src/main/java/org/com/librarysystem/Main.java file.

Run the main method.

The console will display a full log of the demo, including adding books, registering patrons, searching, checking out, reserving, and returning a book.
