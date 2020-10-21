package com.library.features.lend_book;

import org.hibernate.SessionFactory;
import com.library.features.book.Book;
import com.library.features.login.Login;
import com.library.features.reader.Reader;

import java.util.List;

/**
 * DAO interface for a lending feature, contains methods that are used by a
 * Service class to get a proper list of Readers, Books and to save a
 * new Lending object.
 *
 * @author Barbara Grabowska
 * @version %I%, %G%
 */
public interface LendingDAO {
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
     * Fetches list of all Book objects from a database that belong to a given login.
     *
     * @param login Login object
     * @return List of Books that are linked with provided login
     */
    List<Book> getAllBooks(Login login);

    /**
     * Inserts a given Lending object into a database
     *
     * @param lending to be saved
     */
    void save(Lending lending);


}
