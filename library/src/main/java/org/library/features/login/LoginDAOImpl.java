package org.library.features.login;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.library.hibernate_util.HibernateUtil;
import org.mindrot.jbcrypt.BCrypt;

import org.hibernate.query.Query;

/**
 * LoginDAO implementation. Methods can be used to hash password and check if
 * input Login data is correct and decide if user is allowed to login or create new account
 * based on data selected form database.
 * <p>
 * When user wants to create new account it checks if Login username is not already in database.
 * If username is unique it saves new account, otherwise it lets know LoginService class
 * that saving this account is not possible.
 * When user wants to login it checks if this Login username already exists and if so
 * it checks if hashed password
 *
 * @author Barbara Grabowska
 * @version %I%, %G%
 */
public class LoginDAOImpl implements LoginDao {

    private SessionFactory sessionFactory;
    private final Logger logger = LogManager.getLogger(LoginDAOImpl.class);

    @Override
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }


    /**
     * Fetches adequate id (according to username) form database
     * and sets it as passed Login object id
     *
     * @param login Login object
     */


    /**
     * Saves new account to database. First hashes password and sets it as login password,
     * then inserts login username and password to login table in database.
     *
     * @param login Login object stores username and password - first plain text password then hashed.
     */
    @Override
    public void saveNewAccount(Login login) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.getTransaction();
            transaction.begin();
            session.save(login);
            session.getTransaction().commit();
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

    /**
     * Checks if user with given login username already exists in database, by
     * selecting hashed password from login table where username is passed login
     * username. When user already exists this method assigns selected password
     * to instance variable hashesPassword, call setMessageInVerifiable method,
     * passes appropriate String message and returns true. If no user was found
     * this method calls setMessageInVerifiable method, passes appropriate
     * String message and returns false.
     *
     * @param login Login object stores username and is used to select password
     *              form database where username in login table equals given login username
     * @return true if user with given username already exists in database, otherwise false
     */
    @Override
    public boolean doesUserAlreadyExist(Login login) {
        // this.login = login;
        try {
            Session session = sessionFactory.openSession();
            org.hibernate.query.Query getHashPw = session.createQuery("select 1 from Login where user_name = :username");
            getHashPw.setParameter("username", login.getUserName());
            return getHashPw.uniqueResult() != null;
            // hashedPassword = (String) getHashPw.getSingleResult();
            //   return (getHashPw.() != null);
            //   session.getTransaction().commit();
            //     setMessageInVerifiable("User already exist, try different login or login instead of creating new account");
            //  return true;
        } catch (HibernateException e) {
            e.printStackTrace();
            logger.error("Couldn't ");
            return false;
        }
    }

    @Override
    public String getHashedPassword(Login login) {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            Query getHashPw = session.createQuery("select password from Login where user_name = :username")
                    .setParameter("username", login.getUserName());
              return (String) getHashPw.uniqueResult();
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
              Query id = session.createQuery("select id from Login where user_name = :username")
                    .setParameter("username", login.getUserName());
            //  setMessageInVerifiable("User already exist, try different login or login instead of creating new account");
       //     login.setId((Integer) id.uniqueResult());
            if (id.uniqueResult() != null){
                login.setId((Integer) id.uniqueResult());
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        finally {
            assert session != null;
            session.close();
        }
        return login;
    }

}