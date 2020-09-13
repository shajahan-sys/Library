package org.library.features.add_edit.reader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.library.features.book.BookDAOImpl;
import org.library.features.reader.Reader;

public class AddEditReaderDAOImpl implements AddEditReaderDAO {
    private SessionFactory sessionFactory;
    private final Logger logger = LogManager.getLogger(AddEditReaderDAOImpl.class);

    @Override
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void save(Reader reader) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.getTransaction();
            transaction.begin();
            if (reader != null) {
                session.saveOrUpdate(reader);
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
