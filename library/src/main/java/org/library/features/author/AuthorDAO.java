package org.library.features.author;

import org.hibernate.SessionFactory;
import org.library.features.DAO;
import org.library.features.book.Book;
import org.library.features.login.Login;

import java.util.List;

public interface AuthorDAO {
    List<Author> getAuthorsList(Login login);
    void delete(Author author);
    void setSessionFactory(SessionFactory sessionFactory);

}
