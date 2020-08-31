package org.library.features.add_edit.reader;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Test;
import org.library.features.login.Login;
import org.library.features.reader.Reader;
import org.library.hibernate_util.HibernateTestUtil;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class AddEditReaderDAOImplTest {
    private Login login;
    private Reader reader;
    private SessionFactory sessionFactory;

    @Test
    void test_save() {
        sessionFactory = HibernateTestUtil.getTestSessionFactory();
        createLogin();
        AddEditReaderDAOImpl addEditReaderDAO = new AddEditReaderDAOImpl();
        addEditReaderDAO.setSessionFactory(sessionFactory);
        reader = new Reader();
        reader.setLogin(login);
        reader.setSurname("Surname");
        reader.setName("Name");
        addEditReaderDAO.save(reader);
        Session session = sessionFactory.openSession();
        session.getTransaction().begin();
        Reader actual = (Reader) session.createQuery("from Reader where user_id = :id")
                .setParameter("id", login.getId()).getSingleResult();
        session.getTransaction().commit();
        session.close();
        assertAll(
                () -> assertEquals(reader.getId(), actual.getId()),
                () -> assertEquals(reader.getSurname(), actual.getSurname()),
                () -> assertEquals(reader.getLogin().getId(), actual.getLogin().getId()),
                () -> assertEquals(reader.getName(), actual.getName())
        );
        deleteReaderLoginFromDB();
    }

    private void createLogin() {
        login = new Login();
        login.setUserName("test");
        login.setPassword("pass");
        Session session = sessionFactory.openSession();
        session.getTransaction().begin();
        session.save(login);
        session.getTransaction().commit();
        session.close();
    }

    private void deleteReaderLoginFromDB() {
        Session session = sessionFactory.openSession();
        session.getTransaction().begin();
        session.delete(reader);
        session.delete(login);
        session.getTransaction().commit();
        session.close();
    }

}