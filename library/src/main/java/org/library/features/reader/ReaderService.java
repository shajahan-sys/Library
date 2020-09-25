package org.library.features.reader;

import org.library.features.login.Login;
import org.library.hibernate_util.HibernateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Service class that contains business logic for reader feature. Enables Controller class
 * to get demanded objects and to delete Reader objects using ReaderDAO instance.
 */
public class ReaderService {
    private ReaderDAO readerDAO;
    /**
     * Name of list of Reader objects
     */
    private List<Reader> readersList;
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
     * Gets proper list of Readers that is associated with given login object, using readerDAO instance.
     *
     * @param login object that contains login data
     * @return List of Reader objects that is linked to login parameter
     */
    protected List<Reader> getReadersList(Login login) {
        initializeReaderDAO();
        readersList = readerDAO.getReadersList(login);
        logger.debug("Got readers list from DAO");
        return readersList;
    }

    /**
     * Gets proper Reader object by passed id value. Uses readersList to create a stream of Reader objects,
     * filters it, and tries to find an appropriate object.
     *
     * @param id represents value of a wanted Reader's id
     * @return Reader object that its id equals id parameter
     */
    protected Reader getReader(int id) {
        return readersList.stream().filter(reader -> reader.getId() == id).findAny().orElse(null);
    }

    /**
     * @return message
     */
    protected String getMessage() {
        return message;
    }

    /**
     * Deletes Reader object if isReaderLendingSetEmpty method returns true for given id value
     * and returns true. Otherwise sets proper message using setMessage method and returns false.
     *
     * @param id represents id value of a Reader object that will be deleted if possible
     * @return true if isReaderLendingSetEmpty method for given id returns true, otherwise false
     */
    protected boolean deleteIfPossible(int id) {
        if (isReaderLendingSetEmpty(id)) {
            readerDAO.delete(getReader(id));
            return true;
        } else {
            setMessage("Cannot delete selected reader. The reader has not returned borrowed books yet.");
            logger.debug("Cannot delete reader");
            return false;
        }
    }

    /**
     * Checks if reader with given id has empty lending set. If so, then
     * sets appropriate message using setMessage method and returns true.
     * Otherwise returns false.
     *
     * @param id of a reader, that will be checked that has empty lending set
     * @return true if reader's lendings set is empty, otherwise false
     */
    protected boolean isReaderLendingSetEmpty(int id) {
        if (getReader(id).getLendings().isEmpty()) {
            setMessage("Selected reader does not have any books to return.");
            return true;
        } else {
            return false;
        }
    }

    /**
     * @param message message to set
     */
    public void setMessage(String message) {
        this.message = message;
        logger.debug("Set message: " + message);
    }

    /**
     * @param readerDAO object to det
     */
    public void setReaderDAO(ReaderDAO readerDAO) {
        this.readerDAO = readerDAO;
    }
}
