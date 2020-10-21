package com.library.features.lend_book;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import com.library.features.book.Book;
import com.library.features.login.Login;
import com.library.features.reader.Reader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
/**
 * LendingDAO implementation.
 *
 * @author Barbara Grabowska
 * @version %I%, %G%
 */
public class LendingDAOImpl implements LendingDAO {
    private SessionFactory sessionFactory;
    /**
     * Logger instance for this class
     */
    private final Logger logger = LoggerFactory.getLogger(LendingDAOImpl.class);

    @Override
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Reader> getReadersList(Login login) {
        Session session = sessionFactory.openSession();
        List<Reader> readers = session.createQuery("from Reader where user_id = :id order by surname")
                .setParameter("id", login.getId())
                .list();
        session.close();
        return readers;
    }

    @Override
    public List<Book> getAllBooks(Login login) {
        Session session = sessionFactory.openSession();
        List<Book> books = session.createQuery("from Book where user_id = :id order by title")
                .setParameter("id", login.getId())
                .list();
        session.close();
        return books;
    }

    @Override
    public void save(Lending lending) {
        Session session = sessionFactory.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.saveOrUpdate(lending);
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
