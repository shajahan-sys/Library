package org.library.features.add_edit.author;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.library.features.author.Author;

public class AddEditAuthorDAOImpl implements AddEditAuthorDAO{
    private SessionFactory sessionFactory;
    private Logger logger = LogManager.getLogger(AddEditAuthorDAOImpl.class);
    @Override
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void save(Author author) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.getTransaction();
            transaction.begin();
            if (author != null) {
                session.saveOrUpdate(author);
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
