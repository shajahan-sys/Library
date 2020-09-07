package org.library.features.lending;

import org.library.features.book.Book;
import org.library.features.login.Login;
import org.library.features.reader.Reader;
import org.library.hibernate_util.HibernateUtil;

import java.util.List;
import java.util.stream.Collectors;

public class LendingService {
    private LendingDAO lendingDAO;
    private List<Reader> readersList;
    private List<Book> available;

    private void initializeLendingDAO() {
        if (lendingDAO == null) {
            lendingDAO = new LendingDAOImpl();
        }
        lendingDAO.setSessionFactory(HibernateUtil.getSessionFactory());
    }

    protected List<Book> getAvailableBooksList(Login login) {
        initializeLendingDAO();
        available = lendingDAO.getAvailableBooksList(login).stream()
                .filter(book -> book.getLending()==null)
                .collect(Collectors.toList());
        return available;
    }
    protected List<Reader> getReadersList(Login login){
        initializeLendingDAO();
        readersList = lendingDAO.getReadersList(login);
        return readersList;
    }
    protected void saveLending(Lending lending){
        initializeLendingDAO();
        lendingDAO.save(lending);
    }
    protected Book getBook(int id) {
        return available.stream().filter(book -> book.getId() == id).findAny().orElse(null);
    }
    protected Reader getReader(int id){
        return readersList.stream().filter(reader -> reader.getId() == id).findAny().orElse(null);
    }
}
