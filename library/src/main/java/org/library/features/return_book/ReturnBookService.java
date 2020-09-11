package org.library.features.return_book;

import org.library.features.lend_book.Lending;
import org.library.features.login.Login;
import org.library.features.reader.Reader;
import org.library.hibernate_util.HibernateUtil;

import java.util.List;

public class ReturnBookService {
    private List<Reader> readersList;
    private ReturnBookDAO returnBookDAO;
    protected List<Reader> getActiveReadersList(Login login){
        initializeDAO();
        readersList = returnBookDAO.getReadersList(login);
        readersList.removeIf(reader -> reader.getLendings().size() == 0);
        return readersList;
    }
    protected Reader getReader(int id){
        return readersList.stream().filter(reader -> reader.getId() == id).findAny().orElse(null);
    }
    private void initializeDAO(){
        if (returnBookDAO == null)
        {
            returnBookDAO = new ReturnBookDAOImpl();
        }
        returnBookDAO.setSessionFactory(HibernateUtil.getSessionFactory());
    }
    protected void deleteLending(Lending lending){
        initializeDAO();
        returnBookDAO.delete(lending);
    }
}
