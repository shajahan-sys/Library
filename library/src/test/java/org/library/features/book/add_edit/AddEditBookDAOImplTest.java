package org.library.features.book.add_edit;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Test;
import org.library.features.author.Author;
import org.library.features.book.Book;
import org.library.features.login.Login;
import org.library.hibernate_util.HibernateTestUtil;

import static org.junit.jupiter.api.Assertions.*;

class AddEditBookDAOImplTest {
    private SessionFactory sessionFactory;
    private Author author;
    private Login login;
    private Book book;

    @Test
    void test_save() {
        sessionFactory = HibernateTestUtil.getTestSessionFactory();
        AddEditBookDAOImpl addEditBookDAO = new AddEditBookDAOImpl();
        addEditBookDAO.setSessionFactory(HibernateTestUtil.getTestSessionFactory());
        createSaveLogin();
        createAndSaveAuthor();
        book = new Book();
        book.setLogin(login);
        book.setPublicationYear("2012");
        book.setAuthor(author);
        book.setTitle("Test");
        addEditBookDAO.save(book);
        Session session = sessionFactory.openSession();
        session.getTransaction().begin();
        Book result = (Book) session.createQuery("from Book where user_id = :id")
                .setParameter("id", login.getId()).getSingleResult();
        session.getTransaction().commit();
        session.close();
        assertAll(
                () -> assertEquals(book.getId(), result.getId()),
                () -> assertEquals(book.getPublicationYear(), result.getPublicationYear()),
                () -> assertEquals(book.getAuthor().getId(), result.getAuthor().getId()),
                () -> assertEquals(book.getTitle(), result.getTitle()),
                () -> assertEquals(book.getLogin().getId(), result.getLogin().getId())
        );
        deleteLoginAuthorBookFromDB();
    }

    void createAndSaveAuthor() {
        author = new Author();
        author.setLogin(login);
        author.setSurname("Name");
        Session session = sessionFactory.openSession();
        session.getTransaction().begin();
        session.save(author);
        session.getTransaction().commit();
        session.close();
    }

    void createSaveLogin() {
        login = new Login();
        login.setUserName("test");
        login.setPassword("pass");
        Session session = sessionFactory.openSession();
        session.getTransaction().begin();
        session.save(login);
        session.getTransaction().commit();
        session.close();
    }

    void deleteLoginAuthorBookFromDB() {
        Session session = sessionFactory.openSession();
        session.getTransaction().begin();
        session.delete(book);
        session.delete(author);
        session.delete(login);
        session.getTransaction().commit();
        session.close();
    }

}