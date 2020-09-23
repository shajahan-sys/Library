package org.library.features.author.add_edit;

import org.library.features.add_edit_utils.AddEditController;
import org.library.features.add_edit_utils.AddEditService;
import org.library.features.author.Author;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@WebServlet(urlPatterns = "/add-edit-author")
public class AddEditAuthorController extends AddEditController<Author> {
    private AddEditAuthorService addEditAuthorService;


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

    @Override
    protected Author createObjectWithProperData(HttpServletRequest req) {
        Author author = new Author();
        author.setLogin(getLogin());
        author.setSurname(req.getParameter("surname"));
        if (getToEdit() != null){
            author.setId(getToEdit().getId());
        }
        return author;
    }

    @Override
    protected void deleteNoLongerValidSessionAttribute(HttpSession session) {
        session.removeAttribute("authors");
    }

    @Override
    protected String getProperJspName() {
        return "addEditAuthor.jsp";
    }

    @Override
    protected String getProperLocationName() {
        return "authors";
    }
}
