package org.library.features.reader;

import org.library.features.login.Login;
import org.library.hibernate_util.HibernateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ReaderService {
    private ReaderDAO readerDAO;
    private List<Reader> readersList;
    private static Logger logger = LoggerFactory.getLogger(ReaderService.class);

    private void initializeReaderDAO() {
        if (readerDAO == null) {
            readerDAO = new ReaderDAOImpl();
        }
        readerDAO.setSessionFactory(HibernateUtil.getSessionFactory());
    }

    protected List<Reader> getReadersList(Login login) {
        initializeReaderDAO();
        readersList = readerDAO.getReadersList(login);
        return readersList;
    }

    protected Reader getReader(int id) {
        return readersList.stream().filter(reader -> reader.getId() == id).findAny().orElse(null);
    }

    protected boolean deleteIfPossible(int id) {
      //  Reader readerToDelete = getReader(id);
        if (isReaderLendingSetEmpty(id)){
            readerDAO.delete(getReader(id));
            return true;
        } else {
            return false;
        }
    }
    protected boolean isReaderLendingSetEmpty(int id){
        return getReader(id).getLendings().isEmpty();
    }
}
