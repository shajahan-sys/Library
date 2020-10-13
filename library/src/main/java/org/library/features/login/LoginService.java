package org.library.features.login;

import org.library.hibernate_util.HibernateUtil;
import org.mindrot.jbcrypt.BCrypt;

/**
 * Login Service class, is used by Login Controller class and uses Login DAO interface.
 * <p>
 * Methods can be used to find out that login or saving a given account is possible.
 *
 * @author Barbara Grabowska
 * @version %I%, %G%
 */
public class LoginService {
    private LoginDAO loginDao;
    /**
     * Represents text that contains information why chosen action can not be executed
     */
    private String message;

    /**
     * Checks if it is possible to execute chosen action using provided login data successfully.
     * If it is not possible then sets proper message and returns false.
     * Initializes loginDao, decides based on action param that user wants to login or to create
     * a new account. Uses loginDao to check if user already exists. Calls isLoginInputCorrect or
     * createNewAccount method. Throws IllegalArgumentException when action doesn not equal to
     * "login" or "create".
     *
     * @param login  Login object that contains login data provided by user
     * @param action represents action chosen by user
     * @return true if chosen action can be executed successfully, otherwise false
     */
    protected boolean loginIfPossible(Login login, String action) {
        initializeLoginDao();
        switch (action) {
            case "login":
                if (loginDao.doesUserAlreadyExist(login)) {
                    return isLoginInputCorrect(login);
                } else {
                    setMessage("There is no user with such a login");
                    return false;
                }
            case "create":
                if (!loginDao.doesUserAlreadyExist(login)) {
                    createNewAccount(login);
                    return true;
                } else {
                    setMessage("User already exists, try different name");
                    return false;
                }
            default:
                throw new IllegalArgumentException("Wrong action name");
        }
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
     * Checks if passed login's password matches hashed password that is got using
     * loginDao class. If so, then sets a new String as password and returns true.
     * Otherwise sets proper message and returns false.
     *
     * @param login Login object that contains proper data
     * @return true if login object password matches hashed password, otherwise false
     */

    public boolean isLoginInputCorrect(Login login) {
        if (BCrypt.checkpw(login.getPassword(), loginDao.getHashedPassword(login))) {
            login.setPassword("not storing a real pass");
            return true;
        } else {
            setMessage("Wrong password!");
            return false;
        }
    }

    /**
     * Saves a new user account using loginDao saveNewAccount method, before saving data hashes a password
     * and sets it as the login's password. After saving account sets a new String as the password
     *
     * @param login Login object stores user login username and password
     */
    protected void createNewAccount(Login login) {
        login.setPassword(hashPassword(login.getPassword()));
        loginDao.saveNewAccount(login);
        login.setPassword("not storing a real pass");
    }

    /**
     * Initializes loginDao if has not already been initialized, sets proper
     * SessionFactory object using HibernateUtil class.
     */
    private void initializeLoginDao() {
        if (loginDao == null) {
            loginDao = new LoginDAOImpl();
        }
        loginDao.setSessionFactory(HibernateUtil.getSessionFactory());
    }

    /**
     * Gets proper Login object using loginDao
     *
     * @param login object with proper username, but without id
     * @return login object with proper username and id
     */
    protected Login getLogin(Login login) {
        return loginDao.getLogin(login);
    }

    /**
     * @return message - proper String
     */
    protected synchronized String getMessage() {
        return message;
    }

    /**
     * @param message proper message to be set
     */
    public synchronized void setMessage(String message) {
        this.message = message;
    }

    /**
     * @param loginDao object to be set
     */
    public void setLoginDao(LoginDAO loginDao) {
        this.loginDao = loginDao;
    }
}
