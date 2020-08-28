
package org.library.features.book;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.library.features.author.Author;
import org.library.features.login.Login;
import org.library.features.management.Management;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BookControllerTest {
    private BookController bookController;
    private List<Book> bookList;
    @Mock
    HttpSession session;
    @Mock
    private HttpServletRequest req;
    @Mock
    private BookService bookService = mock(BookService.class);
    @Mock
    HttpServletResponse resp;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        bookController = new BookController();
    }

    @AfterEach
    void tearDown() {
        bookController = null;
        bookService = null;
    }

    @Test
    void test_doPost_when_manage_button_was_clicked() throws IOException, ServletException {
        when(req.getSession()).thenReturn(session);
        when(req.getParameter("button")).thenReturn("manage");
        when(req.getParameter("selected")).thenReturn("1");
        bookController.setBookService(bookService);
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        bookController.doPost(req, resp);
        verify(resp).sendRedirect(captor.capture());
        assertEquals("management", captor.getValue());
    }

    @Test
    void test_doPost_when_logout_button_was_clicked() throws ServletException, IOException {
        when(req.getParameter("button")).thenReturn("logout");
        bookController.setBookService(bookService);
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        bookController.doPost(req, resp);
        verify(resp).sendRedirect(captor.capture());
        assertEquals("logout", captor.getValue());
    }

    @Test
    void test_doPost_when_request_doesnt_have_a_proper_button_parameter() {
        when(req.getParameter("button")).thenReturn("wrong button");
        assertThrows(IllegalArgumentException.class, () -> bookController.doPost(req, resp));
    }

    @Test
    void test_doGet_when_session_attribute_userLogin_is_null() {
        when(req.getSession()).thenReturn(session);
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        assertAll(
                () -> assertDoesNotThrow(() -> bookController.doGet(req, resp)),
                () -> verify(resp).sendRedirect(captor.capture()),
                () -> assertEquals("login", captor.getValue())
        );
    }

    @Test
    void test_doGet_when_userLogin_exists_but_request_parameter_button_doesnt() throws ServletException, IOException {
        when(req.getSession()).thenReturn(session);
        RequestDispatcher rd = mock(RequestDispatcher.class);
        when(req.getSession().getAttribute("userLogin")).thenReturn(new Login());
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        when(req.getRequestDispatcher(captor.capture())).thenReturn(rd);
        bookController.setBookService(bookService);
        bookController.doGet(req, resp);
        verify(req).getRequestDispatcher(captor.capture());
        assertEquals("book.jsp", captor.getValue());
    }

    @Test
    void test_doGet_when_request_parameter_button_equals_cancel() throws ServletException, IOException {
        when(req.getSession()).thenReturn(session);
        when(req.getParameter("button")).thenReturn("cancel");
        RequestDispatcher rd = mock(RequestDispatcher.class);
        when(req.getSession().getAttribute("userLogin")).thenReturn(new Login());
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        when(req.getRequestDispatcher(captor.capture())).thenReturn(rd);
        bookController.setBookService(bookService);
        bookController.doGet(req, resp);
        assertAll(
                () -> verify(bookService).setEditBookToNull(),
                () -> verify(req).getRequestDispatcher(captor.capture()),
                () -> assertEquals("book.jsp", captor.getValue())
        );
    }


    @Test
    void test_setManagementValueIfDoesntExist() {
        List<Book> list1 = getBookList();
        List<Book> list2 = getBookList();
        list2.forEach(book -> {
            if (book.getManagement() == null) {
                book.setManagement(new Management());
                book.getManagement().setReturnDate("available");
            }
        });
        bookController.setManagementValueIfDoesntExist(list1);
        assertAll(
                () -> assertEquals(list1.get(1).getId(), list2.get(1).getId()),
                () -> assertEquals(list1.get(1).getManagement().getReturnDate(), list2.get(1).getManagement().getReturnDate())
        );
    }

    @Test
    void test_resolveSave() {
        when(req.getParameter("editTitle")).thenReturn("Title");
        when(req.getParameter("author1")).thenReturn("1");
        when(req.getParameter("year")).thenReturn("2020");
        bookController.setBookService(bookService);
        assertAll(
                () -> assertDoesNotThrow(() -> bookController.resolveSave(req)),
                () -> verify(bookService).saveBook(any(Book.class))
        );
    }

    @Test
    void test_setProperListOfBooks_when_request_parameter_button_equals_search() {
        when(req.getParameter("button")).thenReturn("search");
        List<Book> list = new ArrayList<>();
        when(bookService.getBooksList(any(Login.class))).thenReturn(list);
        when(req.getParameter("author2")).thenReturn("no author");
        bookController.setBookService(bookService);
        bookController.setProperListOfBooks(req);
        assertAll(
                () -> verify(bookService).filterBooks(any(Book.class)),
                () -> verify(req).setAttribute("books", list)
        );
    }

    @Test
    void test_setProperListOfBooks_when_request_parameter_button_is_null() {
        List<Book> list = new ArrayList<>();
        when(bookService.getBooksList(any(Login.class))).thenReturn(list);
        bookController.setBookService(bookService);
        bookController.setProperListOfBooks(req);
        verify(req).setAttribute("books", list);
    }

    @Test
    void test_setProperListOfAuthors() {
        List<Author> list = new ArrayList<>();
        when(bookService.getAuthorsList(any(Login.class))).thenReturn(list);
        bookController.setBookService(bookService);
        bookController.setProperListOfAuthors(req);
        verify(req).setAttribute("authors", list);
    }

    @Test
    void test_resolveDelete() {
        bookController.setBookService(bookService);
        when(req.getParameter("selected")).thenReturn("1");
        bookController.resolveDelete(req);
        verify(bookService).deleteBook(any(Book.class));
    }
    @Test
    void test_resolveEdit(){
        Book book = new Book(1);
          when(req.getParameter("selected")).thenReturn("1");
        when(bookService.getBook(any(Book.class))).thenReturn(book);
        bookController.setBookService(bookService);
        bookController.resolveEdit(req);
        verify(req).setAttribute("edit", book);

    }
    @Test
    void test_filterBooks_when_request_parameter_author2_equals_no_author() {
        when(req.getParameter("searchTitle")).thenReturn("ww");
        when(req.getParameter("author2")).thenReturn("no author");
        bookController.setBookService(bookService);
        bookController.filterBooks(req);
        verify(bookService).filterBooks(any(Book.class));
    }
    @Test
    void test_filterBooks_when_request_parameter_author2_doesnt_equal_no_author(){
        when(req.getParameter("searchTitle")).thenReturn("ww");
        when(req.getParameter("author2")).thenReturn("2");
        bookController.setBookService(bookService);
        bookController.filterBooks(req);
        assertAll(
                () -> verify(req, atLeast(2)).getParameter("author2"),
                () -> verify(bookService).filterBooks(any(Book.class))
        );
    }

    @Test
    void test_resolve_manage() {
        createBookList();
        when(req.getSession()).thenReturn(session);
        when(req.getParameter("selected")).thenReturn("1");
        bookController.setBookService(bookService);
        bookController.resolveManage(req);
        verify(bookService).setEditBookToNull();
    }

    private void createBookList() {
        bookList = Arrays.asList(
                new Book(0),
                new Book(1),
                new Book(2)
        );
        bookList.forEach(
                book -> book.setTitle("title" + book.getId())
        );
    }

    private List<Book> getBookList() {
        createBookList();
        return bookList;
    }
}