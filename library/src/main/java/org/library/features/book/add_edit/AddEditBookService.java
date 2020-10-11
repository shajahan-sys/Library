package org.library.features.book.add_edit;

import org.library.features.add_edit_utils.AddEditDAO;
import org.library.features.add_edit_utils.AddEditService;
import org.library.features.book.Book;
import org.library.hibernate_util.HibernateUtil;

/**
 * Service class that implements AddEditService interface
 *
 * @author Barbara Grabowska
 * @version %I%, %G%
 */
public class AddEditBookService implements AddEditService<Book> {
    private AddEditBookDAO addEditBookDAO;

    @Override
    public void save(Book book) {
        if (addEditBookDAO == null) {
            addEditBookDAO = new AddEditBookDAOImpl();
        }
        addEditBookDAO.setSessionFactory(HibernateUtil.getSessionFactory());
        addEditBookDAO.save(book);
    }

    @Override
    public void setAddEditDAO(AddEditDAO<Book> addEditDAO) {
        addEditBookDAO = (AddEditBookDAO) addEditDAO;
    }
}
