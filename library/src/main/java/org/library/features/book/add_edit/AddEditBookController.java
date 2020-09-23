package org.library.features.book.add_edit;

import org.library.features.add_edit_utils.AddEditController;
import org.library.features.add_edit_utils.AddEditService;
import org.library.features.author.Author;
import org.library.features.book.Book;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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
        Book book = new Book();
        book.setTitle(req.getParameter("editTitle"));
        book.setAuthor(new Author(Integer.parseInt(req.getParameter("author1"))));
        book.setPublicationYear(req.getParameter("year"));
        book.setLogin(getLogin());
        if (getToEdit() != null) {
            book.setId(getToEdit().getId());
        }
        return book;
    }

    @Override
    protected void deleteNoLongerValidSessionAttribute(HttpSession session) {
        session.removeAttribute("books");
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
