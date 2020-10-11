package org.library.features.return_book;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.library.features.lend_book.Lending;
import org.library.features.login.Login;

import java.util.List;

public class ReturnBookDAOImpl implements ReturnBookDAO {
    private SessionFactory sessionFactory;
    private final Logger logger = LogManager.getLogger(ReturnBookDAOImpl.class);


    @Override
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List getReadersList(Login login) {
        List readers;
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.getTransaction();
            transaction.begin();
            readers = session.createQuery("from Reader where user_id = :id order by surname")
                 //   .setParameter("id", login.getId())
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
    public void delete(Lending lending) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            lending = session.get(Lending.class, lending.getId());
            if (lending != null) {
                session.delete(lending);
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
