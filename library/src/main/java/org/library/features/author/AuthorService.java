package org.library.features.author;

import org.library.features.login.Login;
import org.library.hibernate_util.HibernateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Service class that contains business logic for author feature. Enables Controller class
 * to get demanded objects and to delete Author objects using AuthorDAO instance.
 */
public class AuthorService {
    private AuthorDAO authorDAO;
    /**
     * Represents message that contains information why Author object can not be deleted
     */
    private String message;
    /**
     * Represents a hashMap that contains Lists of author objects that are assigned to Login objects
     */
    private final Map<Login, List<Author>> authorsByLogin = new HashMap<>();
    /**
     * Logger object for this class
     */
    private final Logger logger = LoggerFactory.getLogger(AuthorService.class);

    /**
     * Initializes authorDAO if has not been already initialized, sets proper sessionFactory object in authorDAO
     */
    private void initializeAuthorDAO() {
        if (authorDAO == null) {
            authorDAO = new AuthorDAOImpl();
            logger.debug("Initialized authorDAO");
        }
        authorDAO.setSessionFactory(HibernateUtil.getSessionFactory());
    }

    /**
     * Gets proper list of Authors that is associated with given login object. Checks that
     * authorsByLogin map already contains list for specified login, if so returns it.
     * Otherwise gets proper list of Author objects using authorDAO instance, then
     * associates this list with specified login in authorsByLogin and returns the list.
     *
     * @param login object that contains login data
     * @return List of Author objects that is linked to login param
     */
    protected List<Author> getAuthorList(Login login) {
        initializeAuthorDAO();
        if (authorsByLogin.containsKey(login)) {
            return authorsByLogin.get(login);
        } else {
            List<Author> authors = authorDAO.getAuthorsList(login);
            authorsByLogin.put(login, authors);
            logger.debug("Got authorsList");
            return authors;
        }
    }

    /**
     * Gets proper Author object by passed id value. Calls getAuthorList method, then creates
     * a stream of Author objects, filters it, and tries to find an appropriate object.
     * Returns the object if found, otherwise returns null.
     *
     * @param id represents value of a wanted Author's id
     * @return Author object that its id equals an id param
     */
    protected Author getAuthor(int id, Login login) {
        logger.debug("Filtering authors");
        return getAuthorList(login).stream().filter(author -> author.getId() == id).findAny().orElse(null);
    }

    /**
     * Removes the mapping for a param login from authorsByLogin
     *
     * @param login represents Login object that List associated with it should be removed
     */
    protected void deleteFromMap(Login login) {
        authorsByLogin.remove(login);
    }

    /**
     * Checks that Author object, which is got using getAuthor method by given id value has empty
     * set of Book objects. If so, tries to delete this author, associates a new, up-to-date
     * list of authors with given login in authorsByLogin map and returns true.
     * Otherwise sets proper message using setMessage method and returns false.
     *
     * @param id represents id value of a Author object that will be deleted if possible
     * @return true if this author's books set is empty, otherwise false
     */
    protected boolean deleteIfPossible(int id, Login login) {
        Author author = getAuthor(id, login);
        if (author.getBooks().isEmpty()) {
            authorDAO.delete(author);
            authorsByLogin.put(login, authorDAO.getAuthorsList(login));
            return true;
        } else {
            setMessage("Can not delete selected author. There are some books assigned to this author," +
                    " delete these books or change the author first.");
            logger.debug("Can't delete author");
            return false;
        }
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

    /**
     * @param authorDAO object to set
     */
    public void setAuthorDAO(AuthorDAO authorDAO) {
        this.authorDAO = authorDAO;
    }
}
