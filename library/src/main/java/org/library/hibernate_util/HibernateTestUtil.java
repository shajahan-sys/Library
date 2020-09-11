package org.library.hibernate_util;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.library.features.author.Author;
import org.library.features.book.Book;
import org.library.features.login.Login;
import org.library.features.lend_book.Lending;
import org.library.features.reader.Reader;

public class HibernateTestUtil {
    private static SessionFactory testSessionFactory;
    private static void configureTest() {
        Configuration configuration = new Configuration().configure("hibernate-test.cfg.xml");
        configuration.addAnnotatedClass(Author.class);
        configuration.addAnnotatedClass(Book.class);
        configuration.addAnnotatedClass(Lending.class);
        configuration.addAnnotatedClass(Reader.class);
        configuration.addAnnotatedClass(Login.class);
        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties());
        testSessionFactory = configuration.buildSessionFactory(builder.build());
    }
    public static SessionFactory getTestSessionFactory() {
        configureTest();
        return testSessionFactory;
    }
}
