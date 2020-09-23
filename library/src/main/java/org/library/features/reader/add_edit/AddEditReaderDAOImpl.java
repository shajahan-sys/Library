package org.library.features.reader.add_edit;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.library.features.reader.Reader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AddEditReaderDAOImpl implements AddEditReaderDAO {
    private SessionFactory sessionFactory;
    private static final Logger logger = LoggerFactory.getLogger(AddEditReaderDAOImpl.class);

    @Override
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void save(Reader reader) {
        Transaction transaction = null;
        Session session = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.getTransaction();
            transaction.begin();
            if (reader != null) {
                session.saveOrUpdate(reader);
            }
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
    }
}
