package org.library.features.welcome;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.library.features.login.Login;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class WelcomeControllerTest {
    private WelcomeController welcomeController;
    @Mock
    private HttpServletRequest req;
    @Mock
    private HttpServletResponse resp;
    @Mock
    HttpSession session;
    private ArgumentCaptor<String> captor;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        welcomeController = new WelcomeController();
        captor = ArgumentCaptor.forClass(String.class);
    }

    @AfterEach
    void tearDown() {
        welcomeController = null;
        captor = null;
    }

    @Test
    void test_doPost_when_request_parameter_button_is_not_proper() {
        when(req.getParameter("button")).thenReturn("wrong button name");
        assertThrows(IllegalArgumentException.class, () -> welcomeController.doPost(req, resp));
    }
    @Test
    void test_doPost_when_request_parameter_button_equals_lend_book() throws IOException {
        when(req.getParameter("button")).thenReturn("lend book");
        welcomeController.doPost(req, resp);
        assertAll(
                () -> verify(resp).sendRedirect(captor.capture()),
                () -> assertEquals("/lending", captor.getValue())
        ); }
    @Test
    void test_doPost_when_request_parameter_button_equals_books() throws IOException {
        when(req.getParameter("button")).thenReturn("books");
        welcomeController.doPost(req, resp);
        assertAll(
                () -> verify(resp).sendRedirect(captor.capture()),
                () -> assertEquals("books", captor.getValue())
        );
    }
    @Test
    void test_doPost_when_request_parameter_button_equals_return_book() throws IOException {
        when(req.getParameter("button")).thenReturn("return book");
        welcomeController.doPost(req, resp);
        assertAll(
                () -> verify(resp).sendRedirect(captor.capture()),
                () -> assertEquals("/return-book", captor.getValue())
        );
    }
    @Test
    void test_doPost_when_request_parameter_button_equals_readers() throws IOException {
        when(req.getParameter("button")).thenReturn("readers");
        welcomeController.doPost(req, resp);
        assertAll(
                () -> verify(resp).sendRedirect(captor.capture()),
                () -> assertEquals("readers", captor.getValue())
        );
    }
    @Test
    void test_doPost_when_request_parameter_button_equals_authors() throws IOException {
        when(req.getParameter("button")).thenReturn("authors");
        welcomeController.doPost(req, resp);
        assertAll(
                () -> verify(resp).sendRedirect(captor.capture()),
                () -> assertEquals("authors", captor.getValue())
        );
    }
    @Test
    void test_doGet_when_login_is_not_null() throws ServletException, IOException {
        when(req.getSession()).thenReturn(session);
        RequestDispatcher rd = mock(RequestDispatcher.class);
        when(req.getSession().getAttribute("userLogin")).thenReturn(new Login());
        when(req.getRequestDispatcher(captor.capture())).thenReturn(rd);
        welcomeController.doGet(req, resp);
        assertAll(
                () -> verify(req).getRequestDispatcher(captor.capture()),
                () -> assertEquals("welcome.jsp", captor.getValue())
        );
    }
    @Test
    void test_doGet_when_login_is_null() throws ServletException, IOException {
        when(req.getSession()).thenReturn(session);
        welcomeController.doGet(req, resp);
        assertAll(
                () -> verify(resp).sendRedirect(captor.capture()),
                () -> assertEquals("login", captor.getValue())
        );
    }
}