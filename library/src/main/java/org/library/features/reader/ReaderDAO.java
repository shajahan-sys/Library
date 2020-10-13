package org.library.features.reader;

import org.hibernate.SessionFactory;
import org.library.features.login.Login;

import java.util.List;

/**
 * DAO interface for a reader feature, contains methods that are used by a
 * Service class to get a proper list of Readers and to delete selected reader.
 *
 * @author Barbara Grabowska
 * @version %I%, %G%
 */
public interface ReaderDAO {
    /**
     * @param sessionFactory to set
     */
    void setSessionFactory(SessionFactory sessionFactory);

    /**
     * Fetches list of Reader objects from database that belongs to a given login.
     *
     * @param login Login object
     * @return List of Readers that are linked with provided login
     */
    List<Reader> getReadersList(Login login);

    /**
     * Removes a given Reader object from a database.
     *
     * @param reader that is about to be deleted
     */
    void delete(Reader reader);

}
