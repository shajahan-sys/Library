package org.library.features.book;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.library.features.author.Author;
import org.library.features.login.Login;
import org.library.hibernate_util.HibernateTestUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BookDAOImplTest {
    private BookDAOImpl impl;
    private SessionFactory sessionFactory;
    private Login login;
    private Author author;
    private Book book;

    @BeforeEach
    void setUp() {
        impl = new BookDAOImpl();
        sessionFactory = HibernateTestUtil.getTestSessionFactory();
        impl.setSessionFactory(sessionFactory);
        createSaveLogin();
    }

    @AfterEach
    void tearDown() {
        impl = null;
    }

    @Test
    void shouldSaveBookProperly() {
        createAndSaveAuthor();
        book = new Book();
        book.setLogin(login);
        book.setPublicationYear("2012");
        book.setAuthor(author);
        book.setTitle("Test");
        impl.save(book);
        Session session = sessionFactory.openSession();
        session.getTransaction().begin();
        Book result = (Book) session.createQuery("from Book where user_id = :id")
                .setParameter("id", login.getId()).getSingleResult();
        session.getTransaction().commit();
        session.close();
        assertAll(
                () -> assertEquals(book.getId(), result.getId()),
                () -> assertEquals(book.getLogin().getId(), result.getLogin().getId())
        );
        deleteLoginAuthorBookFromDB();
    }

    @Test
    void shouldFetchProperListOfBooksFormDB() {
        createAndSaveAuthor();
        List<Book> books = Arrays.asList(
                new Book(),
                new Book(),
                new Book());
        books.forEach(b -> {
            b.setTitle("O");
            b.setAuthor(author);
            b.setPublicationYear("0909");
            b.setLogin(login);
        });
        Session session = sessionFactory.openSession();
        session.getTransaction().begin();
        books.forEach(session::save);
        session.getTransaction().commit();
        session.close();
        assertEquals(books.size(), impl.getBooksList(login).size());
        deleteListOfObjects(books);
    }

    @Test
    void shouldFetchListOfAuthorsThatBelongsToGivenLogin() {
        List<Author> authors = Arrays.asList(
                new Author(),
                new Author(),
                new Author()
        );
        authors.forEach(a -> {
            a.setSurname("p");
            a.setLogin(login);
        });
        Session session = sessionFactory.openSession();
        session.getTransaction().begin();
        authors.forEach(session::save);
        session.getTransaction().commit();
        session.close();
        assertAll(
                () -> assertEquals(authors.size(), impl.getAuthorsList(login).size()),
                () -> assertEquals(login.getId(), impl.getAuthorsList(login).get(0).getLogin().getId())
        );
        deleteListOfObjects(authors);
    }

    @Test
    void shouldDeleteBookFromDB() {
        createAndSaveAuthor();
        Book book = new Book();
        book.setTitle("oki");
        book.setLogin(login);
        book.setPublicationYear("2020");
        book.setAuthor(author);
        Session session = sessionFactory.openSession();
        session.getTransaction().begin();
        session.save(book);
        session.getTransaction().commit();
        session.close();
        impl.delete(book);
        ArrayList emptyList = new ArrayList();
        assertEquals(emptyList.size(), impl.getBooksList(login).size());
        deleteLoginAuthorBookFromDB();
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

    void deleteLoginAuthorBookFromDB() {
        Session session = sessionFactory.openSession();
        session.getTransaction().begin();
        if (book != null) {
            session.delete(book);
        }
        session.delete(author);
        session.delete(login);
        session.getTransaction().commit();
        session.close();
    }

    void deleteListOfObjects(List<?> objects) {
        Session session = sessionFactory.openSession();
        session.getTransaction().begin();
        objects.forEach(session::delete);
        if (objects.get(0) instanceof Book) {
            session.delete(author);
        }
        session.delete(login);
        session.getTransaction().commit();
        session.close();
    }
}