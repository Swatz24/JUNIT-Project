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
        System.out.println("Cleaning up after testing with @AfterAll");
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

    // Testing purchaseBook()

    @Test
    public void purchaseBookPositive() {
        Book book1 = new Book("Book1", "author1", "Mystery", 20.00);
        User user = new User("user1", "abcd1234", "user1@gmail.com");
        // Setup mock behaviour to return book exists. and then purchase the book.
        when(bookDatabase.contains(book1)).thenReturn(true);
        boolean result = bookService.purchaseBook(user, book1);

        // assert the book purchase is successful.
        assertTrue(result);
    }

    @Test
    public void purchaseBookNegative() {
        Book book1 = new Book("Book1", "author1", "Mystery", 20.00);
        User user = new User("user1", "abcd1234", "user1@gmail.com");
        //   Setup mock behaviour to return book does not exists. and then show book purchase failed.
        when(bookDatabase.contains(book1)).thenReturn(false);
        boolean result = bookService.purchaseBook(user, book1);
        assertFalse(result);
    }

    // Testing remove book function.
    @Test
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

  // Testing addBook Method

    @Test
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
    public void addBookEdge() {

        // Test adding a new book to the empty database
        Book book2 = new Book("Book2", "author2", "Thriller", 15.00);
        // Set up mock behavior for book database, the book2 doesn't exist already.
        when(bookDatabase.contains(book2)).thenReturn(false);
        // Calling the method with book2 as an argument
        boolean result2 = bookService.addBook(book2);
        // Assert true since the book is added successfully.
        assertTrue(result2);
        // Verify if the add method is called with the book2 as an argument only once.
        verify(bookDatabase, times(1)).add(book2);
    }


}
