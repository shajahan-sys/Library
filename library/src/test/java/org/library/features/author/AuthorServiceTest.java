package org.library.features.author;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.library.features.book.Book;
import org.library.features.login.Login;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class AuthorServiceTest {
    private AuthorService authorService;
    private AuthorDAO authorDAO;
    private List<Author> authors;
    private Author myAuthor;
    private Login myLogin;
    @Mock
    private AuthorDAOImpl authorDAOImpl;

    @BeforeEach
    void setUp() {
        authorService = new AuthorService();
        MockitoAnnotations.openMocks(this);
        authorDAO = authorDAOImpl;
        myLogin = new Login();
        authors = new ArrayList<>();
        when(authorDAO.getAuthorsList(myLogin)).thenReturn(authors);
        authorService.setAuthorDAO(authorDAO);

    }

    @AfterEach
    void tearDown() {
        authorService = null;
        authorDAO = null;
        authors = null;
        myLogin = null;
    }

    @Test
    void test_getAuthorList() {
        assertEquals(authorService.getAuthorList(myLogin), authors);
    }

    @Test
    void test_getAuthor() {
        initializeAuthorAndAddToAuthors();
        authors.add(new Author(1));
        assertEquals(myAuthor, authorService.getAuthor(2, myLogin));
    }

    @Test
    void test_deleteIfPossible_when_books_size_equals_to_0() {
        initializeAuthorAndAddToAuthors();
        myAuthor.setBooks(new HashSet<>());
        assertTrue(authorService.deleteIfPossible(2, myLogin));
    }

    @Test
    void test_deleteIfPossible_when_books_size_is_greater_than_0() {
        initializeAuthorAndAddToAuthors();
        Set<Book> books = new HashSet<>();
        books.add(new Book());
        myAuthor.setBooks(books);
        String message = "Can not delete selected author. There are some books assigned to this author, " +
                "delete these books or change the author first.";
        assertAll(
                () -> assertFalse(authorService.deleteIfPossible(2, myLogin)),
                () -> assertEquals(message, authorService.getMessage())
        );
    }



    private void initializeAuthorAndAddToAuthors() {
        myAuthor = new Author(2);
        authors.add(myAuthor);
    }
}