package org.library.features.login;

import org.hibernate.SessionFactory;
import org.library.hibernate_util.HibernateUtil;

/**
 * Login Service class, implements Verifiable, is used by Login Controller class and
 * uses Login DAO interface.
 * <p>
 * Methods can be used to find out if login to proper account or saving given account is possible.
 *
 * @author Barbara Grabowska
 * @version %I%, %G%
 */
public class LoginService implements Verifiable {
    private LoginDao loginDao;
    private String message;
    private SessionFactory sessionFactory;

    /**
     * Checks using LoginDAO method if user with given login username already exists in
     * database. If user exists then this method return boolean value of Login DAO method
     * which checks if given login password matches user password stored in database.
     * If user doesn't already exist returns false.
     *
     * @param login Login object which stores login username and password collected from user
     * @return true if user already exists in database and if given login password matches
     * password fetched form database. If user doesn't already exist method returns false.
     */
    protected boolean loginToProperAccount(Login login) {
        initializeLoginDao();
        if (loginDao.doesUserAlreadyExist(login)) {
            return loginDao.isLoginInputCorrect(login);
        }
        return false;
    }

    /**
     * Checks if user with given login username already exists in database and if user doesn't
     * exist this method saves new account with provided login username and password and returns true.
     * If user already exists returns false.
     *
     * @param login Login object stores user login username and password
     * @return true if user doesn't already exist, otherwise false
     */
    protected boolean createNewAccount(Login login) {
        initializeLoginDao();
        if (!loginDao.doesUserAlreadyExist(login)) {
            loginDao.saveNewAccount(login);
            return true;
        }
        return false;
    }

    /**
     * Initializes login DAO interface instance variable with LoginDAOImpl class.
     * Checks if SessionFactory object reference equals null to make program testable
     * with test-database. If sessionFactory equals null then this method passes
     * SessionFactory object which is connected with proper database to loginDao object.
     */
    private void initializeLoginDao() {
        loginDao = new LoginDAOImpl();
        loginDao.setVerifier(this);
        if (sessionFactory != null) {
            loginDao.setSessionFactory(sessionFactory);
        } else {
            loginDao.setSessionFactory(HibernateUtil.getSessionFactory());
        }
    }

    protected void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    protected Login getLogin() {
        return loginDao.getLogin();
    }

    protected String getMessage() {
        return message;
    }

    @Override
    public void setMessage(String message) {
        this.message = message;
    }

}
