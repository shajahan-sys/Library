package org.library.features.author;

import org.library.features.login.Login;
import org.library.hibernate_util.HibernateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Service class that contains business logic for author feature. Enables Controller class
 * to get demanded objects and to delete Author objects using AuthorDAO instance.
 */
public class AuthorService {
    private AuthorDAO authorDAO;
    /**
     * Name of list of Author objects
     */
    private List<Author> authorList;
    /**
     * Represents message that should be set in deleteIfPossible method
     */
    private String message;
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
     * Gets proper list of Authors that is associated with given login object, using authorDAO instance.
     *
     * @param login object that contains login data
     * @return List of Author objects that is linked to login param
     */
    protected List<Author> getAuthorList(Login login) {
        initializeAuthorDAO();
        authorList = authorDAO.getAuthorsList(login);
        return authorList;
    }

    /**
     * Gets proper Author object by passed id value. Uses authorList to create a stream of Author objects,
     * filters it, and tries to find an appropriate object.
     *
     * @param id represents value of a wanted Author's id
     * @return Author object that its id equals an id parameter
     */
    protected Author getAuthor(int id) {
        return authorList.stream().filter(author -> author.getId() == id).findAny().orElse(null);
    }

    /**
     * Checks that Author object, which is got using getAuthor method by given id value has empty
     * set of Book objects. If so, tries to delete this author and returns true.
     * Otherwise sets proper message using setMessage method and returns false.
     *
     * @param id represents id value of a Author object that will be deleted if possible
     * @return true if this author's books set is empty, otherwise false
     */
    protected boolean deleteIfPossible(int id) {
        Author author = getAuthor(id);
        if (author.getBooks().isEmpty()) {
            authorDAO.delete(author);
            return true;
        } else {
            setMessage("Cannot delete selected author. There are some books assigned to this author, delete these books or change the author first.");
            return false;
        }
    }

    /**
     * @return message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * @param authorDAO object to set
     */
    public void setAuthorDAO(AuthorDAO authorDAO) {
        this.authorDAO = authorDAO;
    }
}
