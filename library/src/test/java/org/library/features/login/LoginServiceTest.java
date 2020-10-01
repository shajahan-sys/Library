package org.library.features.login;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class LoginServiceTest {
    private Login login;
    private LoginService loginService;
    private LoginDao loginDao;
    @Mock
    private LoginDAOImpl loginDaoImpl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        login = new Login();
        login.setPassword("pass");
        loginDao = loginDaoImpl;
        loginService = new LoginService();
        loginService.setLoginDao(loginDao);
    }

    @AfterEach
    void tearDown() {
        loginService = null;
        loginDao = null;
        login = null;
    }

    @Test
    void test_isLoginInputCorrect_when_password_is_correct() {
        String hashed = BCrypt.hashpw("pass", BCrypt.gensalt());
        when(loginDao.getHashedPassword(login)).thenReturn(hashed);
        assertAll(
                () -> assertTrue(loginService.isLoginInputCorrect(login)),
                () -> assertEquals(login.getPassword(), "not storing real pass")
        );

    }

    @Test
    void test_isLoginInputCorrect_when_password_is_not_correct() {
        String wrongHashedPass = BCrypt.hashpw("wrong", BCrypt.gensalt());
        when(loginDao.getHashedPassword(login)).thenReturn(wrongHashedPass);
        assertAll(
                () -> assertFalse(loginService.isLoginInputCorrect(login)),
                () -> assertEquals("Wrong password!", loginService.getMessage())
        );
    }

    @Test
    void test_createNewAccount() {
        loginService.createNewAccount(login);
        assertAll(
                () -> verify(loginDao).saveNewAccount(login),
                () -> assertEquals(login.getPassword(), "not storing real pass")
        );
    }

    @Test
    void test_loginIfPossible_when_action_equals_login_and_user_does_not_exist() {
        when(loginDao.doesUserAlreadyExist(login)).thenReturn(false);
        assertAll(
                () -> assertFalse(loginService.loginIfPossible(login, "login")),
                () -> assertEquals(loginService.getMessage(), "There is no user with such a name")
        );
    }

    @Test
    void test_loginIfPossible_when_action_equals_create_and_user_exists() {
        when(loginDao.doesUserAlreadyExist(login)).thenReturn(true);
        assertAll(
                () -> assertFalse(loginService.loginIfPossible(login, "create")),
                () -> assertEquals(loginService.getMessage(), "User already exists, try different name")
        );
    }

    @Test
    void test_loginIfPossible_when_action_name_is_wrong() {
        assertThrows(IllegalArgumentException.class, () -> loginService.loginIfPossible(login, "wrong name"));
    }
}
