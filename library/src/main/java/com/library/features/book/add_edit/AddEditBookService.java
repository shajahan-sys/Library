package com.library.features.book.add_edit;

import com.library.features.add_edit_utils.AddEditDAO;
import com.library.features.add_edit_utils.AddEditService;
import com.library.hibernate_util.HibernateUtil;
import com.library.features.book.Book;

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
