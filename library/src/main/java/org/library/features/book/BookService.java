package org.library.features.book;

import org.library.features.login.Login;

import java.util.List;

public class BookService {

    protected List<Book> getBooksList(Login login){
        BookDAO bookDAO = new BookDAOImpl();
        return bookDAO.getAll(login);
    }
}
