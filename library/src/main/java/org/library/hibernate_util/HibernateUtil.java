package org.library.hibernate_util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.library.features.author.Author;
import org.library.features.book.Book;
import org.library.features.login.Login;
import org.library.features.lending.Lending;
import org.library.features.reader.Reader;

public class HibernateUtil {
    private static SessionFactory sessionFactory ;

    private static final Logger logger = LogManager.getLogger(HibernateUtil.class);
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
    public static SessionFactory getSessionFactory() {
        configure();
        return sessionFactory;
    }

}
