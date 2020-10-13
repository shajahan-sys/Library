package org.library.features.lend_book;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.library.features.book.Book;
import org.library.features.login.Login;
import org.library.features.reader.Reader;
import org.mockito.*;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class LendingControllerTest {
    private LendingController lendingController;
    @Mock
    private PrintWriter pw;
    @Spy
    private HttpSession session;
    @Mock
    private HttpServletResponse resp;
    @Mock
    private HttpServletRequest req;
    @Mock
    RequestDispatcher rd;
    @Mock
    private LendingService lendingService;

    @BeforeEach
    void setUp() {
        lendingController = new LendingController();
        MockitoAnnotations.openMocks(this);
        when(req.getSession()).thenReturn(session);

    }

    @AfterEach
    void tearDown() {
        lendingController = null;
    }

    @Test
    void test_removeSessionAttributes() {
        when(session.getAttribute("selReader")).thenReturn(new Reader());
        when(session.getAttribute("selBook")).thenReturn(new Book());
        lendingController.removeSessionAttributes(req);
        assertAll(
                () -> verify(session).removeAttribute("selBook"),
                () -> verify(session).removeAttribute("selReader")
        );
    }

    @Test
    void test_resolveLend_when_date_format_is_proper() throws IOException, ParseException {
        Login login = new Login();
        when(session.getAttribute("userLogin")).thenReturn(login);
        when(req.getParameter("book")).thenReturn("1");
        when(req.getParameter("date")).thenReturn("2020-09-09");
        when(req.getParameter("reader")).thenReturn("2");
        when(lendingService.isDateFormatProper("2020-09-09")).thenReturn(true);
        lendingController.setLendingService(lendingService);
        lendingController.resolveLend(req, resp);
        assertAll(
                () -> verify(lendingService).saveLending(any(Lending.class)),
                () -> verify(session).setAttribute("readersMightHaveChanged", true),
                () -> verify(session).setAttribute("booksMightHaveChanged", true),
                () -> verify(resp).sendRedirect("books")
        );
    }

    @Test
    void test_resolveLend_when_date_format_is_not_proper() throws IOException, ParseException {
        when(lendingService.isDateFormatProper(req.getParameter("date"))).thenReturn(false);
        lendingController.setLendingService(lendingService);
        when(resp.getWriter()).thenReturn(pw);
        lendingController.resolveLend(req, resp);
        verify(lendingService, Mockito.times(0)).saveLending(any(Lending.class));
    }

    @Test
    void test_getProperLendingObject() throws ParseException {
        Login login = new Login();
        Book myBook = new Book();
        Reader reader = new Reader();
        when(session.getAttribute("userLogin")).thenReturn(login);
        when(req.getParameter("book")).thenReturn("1");
        when(req.getParameter("date")).thenReturn("2020-09-09");
        when(req.getParameter("reader")).thenReturn("2");
        when(lendingService.getBook(login, 1)).thenReturn(myBook);
        when(lendingService.getReader(login, 2)).thenReturn(reader);
        lendingController.setLendingService(lendingService);
        Lending lendingToCheck = lendingController.getProperLendingObject(req);
        assertAll(
                () -> assertEquals(myBook, lendingToCheck.getBook()),
                () -> assertEquals(reader, lendingToCheck.getReader())
        );
    }

    @Test
    void test_setAttributes() {
        Login login = new Login();
        List<Book> books = new ArrayList<>();
        List<Reader> readers = new ArrayList<>();
        readers.add(new Reader());
        books.add(new Book());
        when(session.getAttribute("userLogin")).thenReturn(login);
        when(lendingService.getAvailableBooksList(login)).thenReturn(books);
        when(lendingService.getReadersList(login)).thenReturn(readers);
        lendingController.setLendingService(lendingService);
        lendingController.setAttributes(req);
        assertAll(
                () -> verify(session).setAttribute("avbBooks", books),
                () -> verify(session).setAttribute("readers", readers)
        );
    }

    @Test
    void test_doPost_when_button_value_equals_cancel() throws IOException {
        when(req.getParameter("button")).thenReturn("cancel");
        lendingController.doPost(req, resp);
        verify(resp).sendRedirect("menu");
    }

    @Test
    void test_doGet() throws IOException, ServletException {
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        when(req.getRequestDispatcher(captor.capture())).thenReturn(rd);
        lendingController.setLendingService(lendingService);
        lendingController.doGet(req, resp);
        assertAll(
                () -> verify(req).getRequestDispatcher(captor.capture()),
                () -> assertEquals("lending.jsp", captor.getValue())
        );
    }
}