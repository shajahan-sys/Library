package org.library.features.book;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.library.features.author.Author;
import org.library.features.login.Login;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * BookDAO implementation.
 *
 * @author Barbara Grabowska
 * @version %I%, %G%
 */
public class BookDAOImpl implements BookDAO {
    private SessionFactory sessionFactory;
    /**
     * Logger instance for this class
     */
    private final Logger logger = LoggerFactory.getLogger(BookDAOImpl.class);

    @Override
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Book> getBooksList(Login login) {
        Session session = sessionFactory.openSession();
        List<Book> books = session.createQuery("from Book where user_id = :id order by title")
                .setParameter("id", login.getId())
                .list();
        session.close();
        return books;
    }

    @Override
    public List<Author> getAuthorsList(Login login) {
        Session session = sessionFactory.openSession();
        List<Author> authors = session.createQuery("from Author where user_id = :id order by surname")
                .setParameter("id", login.getId())
                .list();
        session.close();
        return authors;
    }

    @Override
    public void delete(Book book) {
        Session session = sessionFactory.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.delete(book);
            tx.commit();
        } catch (HibernateException e) {
            logger.error(e.getMessage());
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }
}
