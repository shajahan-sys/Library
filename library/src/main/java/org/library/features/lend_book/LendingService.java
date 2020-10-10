package org.library.features.lend_book;

import org.library.features.book.Book;
import org.library.features.login.Login;
import org.library.features.reader.Reader;
import org.library.hibernate_util.HibernateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class that contains business logic for lend_book feature. Enables Controller class
 * to get demanded objects and to save Lending objects using LendingDAO instance.
 */
public class LendingService {
    private LendingDAO lendingDAO;
    /**
     * Represents message that should be set in isDateFormatProper method
     */
    private String message;
    /**
     * Logger object for this class
     */
    private final Logger logger = LoggerFactory.getLogger(LendingService.class);


    /**
     * Initializes lendingDAO if has not been already initialized, sets proper sessionFactory object in lendingDAO
     */
    private void initializeLendingDAO() {
        if (lendingDAO == null) {
            lendingDAO = new LendingDAOImpl();
            logger.debug("Initialized lendingDAO");
        }
        lendingDAO.setSessionFactory(HibernateUtil.getSessionFactory());
    }

    /**
     * Gets proper list of Books that is associated with given login using
     * lendingDAO instance, then filters these books to find ones that are not
     * on loan and returns them as a list.
     *
     * @param login object that contains login data
     * @return List of Book objects that is linked to a given login
     */
    protected List<Book> getAvailableBooksList(Login login) {
        initializeLendingDAO();
        logger.debug("Got available Books list");
        return lendingDAO.getAvailableBooksList(login).stream()
                .filter(book -> book.getLending() == null)
                .collect(Collectors.toList());
    }

    /**
     * Gets proper list of Readers that is associated with given login
     * using lendingDAO instance, and returns it.
     *
     * @param login object that contains login data
     * @return List of Reader objects that is linked to login parameter
     */
    protected List<Reader> getReadersList(Login login) {
        logger.debug("Got readers");
        return lendingDAO.getReadersList(login);
    }

    /**
     * Saves Lending object using lendingDAO instance.
     *
     * @param lending lendingDAO instance
     */
    protected void saveLending(Lending lending) {
        lendingDAO.save(lending);
        logger.debug("Saved Lending");
    }

    /**
     * Returns a book, whose id equals provided id value, or if that book doesn't exist returns null.
     *
     * @param login contains user's login data
     * @param id    of a book to be selected
     * @return Book object whose id equals provided id value or else null
     */
    protected Book getBook(Login login, int id) {
        logger.debug("Selected book");
        return getAvailableBooksList(login).stream().filter(book -> book.getId() == id).findAny().orElse(null);
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
        logger.debug("Selected reader");
        return getReadersList(login).stream().filter(reader -> reader.getId() == id).findAny().orElse(null);
    }

    /**
     * Checks if provided date has a proper format, if so, returns true.
     * Otherwise sets proper message using setMessage method and returns false.
     *
     * @param input String representation of a date provided by user
     * @return true if date format is proper, otherwise false
     */
    protected boolean isDateFormatProper(String input) {
        if (input.matches("([0-9]{4})-([0-9]{2})-([0-9]{2})")) {
            return true;
        } else {
            setMessage("The specified date format is not valid. Use the yyyy-mm-dd format.");
            logger.debug("Wrong date format");
            return false;
        }
    }
    /**
     * @param lendingDAO object to set
     */
    protected void setLendingDAO(LendingDAO lendingDAO) {
        this.lendingDAO = lendingDAO;
    }

    /**
     * @return message
     */
    public synchronized String getMessage() {
        return message;
    }

    /**
     * @param message message to set
     */
    public synchronized void setMessage(String message) {
        this.message = message;
    }
}
