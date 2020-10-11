package org.library.features.book.add_edit;

import org.library.features.add_edit_utils.AddEditController;
import org.library.features.add_edit_utils.AddEditService;
import org.library.features.author.Author;
import org.library.features.book.Book;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;

/**
 * Controller class which is a Servlet implementation, extends AddEditController abstract class.
 *
 * @author Barbara Grabowska
 * @version %I%, %G%
 */
@WebServlet(urlPatterns = "/add-edit-book")
public class AddEditBookController extends AddEditController<Book> {
    private AddEditBookService addEditBookService;

    @Override
    protected void save(Book bookToSave) {
        if (addEditBookService == null) {
            addEditBookService = new AddEditBookService();
        }
        addEditBookService.save(bookToSave);
    }

    @Override
    protected void setAddEditService(AddEditService<Book> addEditService) {
        addEditBookService = (AddEditBookService) addEditService;
    }

    @Override
    protected Book createObjectWithProperData(HttpServletRequest req) {
        setSessionAttribute(req);
        Book book = new Book();
        book.setTitle(req.getParameter("title"));
        book.setAuthor(new Author(Integer.parseInt(req.getParameter("author1"))));
        book.setPublicationYear(req.getParameter("year"));
        book.setLogin(getLogin(req));
        if (getToEdit(req) != null) {
            book.setId(getToEdit(req).getId());
        }
        return book;
    }
    /**
     * Sets session attribute "authorsMightHaveChanged".
     *
     * @param req object that contains the request the client has made of the servlet
     */
    protected void setSessionAttribute(HttpServletRequest req) {
        req.getSession().setAttribute("authorsMightHaveChanged", true);
    }

    @Override
    protected String getProperJspName() {
        return "addEditBook.jsp";
    }

    @Override
    protected String getProperLocationName() {
        return "books";
    }

}
