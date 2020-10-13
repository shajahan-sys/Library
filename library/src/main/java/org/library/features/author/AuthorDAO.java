package org.library.features.author;

import org.hibernate.SessionFactory;
import org.library.features.login.Login;

import java.util.List;

/**
 * DAO interface for an author feature, contains methods that are used by a
 * Service class to get a proper list of Authors and to delete selected author.
 *
 * @author Barbara Grabowska
 * @version %I%, %G%
 */
public interface AuthorDAO {
    /**
     * Fetches list of Author objects from database that belongs to a given login.
     *
     * @param login Login object
     * @return List of Authors that are linked with provided login
     */
    List<Author> getAuthorsList(Login login);

    /**
     * Removes an Author object from a database.
     *
     * @param author that is about to be deleted
     */
    void delete(Author author);

    /**
     * @param sessionFactory to set
     */
    void setSessionFactory(SessionFactory sessionFactory);

}
