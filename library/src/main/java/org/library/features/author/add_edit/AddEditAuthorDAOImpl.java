package org.library.features.author.add_edit;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.library.features.author.Author;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * AddEditAuthorDAO implementation.
 *
 * @author Barbara Grabowska
 * @version %I%, %G%
 */
public class AddEditAuthorDAOImpl implements AddEditAuthorDAO {
    private SessionFactory sessionFactory;
    /**
     * Logger instance for this class
     */
    private final Logger logger = LoggerFactory.getLogger(AddEditAuthorDAOImpl.class);

    @Override
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void save(Author author) {
        Session session = sessionFactory.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.saveOrUpdate(author);
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
