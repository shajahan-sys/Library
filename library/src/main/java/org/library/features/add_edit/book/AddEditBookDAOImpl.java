package org.library.features.add_edit.book;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.library.features.add_edit.author.AddEditAuthorDAOImpl;
import org.library.features.book.Book;

public class AddEditBookDAOImpl implements AddEditBookDAO{
    private SessionFactory sessionFactory;
    private Logger logger = LogManager.getLogger(AddEditBookDAOImpl.class);

    @Override
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void save(Book book) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.getTransaction();
            transaction.begin();
            if (book != null) {
                session.saveOrUpdate(book);
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
