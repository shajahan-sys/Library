package org.library.features.add_edit.author;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Test;
import org.library.features.add_edit.author.AddEditAuthorDAOImpl;
import org.library.features.author.Author;
import org.library.features.login.Login;
import org.library.hibernate_util.HibernateTestUtil;


import static org.junit.jupiter.api.Assertions.*;

class AddEditAuthorDAOImplTest {
    private Login login;
    private Author author;
    private SessionFactory sessionFactory;

    @Test
    void test_save(){
        sessionFactory = HibernateTestUtil.getTestSessionFactory();
        createLogin();
        AddEditAuthorDAOImpl addEditAuthorDAO = new AddEditAuthorDAOImpl();
        addEditAuthorDAO.setSessionFactory(sessionFactory);
        author = new Author();
        author.setLogin(login);
        author.setSurname("Surname");
        addEditAuthorDAO.save(author);
        Session session = sessionFactory.openSession();
        session.getTransaction().begin();
        Author actual = (Author) session.createQuery("from Author where user_id = :id")
                .setParameter("id", login.getId()).getSingleResult();
        session.getTransaction().commit();
        session.close();
        assertAll(
                () -> assertEquals(author.getId(), actual.getId()),
                () -> assertEquals(author.getSurname(), actual.getSurname()),
                () -> assertEquals(author.getLogin().getId(), actual.getLogin().getId())
        );
        deleteAuthorLoginFromDB();
    }
    private void createLogin(){
        login = new Login();
        login.setUserName("test");
        login.setPassword("pass");
        Session session = sessionFactory.openSession();
        session.getTransaction().begin();
        session.save(login);
        session.getTransaction().commit();
        session.close();
    }
   private void deleteAuthorLoginFromDB() {
        Session session = sessionFactory.openSession();
        session.getTransaction().begin();
        session.delete(author);
        session.delete(login);
        session.getTransaction().commit();
        session.close();
    }

}