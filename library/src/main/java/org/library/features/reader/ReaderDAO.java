package org.library.features.reader;

import org.hibernate.SessionFactory;
import org.library.features.login.Login;

import java.util.List;

public interface ReaderDAO {
    void setSessionFactory(SessionFactory sessionFactory);
    List<Reader> getReadersList(Login login);
    void delete(Reader reader);

}
