package org.library.features.lend_book;

import org.library.features.book.Book;
import org.library.features.login.Login;
import org.library.features.reader.Reader;
import org.library.hibernate_util.HibernateUtil;

import java.util.List;
import java.util.stream.Collectors;

public class LendBookService {
    private LendBookDAO lendBookDAO;
    private List<Reader> readersList;
    private List<Book> available;

    private void initializeLendingDAO() {
        if (lendBookDAO == null) {
            lendBookDAO = new LendBookDAOImpl();
        }
        lendBookDAO.setSessionFactory(HibernateUtil.getSessionFactory());
    }

    protected List<Book> getAvailableBooksList(Login login) {
        initializeLendingDAO();
        available = lendBookDAO.getAvailableBooksList(login).stream()
                .filter(book -> book.getLendBook()==null)
                .collect(Collectors.toList());
        return available;
    }
    protected List<Reader> getReadersList(Login login){
        initializeLendingDAO();
        readersList = lendBookDAO.getReadersList(login);
        return readersList;
    }
    protected void saveLending(LendBook lendBook){
        initializeLendingDAO();
        lendBookDAO.save(lendBook);
    }
    protected Book getBook(int id) {
        return available.stream().filter(book -> book.getId() == id).findAny().orElse(null);
    }
    protected Reader getReader(int id){
        return readersList.stream().filter(reader -> reader.getId() == id).findAny().orElse(null);
    }
}
