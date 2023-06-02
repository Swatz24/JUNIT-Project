package org.example;

import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("Testing BookService Class")
public class BookServiceTest {

    @Mock
    private List<Book> bookDatabase;

    @InjectMocks
    private BookService bookService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @BeforeAll
    public static void setClass() {
        System.out.println("Setting up @BeforeAll");
    }

    @AfterAll
    public static void tearDownClass() {
        System.out.println("Cleaning up after all testing with @AfterAll");
    }

    @AfterEach
    public void tearDown() {
        System.out.println("Executing clean up operations after each test with @AfterEach");
        bookService = null;
    }

    // Test searchBook()
    @Test
    @DisplayName("Search Book by Title - Positive Match")
    public void searchBookTitlePositive() {
        Book book1 = new Book("Book1", "Author1", "Mystery", 20.95);
        Book book2 = new Book("Book2", "Author2", "Fantasy", 25.50);
        List<Book> books = new ArrayList<>();
        books.add(book1);
        books.add(book2);
        when(bookDatabase.stream()).thenReturn(books.stream());

        List<Book> results = bookService.searchBook("Book1");
        assertEquals(1, results.size());
        assertTrue(results.contains(book1));
    }

    @Test
    @DisplayName("Search Book by Author - Positive Match")
    public void searchBookAuthorPositive() {
        Book book1 = new Book("Book1", "Author1", "Mystery", 20.95);
        Book book2 = new Book("Book2", "Author2", "Fantasy", 25.50);
        List<Book> books = new ArrayList<>();
        books.add(book1);
        books.add(book2);
        when(bookDatabase.stream()).thenReturn(books.stream());

        List<Book> results = bookService.searchBook("Author2");
        assertEquals(1, results.size());
        assertTrue(results.contains(book2));
    }

    @Test
    @DisplayName("Search Book by Genre - Positive Match")
    public void searchBookGenrePositive() {
        Book book1 = new Book("Book1", "Author1", "Mystery", 20.95);
        Book book2 = new Book("Book2", "Author2", "Fantasy", 25.50);
        List<Book> books = new ArrayList<>();
        books.add(book1);
        books.add(book2);
        when(bookDatabase.stream()).thenReturn(books.stream());

        List<Book> results = bookService.searchBook("Mystery");
        assertEquals(1, results.size());
        assertTrue(results.contains(book1));
    }

    @Test
    @DisplayName("Search Book - Positive Match with OR Condition")
    public void searchBookPositiveAll() {
        Book book1 = new Book("Book1", "Author1", "Mystery", 20.95);
        Book book2 = new Book("Book2", "Author2", "Fantasy", 25.50);
        List<Book> books = new ArrayList<>();
        books.add(book1);
        books.add(book2);
        when(bookDatabase.stream()).thenReturn(books.stream());

        List<Book> results = bookService.searchBook("Author1");
        assertEquals(1, results.size());
        assertTrue(results.contains(book1));
    }

    @Test
    @DisplayName("Search Book - Positive Match with OR Condition")
    public void searchBookPositiveMultipleBooks() {

        // Adding two books of the same author
        Book book1 = new Book("Book1", "Author1", "Mystery", 20.95);
        Book book2 = new Book("Book2", "Author1", "Fantasy", 25.50);
        List<Book> books = new ArrayList<>();
        books.add(book1);
        books.add(book2);
        when(bookDatabase.stream()).thenReturn(books.stream());

        // Searching books of Author1
        List<Book> results = bookService.searchBook("Author1");
        // Assert result size should be 2, as there are two books with same author and the results List should contain both the books.
        assertEquals(2, results.size());
        assertTrue(results.contains(book1));
        assertTrue(results.contains(book2));
    }

    @Test
    @DisplayName("Search Book - No Match")
    public void searchBookNegative() {
        Book book1 = new Book("Book1", "Author1", "Mystery", 20.95);
        Book book2 = new Book("Book2", "Author2", "Fantasy", 25.50);
        List<Book> books = new ArrayList<>();
        books.add(book1);
        books.add(book2);
        when(bookDatabase.stream()).thenReturn(books.stream());

        List<Book> searchResult = bookService.searchBook("Nonexistent Book");
        assertTrue(searchResult.isEmpty());
    }

    @Test
    @DisplayName("Search Book - Empty Database- Edge case")
    public void searchBookEmptyDatabase() {
        List<Book> books = new ArrayList<>();
        // Mock up empty database
        when(bookDatabase.stream()).thenReturn(books.stream());

        // searching empty DB, searchResult should be empty.
        List<Book> searchResult = bookService.searchBook("Mystery");
        assertTrue(searchResult.isEmpty());
    }

    // Testing purchaseBook()

    @Test
    public void purchaseBookPositive() {
        Book book1 = new Book("Book1", "author1", "Mystery", 20.00);
        User user = new User("user1", "abcd1234", "user1@gmail.com", 25);
        // Setup mock behaviour to return book exists. and then purchase the book.
        when(bookDatabase.contains(book1)).thenReturn(true);
        boolean result = bookService.purchaseBook(user, book1);

        // assert the book purchase is successful.
        assertTrue(result);
    }

    @Test
    public void purchaseBookNegative() {
        Book book1 = new Book("Book1", "author1", "Mystery", 20.00);
        User user = new User("user1", "abcd1234", "user1@gmail.com", 30);
        //   Setup mock behaviour to return book does not exists. and then show book purchase failed.
        when(bookDatabase.contains(book1)).thenReturn(false);
        boolean result = bookService.purchaseBook(user, book1);
        assertFalse(result);
    }

    @Test
    @DisplayName("Purchase Book - Insufficient Funds- Edge Case")
    public void purchaseBookInsufficientFunds() {
        Book book1 = new Book("Book1", "author1", "Mystery", 20.00);
        User user = new User("user1", "abcd1234", "user1@gmail.com");
        // Setup mock behavior to return book exists.
        when(bookDatabase.contains(book1)).thenReturn(true);
        // Set user's balance to a lower value than the book price
        user.setBalance(10.00);
        boolean result = bookService.purchaseBook(user, book1);

        // Assert that the book purchase fails due to insufficient funds
        assertFalse(result);
    }

    // Testing remove book function.
    @Test
    @DisplayName("Remove Book - Positive")
    public void removeBookPositive() {
        Book book1 = new Book("Book1", "author1", "Mystery", 20.00);
        // Mock the behaviour to say that the book is in the DB
        when(bookDatabase.remove(book1)).thenReturn(true);

        // Calling the removeBook() and passing the book to be removed.
        boolean result = bookService.removeBook(book1);
        // assert true means the book is removed successfully.
        assertTrue(result);
        // Verify that the remove() method of the book DB was called with the book1 as an argument:
        verify(bookDatabase).remove(book1);
    }

    @Test
    @DisplayName("Remove Book - Negative")
    public void removeBookNegative() {
        Book book1 = new Book("Book1", "author1", "Mystery", 20.00);
        // Mock the behaviour to show that the book is not in the DB
        when(bookDatabase.remove(book1)).thenReturn(false);
        // Calling the removeBook() and passing the book to be removed.
        boolean result = bookService.removeBook(book1);
        //  // assert false means the book is not removed .
        assertFalse(result);
        // Verify that the remove() method of the book DB was called with the book1 as an argument:
        verify(bookDatabase).remove(book1);
    }

    @Test
    @DisplayName("Remove Book - Null Book")
    // edgeCase trying to remove null
    public void removeBookNull() {
            // Passing null as the book to be removed
            boolean result = bookService.removeBook(null);
            // Assert false means the book is not removed.
            assertFalse(result);
            // Verify that the remove() method of the book DB was not called with a non-null argument
            verify(bookDatabase, never()).remove(isNotNull());
    }


  // Testing addBook Method

    @Test
    @DisplayName("Add Book - Positive")
    public void addBookPositive() {
        Book book1 = new Book("Book1", "author1", "Mystery", 20.00);
        //set up mock behavior for book database , the book1 doesn't exist already.
        when(bookDatabase.contains(book1)).thenReturn(false);
        // Calling the method with book1 as argument
        boolean result = bookService.addBook(book1);
        // assert true since the book is added successfully.
        assertTrue(result);
        // verify if the add method is called with the book1 as argument only once.
        verify(bookDatabase, times(1)).add(book1);
    }

    @Test
    @DisplayName("Add Book - Negative")
    public void addBookNegative() {
        Book book1 = new Book("Book1", "author1", "Mystery", 20.00);
        //set up mock behavior for book database , the book1 exist already.
        when(bookDatabase.contains(book1)).thenReturn(true);
        // Calling the method with book1 as argument
        boolean result = bookService.addBook(book1);
        // assert false since the book is not added.
        assertFalse(result);
        // verify add() is not called with book1 as argument.
        verify(bookDatabase, never()).add(book1);
    }

        @Test
        @DisplayName("Add Book - Existing Book with Different Price- EdgeCase")
        public void addBookExistingWithDifferentPrice() {
            // Same book but two different prices, so it can be added.
            Book existingBook = new Book("Book1", "author1", "Mystery", 15.00);
            Book newBookWithDifferentPrice = new Book("Book1", "author1", "Mystery", 25.00);
            // Set up mock behavior for book database, indicating that the existing book already exists
            when(bookDatabase.contains(existingBook)).thenReturn(true);
            // Calling the method with the new book that has a different price
            boolean result = bookService.addBook(newBookWithDifferentPrice);
            // Assert true since the book is added
            assertTrue(result);
            // Verify that add() is called with the new book as an argument
            verify(bookDatabase, times(1)).add(newBookWithDifferentPrice);
        }

        // Testing addBookReview method

         @Test
         @DisplayName("Add Book Review - Positive- Book purchased by user")
         public void addBookReviewPositive() {
            List<Book> purchasedBooks = new ArrayList<>();
            Book book = new Book("Book1", "Author1", "Mystery", 35);
            purchasedBooks.add(book);
            User user = new User("user1", "abcd1234", "user1@gmail.com", purchasedBooks);

            String review = "Good read!";
            boolean result = bookService.addBookReview(user, book, review);

            assertTrue(result); // Verify that the review was added since the book was purchased
            assertTrue(book.getReviews().contains(review)); // Verify that the review is present in the book's reviews list
    }

    @Test
    @DisplayName("Add Book Review - User Has Not Purchased Book")
    public void addBookReviewNegative() {
        List<Book> purchasedBooks = new ArrayList<>();
        Book book = new Book("Book1", "Author1", "Mystery", 12);
        User user = new User("user1", "abcd1234", "user1@gmail.com", purchasedBooks);

        String review = "Good read!";
        boolean result = bookService.addBookReview(user, book, review);

        assertFalse(result); // Verify that the review was not added since the book was not purchased
        assertFalse(book.getReviews().contains(review)); // Verify that the review is not present in the book's reviews list
    }

    @Test
    @DisplayName("Add Book Review - Empty Review")
    public void addBookReviewEdgeCaseEmptyReview() {
        List<Book> purchasedBooks = new ArrayList<>();
        Book book = new Book("Book1", "Author1", "Mystery", 25);
        purchasedBooks.add(book);
        User user = new User("user1", "abcd1234", "user1@gmail.com", purchasedBooks);

        String review = ""; // Empty review
        boolean result = bookService.addBookReview(user, book, review);

        assertFalse(result); // Verify that the review was not added since it is empty
        assertFalse(book.getReviews().contains(review)); // Verify that the empty review is not present in the book's reviews list
    }







}
