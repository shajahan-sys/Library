package org.library.features.add_edit.author;

import org.library.features.add_edit.AddEditController;
import org.library.features.add_edit.AddEditService;
import org.library.features.add_edit.reader.AddEditReaderService;
import org.library.features.author.Author;
import org.library.features.login.Login;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(urlPatterns = "add-edit-author")
public class AddEditAuthorController extends AddEditController<Author> {
    private Login login;
    private HttpSession session;
    private Author authorToEdit;
    private AddEditAuthorService addEditAuthorService;
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (session == null){
            session =req.getSession();
        }
        login = (Login) session.getAttribute("userLogin");
        authorToEdit = (Author) session.getAttribute("edit");
        if (login != null) {
            if (authorToEdit != null){
                req.setAttribute("edit", authorToEdit);
            }
            req.getRequestDispatcher("addEditAuthor.jsp").forward(req, resp);
        } else {
            resp.sendRedirect("login");
        }
    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws SecurityException, IOException {
        if (req.getParameter("button").equals("save")) {
            save(createReaderWithProperData(req));
        }
        if (authorToEdit != null) {
            req.getSession().removeAttribute("edit");
        }
        resp.sendRedirect("authors");

    }
    protected Author createReaderWithProperData(HttpServletRequest req){
        Author author = new Author();
        author.setLogin(login);
        author.setSurname(req.getParameter("surname"));
        if (authorToEdit != null){
            author.setId(authorToEdit.getId());
        }
        return author;
    }
    @Override
    protected void save(Author authorToSave) {
        if (addEditAuthorService == null) {
            addEditAuthorService = new AddEditAuthorService();
        }
        addEditAuthorService.save(authorToSave);
    }

    @Override
    protected void setAddEditService(AddEditService<Author> addEditService) {
        addEditAuthorService = (AddEditAuthorService) addEditService;
    }
}
