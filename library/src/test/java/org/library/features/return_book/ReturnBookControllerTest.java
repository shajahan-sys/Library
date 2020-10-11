package org.library.features.return_book;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.library.features.login.Login;
import org.library.features.reader.Reader;
import org.mockito.*;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReturnBookControllerTest {
    private ReturnBookController returnBookController;
    @Spy
    private HttpSession session;
    @Mock
    private HttpServletResponse resp;
    @Mock
    private HttpServletRequest req;
    @Mock
    private ReturnBookService returnBookService;
    private Login login;

    @BeforeEach
    void setUp() {
        returnBookController = new ReturnBookController();
        MockitoAnnotations.openMocks(this);
        login = new Login();
        when(req.getSession()).thenReturn(session);
        when(session.getAttribute("userLogin")).thenReturn(login);
        returnBookController.setReturnBookService(returnBookService);
    }

    @AfterEach
    void tearDown() {
        returnBookController = null;
        login = null;
    }

    @Test
    void test_removeSessionAttributeReader() {
        when(session.getAttribute("reader")).thenReturn(new Reader());
        returnBookController.removeSessionAttributeReader(req);
        verify(session).removeAttribute("reader");
    }

    @Test
    void test_resolveSubmit_when_selReader_equals_no_reader() {
        when(req.getParameter("selReader")).thenReturn("no reader");
        returnBookController.resolveSubmit(req);
        verifyNoInteractions(returnBookService);
    }

    @Test
    void test_resolveSubmit_when_selReader_is_proper_id() {
        when(req.getParameter("selReader")).thenReturn("1");
        Reader reader = new Reader(1);
        when(returnBookService.getReader(login, 1)).thenReturn(reader);
        returnBookController.resolveSubmit(req);
        assertAll(
                () -> verify(req).setAttribute("selectedReader", reader),
                () -> verify(req).setAttribute("lendings", reader.getLendings())
        );
    }

    @Test
    void test_setActiveReaders() {
        List<Reader> readers = new ArrayList<>();
        readers.add(new Reader());
        when(returnBookService.getActiveReadersList(login)).thenReturn(readers);
        returnBookController.setActiveReaders(req);
        verify(session).setAttribute("activeReaders", readers);
    }

    @Test
    void test_setProperRequestAttributes_when_button_equals_null_and_reader_exists() {
        Reader reader = new Reader(2);
        when(session.getAttribute("reader")).thenReturn(reader);
        returnBookController.setProperRequestAttributes(req);
        assertAll(
                () -> verify(req).setAttribute("selectedReader", reader),
                () -> verify(req).setAttribute("lendings", reader.getLendings())
        );
    }

    @Test
    void test_doPost_when_button_value_equals_return() throws IOException {
        when(req.getParameter("button")).thenReturn("return");
        when(req.getParameter("selected")).thenReturn("2");
        returnBookController.doPost(req, resp);
        assertAll(
                () -> verify(returnBookService).deleteLending(2),
                () -> verify(session).setAttribute("readersMightHaveChanged", true),
                () -> verify(session).setAttribute("booksMightHaveChanged", true),
                () -> verify(resp).sendRedirect("books")
        );
    }

    @Test
    void test_doPost_when_button_equals_cancel() throws IOException {
        when(req.getParameter("button")).thenReturn("cancel");
        returnBookController.doPost(req, resp);
        verify(resp).sendRedirect("menu");
    }

    @Test
    void test_doGet() throws ServletException, IOException {
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        RequestDispatcher rd = Mockito.mock(RequestDispatcher.class);
        when(req.getRequestDispatcher(captor.capture())).thenReturn(rd);
        returnBookController.doGet(req, resp);
        assertAll(
                () -> verify(req).getRequestDispatcher(captor.capture()),
                () -> assertEquals("returnBook.jsp", captor.getValue())
        );

    }
}