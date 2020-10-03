package org.library.features.login;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
import java.io.PrintWriter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

class LoginControllerTest {
    @Mock
    private HttpServletRequest req;
    private LoginController loginController;
    @Mock
    private LoginService service;
    @Mock
    private HttpServletResponse resp;
    @Spy
    private HttpSession session;
    private final ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        loginController = new LoginController();
    }

    @AfterEach
    void tearDown() {
        loginController = null;
    }

    @Test
    void test_getLoginWithProperData() {
        String uName = "randomName";
        String uPass = "pass";
        when(req.getParameter("username")).thenReturn(uName);
        when(req.getParameter("password")).thenReturn(uPass);
        Login expectedLogin = loginController.getLoginWithProperData(req);
        assertAll(
                () -> assertEquals(expectedLogin.getUserName(), uName),
                () -> assertEquals(expectedLogin.getPassword(), uPass)
        );
    }

    @Test
    void test_doGet() throws ServletException, IOException {
        RequestDispatcher rd = mock(RequestDispatcher.class);
        when(req.getRequestDispatcher(captor.capture())).thenReturn(rd);
        loginController.doGet(req, resp);
        assertAll(
                () -> verify(req).getRequestDispatcher(captor.capture()),
                () -> assertEquals("login.jsp", captor.getValue()));
    }

    @Test
    void test_doPost_when_service_method_loginIfPossible_returns_true() throws IOException {
        Login myLogin = new Login();
        when(req.getParameter("command")).thenReturn("login");
        when(service.getLogin(any(Login.class))).thenReturn(myLogin);
        when(service.loginIfPossible(any(Login.class), anyString())).thenReturn(true);
        when(req.getSession()).thenReturn(session);
        loginController.setLoginService(service);
        loginController.doPost(req, resp);
        assertAll(
                () -> verify(session).setAttribute("userLogin", myLogin),
                () -> verify(resp).sendRedirect(captor.capture()),
                () -> assertEquals("menu", captor.getValue())
        );
    }

    @Test
    void test_doPost_when_service_method_loginIfPossible_returns_false() throws IOException {
        when(service.loginIfPossible(any(Login.class), anyString())).thenReturn(false);
        loginController.setLoginService(service);
        PrintWriter pw = mock(PrintWriter.class);
        when(resp.getWriter()).thenReturn(pw);
        loginController.doPost(req, resp);
        assertAll(
                () -> verify(resp).getWriter(),
                () -> verify(service).getMessage()
        );
    }

}