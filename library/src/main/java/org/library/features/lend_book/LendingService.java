package org.library.features.lend_book;

import org.library.features.book.Book;
import org.library.features.login.Login;
import org.library.features.reader.Reader;
import org.library.hibernate_util.HibernateUtil;

import java.util.List;
import java.util.stream.Collectors;

public class LendingService {
    private LendingBookDAO lendingBookDAO;
    private List<Reader> readersList;
    private List<Book> available;

    private void initializeLendingDAO() {
        if (lendingBookDAO == null) {
            lendingBookDAO = new LendingDAOImpl();
        }
        lendingBookDAO.setSessionFactory(HibernateUtil.getSessionFactory());
    }

    protected List<Book> getAvailableBooksList(Login login) {
        initializeLendingDAO();
        available = lendingBookDAO.getAvailableBooksList(login).stream()
                .filter(book -> book.getLending()==null)
                .collect(Collectors.toList());
        return available;
    }
    protected List<Reader> getReadersList(Login login){
        initializeLendingDAO();
        readersList = lendingBookDAO.getReadersList(login);
        return readersList;
    }
    protected void saveLending(Lending lending){
        initializeLendingDAO();
        lendingBookDAO.save(lending);
    }
    protected Book getBook(int id) {
        return available.stream().filter(book -> book.getId() == id).findAny().orElse(null);
    }
    protected Reader getReader(int id){
        return readersList.stream().filter(reader -> reader.getId() == id).findAny().orElse(null);
    }
}
