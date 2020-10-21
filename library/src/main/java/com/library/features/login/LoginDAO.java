package com.library.features.login;

import org.hibernate.SessionFactory;

/**
 * DAO interface for a login feature, contains methods that are
 * necessary for user to sing in or sign up.
 *
 * @author Barbara Grabowska
 * @version %I%, %G%
 */
public interface LoginDAO {
    /**
     * Inserts new account(Login object) into a database.
     *
     * @param login Login object that stores proper data
     */
    void saveNewAccount(Login login);

    /**
     * Checks if a login with given username already exists in a database.
     *
     * @param login Login object
     * @return true if any instance matches the query, otherwise false
     */
    boolean doesUserAlreadyExist(Login login);

    /**
     * Fetches adequate id (according to username) form database
     * and sets it as a passed Login object's id.
     *
     * @param login Login object
     * @return Login object that contains proper data
     */
    Login getLogin(Login login);

    /**
     * @param factory SessionFactory object to set
     */
    void setSessionFactory(SessionFactory factory);

    /**
     * Fetches a hashed password from database that is linked to
     * a given login's username and returns it.
     *
     * @param login Login object
     * @return hashedPassword that belongs to a given user, or null
     */
    String getHashedPassword(Login login);
}
