
package org.library.features.book;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.library.features.author.Author;
import org.library.features.login.Login;
import org.library.features.lend_book.Lending;
import org.library.features.reader.Reader;
import org.mockito.*;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BookControllerTest {
    private BookController bookController;
    private List<Book> books;
    private Login login;
    @Mock
    private PrintWriter pw;
    @Spy
    HttpSession session;
    @Mock
    private HttpServletRequest req;
    @Mock
    private BookService bookService = mock(BookService.class);
    @Mock
    private RequestDispatcher rd;
    @Mock
    HttpServletResponse resp;
    private final ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        bookController = new BookController();
        login = new Login();
        when(session.getAttribute("userLogin")).thenReturn(login);
        when(req.getSession()).thenReturn(session);
        bookController.setBookService(bookService);
    }

    @AfterEach
    void tearDown() {
        bookController = null;
        bookService = null;
    }

    @Test
    void test_getFilteredBooks_when_author2_parameter_equals_no_author() {
        when(req.getParameter("author2")).thenReturn("no author");
        bookController.getFilteredBooks(req);
        assertAll(
                () -> verify(req).getParameter("searchTitle"),
                () -> verify(req, Mockito.times(1)).getParameter("author2"),
                () -> verify(session).getAttribute("userLogin"),
                () -> verify(bookService).filterBooks(any(Book.class), any(Login.class))
        );
    }

    @Test
    void test_getFilteredBooks_when_author2_parameter_is_proper() {
        when(req.getParameter("author2")).thenReturn("1");
        bookController.getFilteredBooks(req);
        assertAll(
                () -> verify(req).getParameter("searchTitle"),
                () -> verify(req, Mockito.times(2)).getParameter("author2"),
                () -> verify(session).getAttribute("userLogin"),
                () -> verify(bookService).filterBooks(any(Book.class), any(Login.class))
        );

    }

    @Test
    void test_lendAction_when_lending_is_null() throws IOException {
        Book book = new Book(1);
        when(req.getParameter("selected")).thenReturn("1");
        when(bookService.isLendingNull(login, 1)).thenReturn(true);
        when(bookService.getBook(login, 1)).thenReturn(book);
        bookController.lendAction(req, resp);
        assertAll(
                () -> verify(session).setAttribute("selBook", book),
                () -> verify(resp).sendRedirect("lend-book")
        );
    }

    @Test
    void test_lendAction_when_lending_is_not_null() throws IOException {
        when(req.getParameter("selected")).thenReturn("1");
        when(bookService.isLendingNull(login, 1)).thenReturn(false);
        when(resp.getWriter()).thenReturn(pw);
        bookController.lendAction(req, resp);
        assertAll(
                () -> verify(bookService).isLendingNull(any(Login.class), anyInt()),
                () -> verify(bookService).getMessage(),
                () -> verifyNoMoreInteractions(bookService),
                () -> verify(resp).getWriter()
        );
    }

    @Test
    void test_deleteAction_when_book_can_be_deleted() throws IOException, ServletException {
        books = new ArrayList<>();
        books.add(new Book(1));
        when(req.getParameter("selected")).thenReturn("1");
        when(bookService.deleteIfPossible(login, 1)).thenReturn(true);
        when(bookService.getBooksList(login)).thenReturn(books);
        when(req.getRequestDispatcher(anyString())).thenReturn(rd);
        bookController.deleteAction(req, resp);
        assertAll(
                () -> verify(session).setAttribute("books", books),
                () -> verify(req).getRequestDispatcher(captor.capture()),
                () -> assertEquals("book.jsp", captor.getValue())

        );
    }

    @Test
    void test_deleteAction_when_book_can_not_be_deleted() throws IOException, ServletException {
        when(req.getParameter("selected")).thenReturn("1");
        when(bookService.deleteIfPossible(login, 1)).thenReturn(false);
        when(resp.getWriter()).thenReturn(pw);
        bookController.deleteAction(req, resp);
        assertAll(
                () -> verify(bookService).deleteIfPossible(login, 1),
                () -> verify(resp).getWriter(),
                () -> verify(bookService).getMessage(),
                () -> verifyNoMoreInteractions(bookService)
        );
    }

    @Test
    void test_editAction() throws IOException {
        Book myBook = new Book();
        when(bookService.getBook(login, 1)).thenReturn(myBook);
        when(req.getParameter("selected")).thenReturn("1");
        bookController.editAction(req, resp);
        assertAll(
                () -> verify(resp).sendRedirect("add-edit-book"),
                () -> verify(session).setAttribute("edit", myBook)
        );
    }

    @Test
    void test_returnAction_when_lending_is_not_null() throws IOException {
        Book myBook = new Book();
        Reader myReader = new Reader();
        Lending lending = new Lending();
        lending.setReader(myReader);
        myBook.setLending(lending);
        when(bookService.getBook(login, 1)).thenReturn(myBook);
        when(req.getParameter("selected")).thenReturn("1");
        when(bookService.isLendingNull(login, 1)).thenReturn(false);
        bookController.returnAction(req, resp);
        assertAll(
                () -> verify(session).setAttribute("reader", myReader),
                () -> verify(session).setAttribute("lendings", myReader.getLendings()),
                () -> verify(resp).sendRedirect("return-book")
        );
    }

    @Test
    void test_returnAction_when_lending_is_null() throws IOException {
        when(req.getParameter("selected")).thenReturn("1");
        when(bookService.isLendingNull(login, 1)).thenReturn(true);
        when(resp.getWriter()).thenReturn(pw);
        bookController.returnAction(req, resp);
        assertAll(
                () -> verify(bookService).isLendingNull(any(Login.class), anyInt()),
                () -> verify(bookService).getMessage(),
                () -> verifyNoMoreInteractions(bookService),
                () -> verify(resp).getWriter()
        );
    }

    @Test
    void test_setProperListOfAuthors_when_session_attrinute_authorList_is_null() {
        List<Author> myList = new ArrayList<>();
        myList.add(new Author(1));
        when(bookService.getAuthorsList(login)).thenReturn(myList);
        bookController.setProperListOfAuthors(req);
        verify(session).setAttribute("authorList", myList);
    }

    @Test
    void test_setProperListOfAuthors_when_session_attribute_authorList_already_exists() {
        when(session.getAttribute("authorList")).thenReturn(new ArrayList<Author>());
        bookController.setProperListOfAuthors(req);
        verifyNoInteractions(bookService);
    }

    @Test
    void test_setProperListOfBooks_when_attribute_saved_is_true_and_button_vaule_is_null() {
        books = new ArrayList<>();
        books.add(new Book());
        when(session.getAttribute("saved")).thenReturn(true);
        when(bookService.getBooksList(login)).thenReturn(books);
        bookController.setProperListOfBooks(req);
        assertAll(
                () -> verify(bookService).deleteFromMap(login),
                () -> verify(session).removeAttribute("saved"),
                () -> verify(session).setAttribute("books", books)
        );
    }

    @Test
    void test_doPost_when_button_value_equals_add_new() throws IOException, ServletException {
        when(req.getParameter("button")).thenReturn("add new");
        bookController.doPost(req, resp);
        verify(resp).sendRedirect("add-edit-book");
    }

    @Test
    void test_doPost_when_request_does_not_have_a_proper_button_parameter() {
        when(req.getParameter("button")).thenReturn("wrong button");
        assertThrows(IllegalArgumentException.class, () -> bookController.doPost(req, resp));
    }

    @Test
    void test_doPost_when_button_value_equals_menu() throws ServletException, IOException {
        when(req.getParameter("button")).thenReturn("menu");
        bookController.doPost(req, resp);
        verify(resp).sendRedirect("menu");

    }
}