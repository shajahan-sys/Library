package org.library.features.book;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.library.features.author.Author;
import org.library.features.login.Login;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class BookServiceTest {
    private BookService bookService;
    private List<Book> bookList;
    private List<Author> authors;
    private Login login;
    private final BookDAO bookDAO = mock(BookDAOImpl.class);

    @BeforeEach
    void setUp() {
        bookService = new BookService();
        createBookList();
        bookService.setBookDAO(bookDAO);
        when(bookDAO.getBooksList(login)).thenReturn(bookList);
        when(bookDAO.getAuthorsList(login)).thenReturn(authors);

    }

    @AfterEach
    void tearDown() {
        bookService = null;
        bookList = null;
        authors = null;
        login = null;
    }

    @Test
    void shouldReturnProperListFromDAO() {
        assertAll(
                () -> assertEquals(bookList, bookService.getBooksList(login)),
                () -> assertEquals(authors, bookService.getAuthorsList(login))
        );
    }

    @Test
    void shouldReturnProperBookAccordingToGivenIdOrReturnNull() {
        bookService.getBooksList(login);
        assertAll(
                () -> assertEquals(bookService.getBook(new Book(0)), bookList.get(0)),
                () -> assertNull(bookService.getBook(new Book(1232)))
        );
    }

    @Test
    void shouldFilterBookListAccordingToPassedBookObject() {
        bookService.getBooksList(login);
        Book onlyAuthor = new Book();
        onlyAuthor.setAuthor(authors.get(0));
        onlyAuthor.setTitle("");
        Book onlyTitle = new Book();
        onlyTitle.setTitle("ti");
        Book authorAndTitle = new Book();
        authorAndTitle.setTitle("1");
        authorAndTitle.setAuthor(authors.get(1));
        List<Book> listByAuthor = bookList.stream()
                .filter(b -> b.getAuthor().getId() == onlyAuthor.getAuthor().getId())
                .collect(Collectors.toList());
        List<Book> listByTitle = bookList.stream()
                .filter(b -> b.getTitle().contains(onlyTitle.getTitle()))
                .collect(Collectors.toList());
        List<Book> listByAuthorAndTitle = bookList.stream()
                .filter(b -> b.getAuthor().getId() == authorAndTitle.getAuthor().getId())
                .filter(b -> b.getTitle().contains(authorAndTitle.getTitle()))
                .collect(Collectors.toList());
        assertAll(
                () -> assertEquals(bookService.filterBooks(onlyAuthor), listByAuthor),
                () -> assertEquals(bookService.filterBooks(onlyTitle), listByTitle),
                () -> assertEquals(bookService.filterBooks(authorAndTitle), listByAuthorAndTitle)
        );
    }

   private void createBookList() {
        createLogin();
        createAuthorList();
        bookList = Arrays.asList(
                new Book(0),
                new Book(1),
                new Book(2)
        );
        bookList.forEach(
                book -> {
                    book.setTitle("title" + book.getId());
                    book.setAuthor(authors.get(book.getId()));
                    book.setPublicationYear("202" + book.getId());
                    book.setLogin(login);
                }
        );
    }

   private void createAuthorList() {
        authors = Arrays.asList(
                new Author(1),
                new Author(2),
                new Author(3)
        );
    }

   private void createLogin() {
        login = new Login();
        login.setUserName("hj");
        login.setPassword("pass");
    }
}