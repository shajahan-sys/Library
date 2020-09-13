package org.library.features.lend_book;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.library.features.book.Book;
import org.library.features.book.BookDAOImpl;
import org.library.features.login.Login;
import org.library.features.reader.Reader;

import java.util.List;

public class LendingDAOImpl implements LendingBookDAO {
    private SessionFactory sessionFactory;
    private final Logger logger = LogManager.getLogger(LendingDAOImpl.class);

    @Override
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Reader> getReadersList(Login login) {
        List readers;
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.getTransaction();
            transaction.begin();
            readers = session.createQuery("from Reader where user_id = :id order by surname")
                    .setParameter("id", login.getId())
                    .getResultList();
            transaction.commit();
        } catch (RuntimeException e) {
            logger.error(e.getMessage());
            try {
                if (transaction != null)
                    transaction.rollback();
            } catch (HibernateException e1) {
                logger.error("Transaction rollback not successful");
            }
            throw e;
        }
        return readers;
    }

    @Override
    public List<Book> getAvailableBooksList(Login login) {
        List books;
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.getTransaction();
            transaction.begin();
            books = session.createQuery("from Book where user_id = :id order by title")
                    .setParameter("id", login.getId())
                    .getResultList();
            transaction.commit();
        } catch (RuntimeException e) {
            logger.error(e.getMessage());
            try {
                if (transaction != null)
                    transaction.rollback();
            } catch (HibernateException e1) {
                logger.error("Transaction rollback not successful");
            }
            throw e;
        }
        return books;
    }

    @Override
    public void save(Lending lending) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.getTransaction();
            transaction.begin();
            if(lending != null){
                session.saveOrUpdate(lending);
            }
            transaction.commit();
        } catch (RuntimeException e) {
            logger.error(e.getMessage());
            try {
                if (transaction != null)
                    transaction.rollback();
            } catch (HibernateException e1) {
                logger.error("Transaction rollback not successful");
            }
            throw e;
        }
    }
}
