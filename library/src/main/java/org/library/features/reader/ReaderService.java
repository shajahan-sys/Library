package org.library.features.reader;

import org.library.features.login.Login;
import org.library.hibernate_util.HibernateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Service class that contains business logic for reader feature. Enables Controller class
 * to get demanded objects and to delete Reader objects using ReaderDAO instance.
 */
public class ReaderService {
    private ReaderDAO readerDAO;
    /**
     * Represents a hashMap that contains Lists of Reader objects that are assigned to Login objects
     */
    private final Map<Login, List<Reader>> readersByLogin = new HashMap<>();
    /**
     * Represents message that should be set in deleteIfPossible and isReaderLendingSetEmpty methods
     */
    private String message;
    /**
     * Logger object for this class
     */
    private final Logger logger = LoggerFactory.getLogger(ReaderService.class);

    /**
     * Initializes readerDAO if has not been already initialized, sets proper sessionFactory object in readerDAO
     */
    private void initializeReaderDAO() {
        if (readerDAO == null) {
            readerDAO = new ReaderDAOImpl();
            logger.debug("initialized ReaderDAO");
        }
        readerDAO.setSessionFactory(HibernateUtil.getSessionFactory());
    }

    /**
     * Gets proper list of Readers that is associated with given login. Checks that
     * readersByLogin map already contains list for specified login, if so returns it.
     * Otherwise gets proper list of Reader objects using bookDAO instance, then
     * associates this list with specified login in readersByLogin and returns the list.
     *
     * @param login object that contains login data
     * @return List of Reader objects that is linked to login parameter
     */
    protected List<Reader> getReadersList(Login login) {
        initializeReaderDAO();
        if (readersByLogin.containsKey(login)) {
            return readersByLogin.get(login);
        } else {
            List<Reader> readers = readerDAO.getReadersList(login);
            readersByLogin.put(login, readers);
            logger.debug("Got readers list from DAO");
            return readers;
        }
    }

    /**
     * Gets proper Reader object whose id equals provided id value. Uses getReadersList
     * method to create a stream of Reader objects, filters it, and tries to find an
     * appropriate object, if such a reader doesn't exist returns null.
     *
     * @param id represents value of a wanted Reader's id
     * @return Reader object that its id equals id parameter
     */
    protected Reader getReader(Login login, int id) {
        return getReadersList(login).stream().filter(reader -> reader.getId() == id).findAny().orElse(null);
    }

    /**
     * Deletes Reader object if isReaderLendingSetEmpty method returns true for a given reader
     * and returns true. Otherwise sets proper message using setMessage method and returns false.
     *
     * @param id represents id value of a Reader object that will be deleted if possible
     * @return true if isReaderLendingSetEmpty method for given id returns true, otherwise false
     */
    protected boolean deleteIfPossible(Login login, int id) {
        Reader reader = getReader(login, id);
        if (isReaderLendingSetEmpty(reader)) {
            readerDAO.delete(reader);
            readersByLogin.put(login, readerDAO.getReadersList(login));
            return true;
        } else {
            setMessage("Cannot delete selected reader. The reader has not returned borrowed books yet.");
            logger.debug("Cannot delete reader");
            return false;
        }
    }

    /**
     * Gets reader with given id and checks that reader has empty lending set
     * passing this reader to isReaderLendingSetEmpty method.
     *
     * @param id of a reader, that will be checked that has empty lending set
     * @return true if reader's lendings set is empty, otherwise false
     */
    protected boolean isReaderLendingSetEmpty(Login login, int id) {
        return isReaderLendingSetEmpty(getReader(login, id));
    }

    /**
     * Checks that given reader has empty lending set. If so, then
     * sets appropriate message using setMessage method and returns true.
     * Otherwise returns false.
     *
     * @param reader Reader object, that will be checked if has empty lending set
     * @return true if reader's lendings set is empty, otherwise false
     */
    protected boolean isReaderLendingSetEmpty(Reader reader) {
        if (reader.getLendings().isEmpty()) {
            setMessage("Selected reader does not have any books to return.");
            return true;
        } else {
            return false;
        }
    }

    /**
     * Removes a List of Reader objects from readersByLogin map that is associated with given login.
     *
     * @param login Login object that is a key in a readersByLogin map
     */
    protected void deleteFromMap(Login login) {
        readersByLogin.remove(login);
    }

    /**
     * @param message message to set
     */
    public synchronized void setMessage(String message) {
        this.message = message;
        logger.debug("Set message: " + message);
    }

    /**
     * @return message
     */
    protected synchronized String getMessage() {
        return message;
    }


    /**
     * @param readerDAO object to set
     */
    public void setReaderDAO(ReaderDAO readerDAO) {
        this.readerDAO = readerDAO;
    }
}
