package org.library.features.reader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.library.features.login.Login;

import java.util.List;

public class ReaderDAOImpl implements ReaderDAO{
    private SessionFactory sessionFactory;
    private final Logger logger = LogManager.getLogger(ReaderDAOImpl.class);

    @Override
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Reader> getReadersList(Login login) {
        List readers;
        Transaction transaction = null;
        Session session = null;
        try{
        session = sessionFactory.openSession();
            transaction = session.getTransaction();
            transaction.begin();
            readers = session.createQuery("from Reader where user_id = :id order by surname")
                    .setParameter("id", login.getId())
                    .getResultList();
            transaction.commit();
        } catch (Exception e) {
            logger.error(e.getMessage());
            try {
                if (transaction != null)
                    transaction.rollback();
            } catch (HibernateException e1) {
                logger.error("Transaction rollback not successful");
            }
            throw e;
        }
        finally {
            assert session != null;
            session.close();
        }
        return readers;
    }

    @Override
    public void delete(Reader reader) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.getTransaction();
            transaction.begin();
            reader = session.get(Reader.class, reader.getId());
            if (reader != null) {
                session.delete(reader);
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
