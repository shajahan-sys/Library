package org.library.features.return_book;

import org.hibernate.SessionFactory;
import org.library.features.lending.Lending;
import org.library.features.login.Login;
import org.library.features.reader.Reader;

import java.util.List;

public interface ReturnBookDAO {
    void setSessionFactory(SessionFactory sessionFactory);
    List<Reader> getReadersList(Login login);
    void delete(Lending lending);
}
