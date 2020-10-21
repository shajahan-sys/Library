package com.library.features.reader;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.library.features.lend_book.Lending;
import com.library.features.login.Login;
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
    private Login login;
    private List<Reader> readers;
    @Mock
    private ReaderService readerService;
    @Mock
    HttpServletResponse resp;
    @Mock
    HttpServletRequest req;
    @Mock
    RequestDispatcher rd;
    @Spy
    HttpSession session;
    private final ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        readerController = new ReaderController();
        login = new Login();
        when(req.getSession()).thenReturn(session);
        when(session.getAttribute("userLogin")).thenReturn(login);
        readerController.setReaderService(readerService);
    }

    @AfterEach
    void tearDown() {
        readerController = null;
        login = null;
    }

    @Test
    void test_setProperAttributes_when_save_attribute_is_null() {
        readers = new ArrayList<>();
        readers.add(new Reader());
        when(readerService.getReadersList(login)).thenReturn(readers);
        readerController.setProperAttributes(req);
        assertAll(
                () -> verify(readerService, Mockito.never()).deleteFromMap(login),
                () -> verify(session).setAttribute("readers", readers)
        );
    }

    @Test
    void test_setProperAttributes_when_save_attribute_is_true() {
        readers = new ArrayList<>();
        readers.add(new Reader());
        when(readerService.getReadersList(login)).thenReturn(readers);
        when(session.getAttribute("saved")).thenReturn(true);
        readerController.setProperAttributes(req);
        assertAll(
                () -> verify(readerService).deleteFromMap(login),
                () -> verify(session).removeAttribute("saved"),
                () -> verify(session).setAttribute("readers", readers)
        );
    }

    @Test
    void test_returnAction_when_selectedReaders_lendingSet_is_not_empty() throws IOException {
        reader = new Reader();
        Set<Lending> lending = new HashSet<>();
        lending.add(new Lending());
        reader.setLendings(lending);
        when(req.getParameter("selected")).thenReturn("1");
        when(readerService.getReader(login, 1)).thenReturn(reader);
        when(readerService.isReaderLendingSetEmpty(login, 1)).thenReturn(false);
        readerController.returnAction(req, resp);
        assertAll(
                () -> verify(session).setAttribute("reader", reader),
                () -> verify(resp).sendRedirect("return-book")
        );
    }

    @Test
    void test_lendAction() {
        reader = new Reader();
        when(req.getParameter("selected")).thenReturn("1");
        when(readerService.getReader(login, 1)).thenReturn(reader);
        readerController.lendAction(req);
        verify(session).setAttribute("selReader", reader);
    }

    @Test
    void test_deleteAction_when_selectedReaders_lendingSet_isEmpty() throws IOException, ServletException {
        readers = new ArrayList<>();
        readers.add(new Reader());
        when(req.getRequestDispatcher(captor.capture())).thenReturn(rd);
        when(req.getParameter("selected")).thenReturn("1");
        when(readerService.deleteIfPossible(login, 1)).thenReturn(true);
        when(readerService.getReadersList(login)).thenReturn(readers);
        readerController.deleteAction(req, resp);
        assertAll(
                () -> verify(readerService).deleteIfPossible(login, 1),
                () -> verify(session).setAttribute("readers", readers),
                () -> verify(req).getRequestDispatcher(captor.capture()),
                () -> assertEquals("reader.jsp", captor.getValue())
        );
    }


    @Test
    void test_editAction() {
        when(req.getParameter("selected")).thenReturn("1");
        reader = new Reader(1);
        when(readerService.getReader(login, 1)).thenReturn(reader);
        readerController.editAction(req);
        verify(session).setAttribute("edit", reader);
    }


    @Test
    void test_doPost_with_wrong_button_value() {
        when(req.getParameter("button")).thenReturn("wrong value");
        assertThrows(IllegalArgumentException.class, () -> readerController.doPost(req, resp));
    }

    @Test
    void test_doPost_when_button_name_equals_edit() throws ServletException, IOException {
        when(req.getParameter("button")).thenReturn("edit");
        when(req.getParameter("selected")).thenReturn("1");
        readerController.doPost(req, resp);
       verify(resp).sendRedirect("add-edit-reader");
    }

    @Test
    void test_doPost_when_button_name_equals_lend() throws ServletException, IOException {
        when(req.getParameter("button")).thenReturn("lend");
        when(req.getParameter("selected")).thenReturn("1");
        when(readerService.getReader(login, 1)).thenReturn(new Reader());
        readerController.doPost(req, resp);
         verify(resp).sendRedirect("lend-book");
    }

    @Test
    void test_doPost_when_button_name_equals_add_new() throws ServletException, IOException {
        when(req.getParameter("button")).thenReturn("add new");
        readerController.doPost(req, resp);
         verify(resp).sendRedirect("add-edit-reader");
    }

    @Test
    void test_doPost_when_button_name_equals_menu() throws ServletException, IOException {
        when(req.getParameter("button")).thenReturn("menu");
        readerController.doPost(req, resp);
        verify(resp).sendRedirect("menu");
    }
}