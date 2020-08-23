package org.library.features.login;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.mindrot.jbcrypt.BCrypt;

import javax.persistence.NoResultException;
import javax.persistence.Query;

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
    private Login login;
    private String hashedPassword;
    private Verifiable verifiable;
    private SessionFactory sessionFactory;

    @Override
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    /**
     * Hashes String plain text password
     *
     * @param plainTextPassword String password
     * @return hashed password that can be stored in database
     */
    protected String hashPassword(String plainTextPassword) {
        return BCrypt.hashpw(plainTextPassword, BCrypt.gensalt());
    }

    /**
     * Fetches adequate id (according to username) form database
     * and sets it as passed Login object id
     *
     * @param login Login object
     */
    protected void setProperLoginId(Login login) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.getTransaction();
            transaction.begin();
            Query userId = session.createQuery("select id from Login where user_name = :username")
                    .setParameter("username", login.getUserName());
            login.setId((Integer) userId.getSingleResult());
            session.getTransaction().commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw e;
        }
    }

    /**
     * Checks if passed to this method login has a password that matches hashed
     * password (fetched from database in doesUserAlreadyExist method). If so then it
     * calls setProperLoginId method and passes login to it. When password is wrong
     * calls setMessageInVerifiable, passes String "Wrong password!" and returns false.
     *
     * @param login Login object
     * @return true if login object password matches hashed password, otherwise false
     */
    @Override
    public boolean isLoginInputCorrect(Login login) {
        if (BCrypt.checkpw(login.getPassword(), hashedPassword)) {
            setProperLoginId(login);
            return true;
        } else {
            setMessageInVerifiable("Wrong password!");
            return false;
        }
    }

    /**
     * Saves new account to database. First hashes password and sets it as login password,
     * then inserts login username and password to login table in database.
     *
     * @param login Login object stores username and password - first plain text password then hashed.
     */
    @Override
    public void saveNewAccount(Login login) {
        login.setPassword(hashPassword(login.getPassword()));
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.getTransaction();
            transaction.begin();
            session.save(login);
            session.getTransaction().commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
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
        this.login = login;
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.getTransaction();
            transaction.begin();
            Query getHashPw = session.createQuery("select password from Login where user_name = :username")
                    .setParameter("username", login.getUserName());
            hashedPassword = (String) getHashPw.getSingleResult();
            session.getTransaction().commit();
            setMessageInVerifiable("User already exist, try different login or login instead of creating new account");
            return true;
        } catch (NoResultException n) {
            setMessageInVerifiable("There is no such a user");
            return false;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw e;
        }
    }

    @Override
    public void setVerifier(Verifiable verifiable) {
        this.verifiable = verifiable;
    }

    protected void setMessageInVerifiable(String message) {
        verifiable.setMessage(message);
    }

    @Override
    public Login getLogin() {
        return login;
    }
}
