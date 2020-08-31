package org.library.features.add_edit.book;

import org.library.features.add_edit.AddEditDAO;
import org.library.features.add_edit.AddEditService;
import org.library.features.book.Book;
import org.library.hibernate_util.HibernateUtil;

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
