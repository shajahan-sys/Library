package org.library.features.add_edit.book;

import org.library.features.add_edit.AddEditController;
import org.library.features.add_edit.AddEditService;
import org.library.features.author.Author;
import org.library.features.book.Book;
import org.library.features.login.Login;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = "add-edit-book")
public class AddEditBookController extends AddEditController<Book> {
    private Login login;
    private Book editedBook;
    private AddEditBookService addEditBookService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        login = (Login) req.getSession().getAttribute("userLogin");
        editedBook = (Book) req.getSession().getAttribute("edit");
        if (login != null) {
            setProperAttributes(req);
            req.getRequestDispatcher("addEditBook.jsp").forward(req, resp);
        } else {
            resp.sendRedirect("login");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if (req.getParameter("button").equals("save")) {
            save(createBookWithProperData(req));
        }
        if (editedBook != null) {
            req.getSession().removeAttribute("edit");
        }
        resp.sendRedirect("books");
    }

    protected void setProperAttributes(HttpServletRequest req) {
        if (editedBook != null) {
            req.setAttribute("edit", editedBook);
        }
        req.setAttribute("authors", req.getSession().getAttribute("authors"));
    }
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

    protected Book createBookWithProperData(HttpServletRequest req) {
        Book book = new Book();
        book.setTitle(req.getParameter("editTitle"));
        book.setAuthor(new Author(Integer.parseInt(req.getParameter("author1"))));
        book.setPublicationYear(req.getParameter("year"));
        book.setLogin(login);
        if (editedBook != null) {
            book.setId(editedBook.getId());
        }
        return book;
    }
}
