package org.library.features.menu;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.library.features.login.Login;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MenuControllerTest {
    private MenuController menuController;
    @Mock
    private HttpServletRequest req;
    @Mock
    private HttpServletResponse resp;
    @Spy
    HttpSession session;
    private final ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        menuController = new MenuController();}

    @AfterEach
    void tearDown() {
        menuController = null;
    }

    @Test
    void test_doPost_when_request_parameter_button_is_not_proper() {
        when(req.getParameter("button")).thenReturn("wrong button name");
        assertThrows(IllegalArgumentException.class, () -> menuController.doPost(req, resp));
    }
    @Test
    void test_doPost_when_request_parameter_button_equals_lend_book() throws IOException {
        when(req.getParameter("button")).thenReturn("lend book");
        menuController.doPost(req, resp);
        assertAll(
                () -> verify(resp).sendRedirect(captor.capture()),
                () -> assertEquals("lend-book", captor.getValue())
        ); }
    @Test
    void test_doPost_when_request_parameter_button_equals_books() throws IOException {
        when(req.getParameter("button")).thenReturn("books");
        menuController.doPost(req, resp);
        assertAll(
                () -> verify(resp).sendRedirect(captor.capture()),
                () -> assertEquals("books", captor.getValue())
        );
    }
    @Test
    void test_doPost_when_request_parameter_button_equals_return_book() throws IOException {
        when(req.getParameter("button")).thenReturn("return book");
        menuController.doPost(req, resp);
        assertAll(
                () -> verify(resp).sendRedirect(captor.capture()),
                () -> assertEquals("return-book", captor.getValue())
        );
    }
    @Test
    void test_doPost_when_request_parameter_button_equals_readers() throws IOException {
        when(req.getParameter("button")).thenReturn("readers");
        menuController.doPost(req, resp);
        assertAll(
                () -> verify(resp).sendRedirect(captor.capture()),
                () -> assertEquals("readers", captor.getValue())
        );
    }
    @Test
    void test_doPost_when_request_parameter_button_equals_authors() throws IOException {
        when(req.getParameter("button")).thenReturn("authors");
        menuController.doPost(req, resp);
        assertAll(
                () -> verify(resp).sendRedirect(captor.capture()),
                () -> assertEquals("authors", captor.getValue())
        );
    }
    @Test
    void test_doGet() throws ServletException, IOException {
        when(req.getSession()).thenReturn(session);
        RequestDispatcher rd = mock(RequestDispatcher.class);
        when(session.getAttribute("userLogin")).thenReturn(new Login());
        when(req.getRequestDispatcher(captor.capture())).thenReturn(rd);
        menuController.doGet(req, resp);
        assertAll(
                () -> verify(req).getRequestDispatcher(captor.capture()),
                () -> assertEquals("menu.jsp", captor.getValue())
        );
    }
}