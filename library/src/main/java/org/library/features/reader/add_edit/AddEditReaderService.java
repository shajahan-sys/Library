package org.library.features.reader.add_edit;

import org.library.features.add_edit_utils.AddEditDAO;
import org.library.features.add_edit_utils.AddEditService;
import org.library.features.reader.Reader;
import org.library.hibernate_util.HibernateUtil;

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
