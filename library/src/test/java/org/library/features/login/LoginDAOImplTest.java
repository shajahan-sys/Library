package org.library.features.login;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.library.hibernate_util.HibernateTestUtil;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.Query;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class LoginDAOImplTest {
    private LoginDAOImpl loginDAO;
    private SessionFactory sessionFactory;
    private Login properLogin;
    private Login invalidLogin;
    @Mock
    private LoginService service;

    @BeforeEach
    void setUp() {
        sessionFactory = HibernateTestUtil.getTestSessionFactory();
        loginDAO = new LoginDAOImpl();
        loginDAO.setSessionFactory(sessionFactory);

    }

    @AfterEach
    void tearDown() {
        loginDAO = null;
        sessionFactory.close();
    }

    @Test
    void theSameStringShouldNotBeTheSameAsHashedPassword() {
        String password = "root";
        String unexpected = loginDAO.hashPassword(password);
        String actual = loginDAO.hashPassword(password);
        assertNotEquals(unexpected, actual);
    }

    @Test
    void shouldHashPassword() {
        assertNotEquals("pass", loginDAO.hashPassword("pass"));
    }

    @Test
    void shouldReturnTrueIfUserAlreadyExistsInDB() {
        createProperLogin();
        loginDAO.setVerifier(service);
        saveProperLogin();
        invalidLogin = new Login();
        invalidLogin.setUserName("wrong user");
        invalidLogin.setPassword("pass");
        assertAll(
                () -> assertTrue(loginDAO.doesUserAlreadyExist(properLogin)),
                () -> assertFalse(loginDAO.doesUserAlreadyExist(invalidLogin))
        );
        deleteProperLogin();
    }

    @Test
    void shouldReturnTrueIfLoginAndPasswordIsProper() {
        createProperLogin();
        loginDAO.setVerifier(service);
        saveProperLogin();
        createProperLogin();
        loginDAO.doesUserAlreadyExist(properLogin);
        System.out.println(properLogin.getPassword());
        assertTrue(loginDAO.isLoginInputCorrect(properLogin));
        deleteProperLogin();
    }

    @Test
    void shouldReturnFalseWhenPasswordIsInvalid() {
        createProperLogin();
        saveProperLogin();
        loginDAO.setVerifier(service);
        Login properLoginWithWrongPassword = properLogin;
        properLoginWithWrongPassword.setPassword("wrongPassword");
        createProperLogin();
        loginDAO.doesUserAlreadyExist(properLoginWithWrongPassword);
        assertFalse(loginDAO.isLoginInputCorrect(properLoginWithWrongPassword));
        deleteProperLogin();
    }

    void saveProperLogin() {
        loginDAO.save(properLogin);
    }

    void deleteProperLogin() {
        Session session = sessionFactory.openSession();
        session.getTransaction().begin();
        Query delete = session.createQuery("delete from Login where user_name = :username")
                .setParameter("username", properLogin.getUserName());
        delete.executeUpdate();
        session.getTransaction().commit();
        session.close();
    }

    private void createProperLogin() {
        properLogin = new Login();
        properLogin.setUserName("proper user");
        properLogin.setPassword("root");
    }
}