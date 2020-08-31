package org.library.features.add_edit.reader;

import org.library.features.add_edit.AddEditDAO;
import org.library.features.add_edit.AddEditService;
import org.library.features.reader.Reader;
import org.library.hibernate_util.HibernateUtil;

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
