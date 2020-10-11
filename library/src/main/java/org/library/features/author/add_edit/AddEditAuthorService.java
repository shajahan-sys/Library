package org.library.features.author.add_edit;

import org.library.features.add_edit_utils.AddEditDAO;
import org.library.features.add_edit_utils.AddEditService;
import org.library.features.author.Author;
import org.library.hibernate_util.HibernateUtil;

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
