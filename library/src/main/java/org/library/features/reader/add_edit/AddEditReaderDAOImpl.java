package org.library.features.reader.add_edit;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.library.features.reader.Reader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * AddEditReaderDAO implementation.
 *
 * @author Barbara Grabowska
 * @version %I%, %G%
 */
public class AddEditReaderDAOImpl implements AddEditReaderDAO {
    private SessionFactory sessionFactory;
    /**
     * Logger instance for this class
     */
    private static final Logger logger = LoggerFactory.getLogger(AddEditReaderDAOImpl.class);

    @Override
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void save(Reader reader) {
        Session session = sessionFactory.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.saveOrUpdate(reader);
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
