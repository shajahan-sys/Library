package com.library.features.author.add_edit;

import com.library.features.add_edit_utils.AddEditDAO;
import com.library.features.add_edit_utils.AddEditService;
import com.library.hibernate_util.HibernateUtil;
import com.library.features.author.Author;

/**
 * Service class that implements AddEditService interface
 *
 * @author Barbara Grabowska
 * @version %I%, %G%
 */
public class AddEditAuthorService implements AddEditService<Author> {
    private AddEditAuthorDAO addEditAuthorDAO;

    @Override
    public void save(Author author) {
        if (addEditAuthorDAO == null) {
            addEditAuthorDAO = new AddEditAuthorDAOImpl();
        }
        addEditAuthorDAO.setSessionFactory(HibernateUtil.getSessionFactory());
        addEditAuthorDAO.save(author);
    }

    @Override
    public void setAddEditDAO(AddEditDAO<Author> addEditDAO) {
        addEditAuthorDAO = (AddEditAuthorDAO) addEditDAO;
    }
}
