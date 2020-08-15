package org.library.features.login;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class LoginControllerTest {
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    private LoginController loginController;
    private Login properLogin;
    private Login wrongLogin;
    private final LoginService service = mock(LoginService.class);


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
    void shouldReturnTrueOnlyIfUserProvidedBothParameters() {
        mockProperLoginData();
        assertTrue(loginController.isInputProper(request));
    }

    @Test
    void shouldReturnFalseWhenThereIsNoLoginProvided() {
        when(request.getParameter("username")).thenReturn("");
        when(request.getParameter("password")).thenReturn("pass");
        assertFalse(loginController.isInputProper(request));
    }

    @Test
    void shouldReturnFalseWhenThereIsNoPasswordProvided() {
        when(request.getParameter("username")).thenReturn("login");
        when(request.getParameter("password")).thenReturn("");
        assertFalse(loginController.isInputProper(request));
    }

    @Test
    void shouldThrowAnExceptionWhenValueIsnt0Or1() {
        mockProperLoginData();
        mockValue("2");
        loginController.isInputProper(request);
        assertThrows(IllegalArgumentException.class, () -> loginController.resolveAction(new Login()));
    }

    @Test
    void shouldTryToLoginToProperAccountAndReturnTrueIfLoginIsCorrect() {
        beforeTestingResolveAction("0");
        when(service.loginToProperAccount(properLogin)).thenReturn(true);
        when(service.loginToProperAccount(wrongLogin)).thenReturn(false);
        assertAll(
                () -> assertTrue(loginController.resolveAction(properLogin)),
                () -> assertFalse(loginController.resolveAction(wrongLogin))
        );
    }

    @Test
    void shouldTryToCreateNewAccountAndReturnTrueIfLoginIsCorrect() {
        beforeTestingResolveAction("1");
        when(service.createNewAccount(properLogin)).thenReturn(true);
        when(service.createNewAccount(wrongLogin)).thenReturn(false);
        assertAll(
                () -> assertTrue(loginController.resolveAction(properLogin)),
                () -> assertFalse(loginController.resolveAction(wrongLogin))
        );
    }

    @Test
    void shouldWorkIfValueEquqls0() {
        mockProperLoginData();
        mockValue("0");
        loginController.isInputProper(request);
        assertDoesNotThrow(() -> loginController.isLoginAndPasswordValid());
    }

    @Test
    void shouldWorkIfValueEquals1() {
        mockProperLoginData();
        mockValue("1");
        loginController.isInputProper(request);
        assertDoesNotThrow(() -> loginController.isLoginAndPasswordValid());
    }

    @Test
    void shouldThrowExceptionIfIsInputProperMethodWasntCalledBefore() {
        mockProperLoginData();
        mockValue("0");
        assertThrows(NullPointerException.class, () -> loginController.isLoginAndPasswordValid());
    }

    @Test
    void shouldThrowExceptionIfValueIsNot0Or1() {
        mockProperLoginData();
        mockValue("3");
        loginController.isInputProper(request);
        assertThrows(IllegalArgumentException.class, () -> loginController.isLoginAndPasswordValid());
    }

    @Test
    void shouldPrintMessageWhenInputIsntCorrect() throws IOException {
        when(request.getParameter("username")).thenReturn("");
        when(request.getParameter("password")).thenReturn("aa");
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        when(response.getWriter()).thenReturn(pw);
        loginController.doPost(request, response);
        String result = sw.getBuffer().toString().trim();
        assertEquals("Provide login and password", result);
    }

    void mockProperLoginData() {
        when(request.getParameter("username")).thenReturn("login");
        when(request.getParameter("password")).thenReturn("password");
    }

    void mockValue(String number) {
        when(request.getParameter("command")).thenReturn(number);
    }

    void createWrongAndProperLogin() {
        properLogin = new Login();
        properLogin.setUserName("okey");
        properLogin.setPassword("right");
        wrongLogin = new Login();
        wrongLogin.setUserName("wrong");
        wrongLogin.setPassword("pass");
    }

    void beforeTestingResolveAction(String number) {
        mockValue(number);
        loginController.setLoginService(service);
        mockProperLoginData();
        createWrongAndProperLogin();
        loginController.isInputProper(request);
    }
}