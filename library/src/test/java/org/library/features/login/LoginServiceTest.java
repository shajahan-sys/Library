package org.library.features.login;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.library.hibernate_util.HibernateTestUtil;

import javax.persistence.Query;

import static org.junit.jupiter.api.Assertions.*;

class LoginServiceTest {
    private Login login;
    private LoginService service;
    private SessionFactory sessionFactory;

    @BeforeEach
    void setUp() {
        sessionFactory = HibernateTestUtil.getTestSessionFactory();
        service = new LoginService();
        service.setSessionFactory(sessionFactory);
        createLogin();
    }

    @AfterEach
    void tearDown() {
        deleteLoginFormTestDB(login);
        sessionFactory.close();
        service = null;
        login = null;
    }

    @Test
    void shouldAllowCreatingNewAccountWithTheSameLoginJustOnce() {
        String expected = "User already exist, try different login or login instead of creating new account";
        assertAll(
                () -> assertTrue(service.createNewAccount(login)),
                () -> assertFalse(service.createNewAccount(login)),
                () -> assertEquals(expected, service.getMessage())
        );
    }
    @Test
    void shouldReturnProperMessageAndFalseIfUserDoesntExist(){
        String expected = "There is no such a user";
        assertAll(
                () -> assertFalse(service.loginToProperAccount(login)),
                () -> assertEquals(expected, service.getMessage())
        );
    }
    @Test
    void shouldAllowUserToLoginToAccountThatAlreadyExistsIfProvidedPasswordIsCorrect() {
        service.createNewAccount(login);
        createLogin();
        assertTrue(service.loginToProperAccount(login));
    }

    @Test
    void shouldntAllowUserToLoginWithWrongPassword() {
        String expected = "Wrong password!";
        service.createNewAccount(login);
        createLogin();
        login.setPassword("wrong password");
        assertAll(
                () -> assertFalse(service.loginToProperAccount(login)),
                () -> assertEquals(expected, service.getMessage())
        );
    }

    private void createLogin() {
        login = new Login();
        login.setUserName("test");
        login.setPassword("correct password");
    }

    private void deleteLoginFormTestDB(Login login) {
        Session session = sessionFactory.openSession();
        session.getTransaction().begin();
        Query delete = session.createQuery("delete from Login where user_name = :username")
                .setParameter("username", login.getUserName());
        delete.executeUpdate();
        session.getTransaction().commit();
        session.close();
    }
}