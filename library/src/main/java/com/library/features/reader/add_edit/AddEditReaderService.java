package com.library.features.reader.add_edit;

import com.library.features.add_edit_utils.AddEditDAO;
import com.library.features.add_edit_utils.AddEditService;
import com.library.hibernate_util.HibernateUtil;
import com.library.features.reader.Reader;

/**
 * Service class that implements AddEditService interface
 *
 * @author Barbara Grabowska
 * @version %I%, %G%
 */
public class AddEditReaderService implements AddEditService<Reader> {
    private AddEditReaderDAO addEditReaderDAO;

    @Override
    public void save(Reader reader) {
        if (addEditReaderDAO == null) {
            addEditReaderDAO = new AddEditReaderDAOImpl();
        }
        addEditReaderDAO.setSessionFactory(HibernateUtil.getSessionFactory());
        addEditReaderDAO.save(reader);
    }

    @Override
    public void setAddEditDAO(AddEditDAO<Reader> addEditDAO) {
        addEditReaderDAO = (AddEditReaderDAO) addEditDAO;
    }
}
