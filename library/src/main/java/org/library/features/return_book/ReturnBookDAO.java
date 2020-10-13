package org.library.features.return_book;

import org.hibernate.SessionFactory;
import org.library.features.lend_book.Lending;
import org.library.features.login.Login;
import org.library.features.reader.Reader;

import java.util.List;

/**
 * DAO interface for a return_book feature, contains methods that are used by a
 * Service class to get a proper list of active Readers and to delete lending.
 *
 * @author Barbara Grabowska
 * @version %I%, %G%
 */
public interface ReturnBookDAO {
    /**
     * @param sessionFactory to set
     */
    void setSessionFactory(SessionFactory sessionFactory);

    /**
     * Fetches list of Reader objects from database that have not empty
     * Lending Set and belong to a given login.
     *
     * @param login Login object
     * @return List of Readers who have at least 1 book on loan and are linked with provided login
     */
    List<Reader> getActiveReadersList(Login login);

    /**
     * Removes a given Lending object from a database.
     *
     * @param lending that is about to be deleted
     */
    void delete(Lending lending);
}
