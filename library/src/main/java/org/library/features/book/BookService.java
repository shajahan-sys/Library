package org.library.features.book;

import org.library.features.author.Author;
import org.library.features.login.Login;
import org.library.hibernate_util.HibernateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * Service class that contains business logic for book feature. Enables Controller class
 * to get demanded objects, filter books, check book's lending and delete Book objects
 * using BookDAO instance. Also sets proper message if wanted action can't be executed.
 */
public class BookService {
    private BookDAO bookDAO;
    /**
     * Represents message that contains information why Book object can't be deleted or returned
     */
    private String message;
    /**
     * Represents a hashMap that contains Lists of Book objects that are assigned to Login objects
     */
    private final Map<Login, List<Book>> booksByLogin = new HashMap<>();
    /**
     * Logger instance for this class
     */
    private final Logger logger = LoggerFactory.getLogger(BookService.class);

    /**
     * Gets proper list of Books that is associated with given login. Checks that
     * booksByLogin map already contains list for specified login, if so returns it.
     * Otherwise gets proper list of Book objects using bookDAO instance, then
     * associates this list with specified login in booksByLogin and returns the list.
     *
     * @param login object that contains login data
     * @return List of Book objects that is linked to a given login
     */
    protected List<Book> getBooksList(Login login) {
        initializeDAO();
        if (booksByLogin.containsKey(login)) {
            return booksByLogin.get(login);
        } else {
            List<Book> books = bookDAO.getBooksList(login);
            booksByLogin.put(login, books);
            logger.debug("Got list of books");
            return books;
        }
    }

    /**
     * Gets proper list of Authors that is associated with given login object
     * using bookDAO instance, and returns this list.
     *
     * @param login object that contains login data
     * @return List of Author objects that is linked to a given login
     */
    protected List<Author> getAuthorsList(Login login) {
        initializeDAO();
        logger.debug("Got list of authors");
        return bookDAO.getAuthorsList(login);
    }

    /**
     * Returns a book, whose id equals provided id value, or if that book doesn't exist returns null.
     *
     * @param login contains user's login data
     * @param id    of a book to be selected
     * @return Book object whose id equals provided id value or else null
     */
    protected Book getBook(Login login, int id) {
        logger.debug("Selecting a book");
        return getBooksList(login).stream().filter(book1 -> book1.getId() == id).findAny().orElse(null);
    }

    /**
     * Returns list of books that match requirements provided by user and are store in a given book.
     * This method filters books that belongs to given user by tile and/or author's surname.
     *
     * @param book  Book object that stores wanted book's title or/and author's surname
     * @param login contains user's login data
     * @return List of Book objects that match user's requirements
     */
    protected List<Book> filterBooks(Book book, Login login) {
        logger.debug("Filtering books");
        if (book.getAuthor() == null) {
            return getBooksList(login).stream()
                    .filter(book1 -> book1.getTitle().contains(book.getTitle()))
                    .collect(Collectors.toList());
        } else {
            return getBooksList(login).stream()
                    .filter(book1 -> book1.getAuthor().getId() == book.getAuthor().getId())
                    .filter(book1 -> book1.getTitle().contains(book.getTitle()))
                    .collect(Collectors.toList());
        }
    }

    /**
     * Deletes selected Book object if it's lending equals null, then
     * updates list of books that is stored in booksByLogin map and
     * returns true. If selected book has lending, this method sets
     * proper message and returns false.
     *
     * @param login login object that contains user's login data
     * @param id    int that represents selected book's id
     * @return true if book's lending equals null, otherwise false
     */
    protected boolean deleteIfPossible(Login login, int id) {
        Book book = getBook(login, id);
        if (book.getLending() == null) {
            bookDAO.delete(book);
            booksByLogin.put(login, bookDAO.getBooksList(login));
            logger.debug("Deleted a book");
            return true;
        } else {
            setMessage("Cannot delete selected book, because it is currently on loan. Book must be returned to be deleted.");
            return false;
        }
    }

    /**
     * Checks that book with provided id is not on loan by checking if it's lending is null.
     * If so, then this method calls setMessage method and returns true.
     * Otherwise, also sets proper message, but returns false.
     *
     * @param login Login object that represents user's login
     * @param id    int that represents selected book's id
     * @return true if selected book's lending is null, otherwise false
     */
    protected boolean isLendingNull(Login login, int id) {
        if (getBook(login, id).getLending() == null) {
            setMessage("Cannot return this book, because it is not on loan");
            return true;
        } else {
            setMessage("Cannot lend selected book, because it is already on loan.");
            return false;
        }
    }

    /**
     * Removes a List of Book objects from booksByLogin map that is associated with given login.
     *
     * @param login Login object that is a key in booksByLogin map
     */
    protected void deleteFromMap(Login login) {
        booksByLogin.remove(login);
        logger.debug("Deleted list from map");
    }

    /**
     * @param bookDAO object to set
     */
    protected void setBookDAO(BookDAO bookDAO) {
        this.bookDAO = bookDAO;
    }

    /**
     * Initializes bookDAO if has not been already initialized, sets proper sessionFactory object in bookDAO
     */
    protected void initializeDAO() {
        if (bookDAO == null) {
            bookDAO = new BookDAOImpl();
            logger.debug("Initialized bookDAO");
        }
        bookDAO.setSessionFactory(HibernateUtil.getSessionFactory());
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
