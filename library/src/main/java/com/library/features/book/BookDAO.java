package com.library.features.book;

import com.library.features.author.Author;
import com.library.features.login.Login;
import org.hibernate.SessionFactory;

import java.util.List;

/**
 * DAO interface for a book feature, contains methods that are used by a
 * Service class to get a proper list of Authors, Books and to delete
 * selected book.
 *
 * @author Barbara Grabowska
 * @version %I%, %G%
 */
public interface BookDAO {
    /**
     * Fetches list of Author objects from database that belongs to a given login.
     *
     * @param login Login object
     * @return List of Authors that are linked with provided login
     */
    List<Author> getAuthorsList(Login login);

    /**
     * Removes a Book object from a database.
     *
     * @param book that is about to be deleted
     */
    void delete(Book book);

    /**
     * @param sessionFactory to set
     */
    void setSessionFactory(SessionFactory sessionFactory);

    /**
     * Fetches list of Book objects from a database that belongs to a given login.
     *
     * @param login Login object
     * @return List of Books that are linked with provided login
     */
    List<Book> getBooksList(Login login);
}
