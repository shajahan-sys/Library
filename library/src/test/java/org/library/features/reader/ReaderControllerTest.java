package org.library.features.reader;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.library.features.lend_book.Lending;
import org.library.features.login.Login;
import org.mockito.*;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReaderControllerTest {
    private ReaderController readerController;
    private Reader reader;
    @Mock
    private ReaderService service;
    @Mock
    HttpServletResponse resp;
    @Mock
    HttpServletRequest req;
    @Spy
    HttpSession session;
    private final ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        readerController = new ReaderController();
    }

    @AfterEach
    void tearDown() {
        readerController = null;
    }

    @Test
    void test_setProperAttributesForwardRequest_when_session_attribute_readers_is_null() throws ServletException, IOException {
        readerController.setReaderService(service);
        readerController.setSession(session);
        RequestDispatcher rd = mock(RequestDispatcher.class);
        when(req.getRequestDispatcher(captor.capture())).thenReturn(rd);
        List<Reader> list = new ArrayList<>();
        when(service.getReadersList(new Login())).thenReturn(list);
        when(session.getAttribute("readers")).thenReturn(null);
        readerController.setProperAttributesForwardRequest(req, resp);
        assertAll(() -> verify(session).setAttribute("readers", list),
                () -> verify(req).getRequestDispatcher(captor.capture()),
                () -> assertEquals("reader.jsp", captor.getValue()));
    }

    @Test
    void test_resolveReturn_when_selectedReaders_lendingSet_is_not_empty() throws IOException {
        readerController.setReaderService(service);
        readerController.setSession(session);
        reader = new Reader();
        Set<Lending> lending = new HashSet<>();
        lending.add(new Lending());
        reader.setLendings(lending);
        when(req.getParameter("selected")).thenReturn("1");
        when(service.getReader(1)).thenReturn(reader);
        readerController.createProperSelectedReader(req);
        readerController.resolveReturn(resp);
        assertAll(
                () -> verify(session).setAttribute("lendings", lending),
                () -> verify(session).setAttribute("reader", reader),
                () -> verify(resp).sendRedirect(captor.capture()),
                () -> assertEquals("return-book", captor.getValue())
        );
    }

    @Test
    void test_resolveLend() {
        readerController.setSession(session);
        reader = new Reader();
        when(req.getParameter("selected")).thenReturn("1");
        when(service.getReader(1)).thenReturn(reader);
        readerController.setReaderService(service);
        readerController.createProperSelectedReader(req);
        readerController.resolveLend();
        verify(session).setAttribute("selReader", reader);
    }

    @Test
    void test_resolveDelete_when_selectedReaders_lendingSet_isEmpty() throws IOException, ServletException {
        readerController.setReaderService(service);
        readerController.setSession(session);
        reader = new Reader();
        List<Reader> readers = new ArrayList<>();
        Set<Lending> lending = new HashSet<>();
        reader.setLendings(lending);
        RequestDispatcher rd = mock(RequestDispatcher.class);
        when(req.getRequestDispatcher(captor.capture())).thenReturn(rd);
        when(req.getParameter("selected")).thenReturn("1");
        when(service.getReader(1)).thenReturn(reader);
        when(service.getReadersList(new Login())).thenReturn(readers);
        readerController.createProperSelectedReader(req);
        readerController.resolveDelete(req, resp);
        assertAll(
                () -> verify(service).delete(reader),
                () -> verify(session).setAttribute("readers", readers),
                () -> verify(req).getRequestDispatcher(captor.capture()),
                () -> assertEquals("reader.jsp", captor.getValue())
        );
    }

    @Test
    void test_resolveEdit() {
        reader = new Reader();
        readerController.setSession(session);
        when(req.getParameter("selected")).thenReturn("1");
        when(service.getReader(1)).thenReturn(reader);
        readerController.setReaderService(service);
        readerController.createProperSelectedReader(req);
        readerController.resolveEdit();
        verify(session).setAttribute("edit", reader);
    }

    @Test
    void test_createProperSelectedReader() {
        when(req.getParameter("selected")).thenReturn("1");
        readerController.setReaderService(service);
        readerController.createProperSelectedReader(req);
        verify(service).getReader(1);
    }

    @Test
    void test_doPost_with_wrong_button_value() {
        when(req.getParameter("button")).thenReturn("wrong value");
        assertThrows(IllegalArgumentException.class, () -> readerController.doPost(req, resp));
    }

    @Test
    void test_doPost_when_button_name_equals_edit() throws ServletException, IOException {
        when(req.getParameter("button")).thenReturn("edit");
        readerController.setSession(session);
        readerController.doPost(req, resp);
        assertAll(
                () -> verify(resp).sendRedirect(captor.capture()),
                () -> assertEquals("add-edit-reader", captor.getValue())
        );
    }

    @Test
    void test_doPost_when_button_name_equals_lend() throws ServletException, IOException {
        when(req.getParameter("button")).thenReturn("lend");
        readerController.setSession(session);
        readerController.doPost(req, resp);
        assertAll(
                () -> verify(resp).sendRedirect(captor.capture()),
                () -> assertEquals("lend-book", captor.getValue())
        );
    }

    @Test
    void test_doPost_when_button_name_equals_add_new() throws ServletException, IOException {
        when(req.getParameter("button")).thenReturn("add new");
        readerController.doPost(req, resp);
        assertAll(
                () -> verify(resp).sendRedirect(captor.capture()),
                () -> assertEquals("add-edit-reader", captor.getValue())
        );
    }

    @Test
    void test_doPost_when_button_name_equals_menu() throws ServletException, IOException {
        when(req.getParameter("button")).thenReturn("menu");
        readerController.doPost(req, resp);
        assertAll(
                () -> verify(resp).sendRedirect(captor.capture()),
                () -> assertEquals("menu", captor.getValue())
        );
    }

    @Test
    void test_doGet_when_login_equals_null() throws ServletException, IOException {
        readerController.setSession(session);
        readerController.doGet(req, resp);
        assertAll(
                () -> verify(resp).sendRedirect(captor.capture()),
                () -> assertEquals("login", captor.getValue())
        );
    }
}