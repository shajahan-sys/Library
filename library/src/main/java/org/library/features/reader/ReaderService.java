package org.library.features.reader;

import org.library.features.login.Login;
import org.library.hibernate_util.HibernateUtil;

import java.util.List;

public class ReaderService {
    private ReaderDAO readerDAO;
    private List<Reader> readersList;

    private void initializeReaderDAO() {
        if (readerDAO == null) {
            readerDAO = new ReaderDAOImpl();
        }
        readerDAO.setSessionFactory(HibernateUtil.getSessionFactory());
    }

    protected List<Reader> getReadersList(Login login){
        initializeReaderDAO();
        readersList = readerDAO.getReadersList(login);
        return readersList;
    }
    protected Reader getReader(int id){
        return readersList.stream().filter(reader -> reader.getId() == id).findAny().orElse(null);
    }
    protected void delete(Reader reader){
        readerDAO.delete(reader);
    }
}
