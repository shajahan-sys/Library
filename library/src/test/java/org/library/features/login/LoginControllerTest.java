package org.library.features.login;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.servlet.http.HttpServletRequest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class LoginControllerTest {
    @Mock
    private HttpServletRequest request;

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
    void shouldThrowAnExceptionWhenValueIsnt0Or1() {
        mockProperLoginData();
        mockValue("2");
       loginController.setProperVariables(request);
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
    void shouldWorkIfValueEquaqls0() {
        mockProperLoginData();
        mockValue("0");
        loginController.setProperVariables(request);
        assertDoesNotThrow(() -> loginController.isLoginAndPasswordValid());
    }

    @Test
    void shouldWorkIfValueEquals1() {
        mockProperLoginData();
        mockValue("1");
        loginController.setProperVariables(request);
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
        loginController.setProperVariables(request);
        assertThrows(IllegalArgumentException.class, () -> loginController.isLoginAndPasswordValid());
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
        loginController.setProperVariables(request);
    }
}