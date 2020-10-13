package org.library.features.author;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.library.features.login.Login;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * AuthorDAO implementation.
 *
 * @author Barbara Grabowska
 * @version %I%, %G%
 */
public class AuthorDAOImpl implements AuthorDAO {
    private SessionFactory sessionFactory;
    /**
     * Logger instance for this class
     */
    private final Logger logger = LoggerFactory.getLogger(AuthorDAOImpl.class);

    @Override
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Author> getAuthorsList(Login login) {
        Session session = sessionFactory.openSession();
        List<Author> authors = session.createQuery("from Author where user_id = :id order by surname")
                .setParameter("id", login.getId())
                .list();
        session.close();
        return authors;
    }

    @Override
    public void delete(Author author) {
        Session session = sessionFactory.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.delete(author);
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
