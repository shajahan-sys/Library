package com.library.hibernate_util;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import com.library.features.author.Author;
import com.library.features.book.Book;
import com.library.features.login.Login;
import com.library.features.lend_book.Lending;
import com.library.features.reader.Reader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class provides a Hibernate SessionFactory object.
 *
 * @author Barbara Grabowska
 * @version %I%, %G%
 */
public class HibernateUtil {
    private static SessionFactory sessionFactory;
    /**
     * Logger instance for this class
     */
    private static final Logger logger = LoggerFactory.getLogger(HibernateUtil.class);

    /**
     * Configures and creates a proper SessionFactory object
     */
    private static void configure() {
        Configuration configuration = new Configuration().configure();
        configuration.addAnnotatedClass(Author.class);
        configuration.addAnnotatedClass(Book.class);
        configuration.addAnnotatedClass(Lending.class);
        configuration.addAnnotatedClass(Reader.class);
        configuration.addAnnotatedClass(Login.class);
        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties());
        sessionFactory = configuration.buildSessionFactory(builder.build());
        logger.debug("Configured SessionFactory");
    }

    /**
     * @return SessionFactory object
     */
    public static SessionFactory getSessionFactory() {
        configure();
        return sessionFactory;
    }

}
