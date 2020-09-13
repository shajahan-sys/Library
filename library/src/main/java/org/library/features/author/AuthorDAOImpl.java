package org.library.features.author;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.library.features.book.BookDAOImpl;
import org.library.features.login.Login;

import java.util.List;

public class AuthorDAOImpl implements AuthorDAO{
    private SessionFactory sessionFactory;
    private Logger logger = LogManager.getLogger(AuthorDAOImpl.class);
    @Override
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Author> getAuthorsList(Login login) {
        List authors;
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.getTransaction();
            transaction.begin();
            authors = session.createQuery("from Author where user_id = :id order by surname")
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
        return authors;
    }

    @Override
    public void delete(Author author) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.getTransaction();
            transaction.begin();
            author = session.get(Author.class, author.getId());
            if (author != null) {
                session.delete(author);
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
    }}
