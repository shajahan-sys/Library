package com.library.features.reader;

import org.hibernate.*;
import com.library.features.login.Login;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
/**
 * ReaderDAO implementation.
 *
 * @author Barbara Grabowska
 * @version %I%, %G%
 */
public class ReaderDAOImpl implements ReaderDAO {
    private SessionFactory sessionFactory;
    /**
     * Logger instance for this class
     */
    private final Logger logger = LoggerFactory.getLogger(ReaderDAOImpl.class);

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
    public void delete(Reader reader) {
        Session session = sessionFactory.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.delete(reader);
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
