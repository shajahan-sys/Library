package org.library.features.lend_book;

import org.hibernate.SessionFactory;
import org.library.features.book.Book;
import org.library.features.login.Login;
import org.library.features.reader.Reader;

import java.util.List;

public interface LendingBookDAO {
    void setSessionFactory(SessionFactory sessionFactory);

    List<Reader> getReadersList(Login login);

    List<Book> getAvailableBooksList(Login login);

    void save(Lending lending);


}
