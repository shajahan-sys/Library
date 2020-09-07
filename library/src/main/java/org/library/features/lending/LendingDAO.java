package org.library.features.lending;

import org.hibernate.SessionFactory;
import org.library.features.book.Book;
import org.library.features.login.Login;
import org.library.features.reader.Reader;

import java.util.List;

public interface LendingDAO {
    void setSessionFactory(SessionFactory sessionFactory);

    List<Reader> getReadersList(Login login);

    List<Book> getAvailableBooksList(Login login);

    void save(Lending lending);


}
