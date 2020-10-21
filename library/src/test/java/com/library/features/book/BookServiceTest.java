package com.library.features.book;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.library.features.author.Author;
import com.library.features.lend_book.Lending;
import com.library.features.login.Login;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BookServiceTest {
    private BookService bookService;
    private List<Book> bookList;
    private Login login;
    private Book myBook;
    private final BookDAO bookDAO = mock(BookDAOImpl.class);

    @BeforeEach
    void setUp() {
        bookService = new BookService();
        login = new Login();
        bookList = new ArrayList<>();
        bookService.setBookDAO(bookDAO);
        when(bookDAO.getBooksList(login)).thenReturn(bookList);
    }

    @AfterEach
    void tearDown() {
        bookService = null;
        bookList = null;
        login = null;
    }

    @Test
    void test_isLendingNull_when_book_is_on_loan() {
        myBook = new Book(2);
        myBook.setLending(new Lending());
        bookList.add(myBook);
        assertFalse(bookService.isLendingNull(login, 2));
    }

    @Test
    void test_isLendingNull_when_book_is_not_on_loan() {
        myBook = new Book(3);
        bookList.add(myBook);
        assertTrue(bookService.isLendingNull(login, 3));
    }

    @Test
    void test_deleteIfPossible_when_book_can_be_deleted() {
        myBook = new Book(3);
        bookList.add(myBook);
        assertAll(
                () -> assertTrue(bookService.deleteIfPossible(login, 3)),
                () -> verify(bookDAO).delete(myBook)
        );
    }

    @Test
    void test_deleteIfPossible_when_book_can_not_be_deleted() {
        myBook = new Book(3);
        myBook.setLending(new Lending());
        bookList.add(myBook);
        assertAll(
                () -> assertFalse(bookService.deleteIfPossible(login, 3)),
                () -> assertEquals("Cannot delete selected book, because it is currently on loan. " +
                        "Book must be returned to be deleted.", bookService.getMessage())
        );
    }

    @Test
    void test_filterBooks_when_author_equals_null() {
       /* Book b1 = new Book();
        b1.setTitle("ab");
        Book b2 = new Book();
        b2.setTitle("cd");
        Book b3 = new Book();
        b3.setTitle("abcd");
        bookList.add(b1);
        bookList.add(b2);
        bookList.add(b3);

        */
        Book book = new Book();
        book.setTitle("b");
        List<Book> myFilteredList = getPrepredBookList().stream()
                .filter(book1 -> book1.getTitle().contains(book.getTitle()))
                .collect(Collectors.toList());
        assertEquals(myFilteredList, bookService.filterBooks(book, login));
    }

    @Test
    void test_filterBooks_when_author_is_set() {
        Author a1 = new Author();
        a1.setSurname("Ma");
        Author a2 = new Author();
        a2.setSurname("Lo");
        List<Book> myList = getPrepredBookList();
        myList.get(0).setAuthor(a1);
        myList.get(1).setAuthor(a2);
        myList.get(2).setAuthor(a2);
        Book book = new Book();
        book.setTitle("b");
        book.setAuthor(a2);
        List<Book> myFilteredBooks = myList.stream()
                .filter(book1 -> book1.getAuthor().getId() == book.getAuthor().getId())
                .filter(book1 -> book1.getTitle().contains(book.getTitle()))
                .collect(Collectors.toList());
        assertEquals(myFilteredBooks, bookService.filterBooks(book, login));
    }

    @Test
    void test_getBook() {
        myBook = new Book(3);
        bookList.add(myBook);
        bookList.add(new Book(5));
        assertEquals(myBook, bookService.getBook(login, 3));
    }

    @Test
    void test_getAuthorsList() {
        List<Author> authors = new ArrayList<>();
        authors.add(new Author(1));
        when(bookDAO.getAuthorsList(login)).thenReturn(authors);
        assertEquals(authors, bookService.getAuthorsList(login));
    }

    @Test
    void test_getBooksList_when_map_does_not_contain_it() {
        bookList.add(new Book(1));
        assertEquals(bookList, bookService.getBooksList(login));
    }

    @Test
    void test_getBooksList_when_map_contains_wanted_list() {
        bookList.add(new Book(1));
        bookService.getBooksList(login);
        bookService.getBooksList(login);
        bookService.getBooksList(login);
        verify(bookDAO, Mockito.times(1)).getBooksList(login);
    }

    private List<Book> getPrepredBookList() {
        Book b1 = new Book();
        b1.setTitle("ab");
        Book b2 = new Book();
        b2.setTitle("cd");
        Book b3 = new Book();
        b3.setTitle("abcd");
        bookList.add(b1);
        bookList.add(b2);
        bookList.add(b3);
        return bookList;
    }
}