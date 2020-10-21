package com.library.features.return_book;

import com.library.hibernate_util.HibernateUtil;
import com.library.features.lend_book.Lending;
import com.library.features.login.Login;
import com.library.features.reader.Reader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Service class that contains business logic for return_book feature. Enables Controller class
 * to get demanded objects and to delete Lending objects using ReturnBookDAO instance.
 *
 * @author Barbara Grabowska
 * @version %I%, %G%
 */
public class ReturnBookService {
    private ReturnBookDAO returnBookDAO;
    /**
     * Logger object for this class
     */
    private final Logger logger = LoggerFactory.getLogger(ReturnBookService.class);


    /**
     * Gets active readers List using returnBookDAO getActiveReadersList method
     *
     * @param login Login object that contains user's login data
     * @return list of readers that have at least one book borrowed currently
     */
    protected List<Reader> getActiveReadersList(Login login) {
        initializeDAO();
        logger.debug("Got active readers list");
        return returnBookDAO.getActiveReadersList(login);
    }

    /**
     * Gets proper Reader object whose id equals provided id value. Uses getActiveReadersList
     * method to create a stream of Reader objects, filters it, and tries to find an
     * appropriate object, if such a reader doesn't exist returns null.
     *
     * @param id represents value of a wanted Reader's id
     * @return Reader object that its id equals id parameter
     */
    protected Reader getReader(Login login, int id) {
        logger.debug("Selected reader");
        return getActiveReadersList(login).stream().filter(reader -> reader.getId() == id).findAny().orElse(null);
    }

    /**
     * Initializes returnBookDAO if has not been already initialized,
     * sets proper sessionFactory object in returnBookDAO
     */
    private void initializeDAO() {
        if (returnBookDAO == null) {
            returnBookDAO = new ReturnBookDAOImpl();
            logger.debug("Initialized returnBookDAO");
        }
        returnBookDAO.setSessionFactory(HibernateUtil.getSessionFactory());
    }

    /**
     * Deletes Lending object corresponding to given id, using returnBookDAO method - delete.
     *
     * @param id represents id of a Lending, which is about to be deleted
     */
    protected void deleteLending(int id) {
        returnBookDAO.delete(new Lending(id));
        logger.debug("Deleted selected lending");
    }

    /**
     * @param returnBookDAO object to set
     */
    protected void setReturnBookDAO(ReturnBookDAO returnBookDAO) {
        this.returnBookDAO = returnBookDAO;
    }
}
