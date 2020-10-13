package org.library.features.login;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * LoginDAO implementation.
 *
 * @author Barbara Grabowska
 * @version %I%, %G%
 */
public class LoginDAOImpl implements LoginDAO {

    private SessionFactory sessionFactory;
    /**
     * Logger instance for this class
     */
    private final Logger logger = LoggerFactory.getLogger(LoginDAOImpl.class);

    @Override
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void saveNewAccount(Login login) {
        Session session = sessionFactory.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.save(login);
            tx.commit();
        } catch (HibernateException e) {
            logger.error(e.getMessage());
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    @Override
    public boolean doesUserAlreadyExist(Login login) {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            Query<Login> query = session.createQuery("select 1 from Login where user_name = :username");
            query.setParameter("username", login.getUserName());
            return query.uniqueResult() != null;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            return false;
        } finally {
            assert session != null;
            session.close();
        }
    }


    @Override
    public String getHashedPassword(Login login) {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            Query<String> getHashPw = session.createQuery("select password from Login where user_name = :username")
                    .setParameter("username", login.getUserName());
            return getHashPw.uniqueResult();
        } catch (Exception e) {
            logger.error(e.getMessage());
            return null;
        } finally {
            assert session != null;
            session.close();
        }
    }

    @Override
    public Login getLogin(Login login) {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            Query<Integer> id = session.createQuery("select id from Login where user_name = :username")
                    .setParameter("username", login.getUserName());
            if (id.uniqueResult() != null) {
                login.setId(id.uniqueResult());
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        } finally {
            assert session != null;
            session.close();
        }
        return login;
    }
}