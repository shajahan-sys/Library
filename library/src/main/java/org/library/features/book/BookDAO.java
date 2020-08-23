package org.library.features.book;

import org.hibernate.SessionFactory;
import org.library.features.author.Author;
import org.library.features.login.Login;

import java.util.List;

public interface BookDAO {
    List<Author> getAuthorsList(Login login);
    void delete(Book book);
    void setSessionFactory(SessionFactory sessionFactory);
    List<Book> getBooksList(Login login);
    void save(Book book);
}
