package org.library.features.book;

import org.library.features.author.Author;
import org.library.features.login.Login;
import org.library.hibernate_util.HibernateUtil;

import java.util.List;

import java.util.stream.Collectors;

public class BookService {
    private BookDAO bookDAO;
    private List<Book> allBooks;
    private Book editBook;

    protected List<Book> getBooksList(Login login) {
        initializeDAO();
        allBooks = bookDAO.getBooksList(login);
        return allBooks;
    }

    protected List<Author> getAuthorsList(Login login) {
        initializeDAO();
        return bookDAO.getAuthorsList(login);
     }

    protected Book getBook(Book book) {
        return allBooks.stream().filter(book1 -> book1.getId() == book.getId()).findAny().orElse(null);
    }

    protected List<Book> filterBooks(Book book) {
        if (book.getAuthor() == null) {
            return allBooks.stream()
                    .filter(book1 -> book1.getTitle().contains(book.getTitle()))
                    .collect(Collectors.toList());
        } else {
            return allBooks.stream()
                    .filter(book1 -> book1.getAuthor().getId() == book.getAuthor().getId())
                    .filter(book1 -> book1.getTitle().contains(book.getTitle()))
                    .collect(Collectors.toList());
        }
    }

    protected void deleteBook(Book book) {
        initializeDAO();
        bookDAO.delete(book);
    }

    protected void setBookDAO(BookDAO bookDAO) {
        this.bookDAO = bookDAO;
    }

    protected void initializeDAO() {
        if (bookDAO == null) {
            bookDAO = new BookDAOImpl();
        }
        bookDAO.setSessionFactory(HibernateUtil.getSessionFactory());
    }
}
