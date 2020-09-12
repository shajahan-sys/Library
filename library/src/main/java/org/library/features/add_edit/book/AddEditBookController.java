package org.library.features.add_edit.book;

import org.library.features.add_edit.AddEditController;
import org.library.features.add_edit.AddEditService;
import org.library.features.author.Author;
import org.library.features.book.Book;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;

@WebServlet(urlPatterns = "add-edit-book")
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
    protected String getProperJspName() {
        return "addEditBook.jsp";
    }

    @Override
    protected String getProperLocationName() {
        return "books";
    }
}
