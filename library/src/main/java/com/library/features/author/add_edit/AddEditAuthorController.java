package com.library.features.author.add_edit;

import com.library.features.add_edit_utils.AddEditController;
import com.library.features.add_edit_utils.AddEditService;
import com.library.features.author.Author;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;

/**
 * Controller class which is a Servlet implementation, extends AddEditController abstract class.
 *
 * @author Barbara Grabowska
 * @version %I%, %G%
 */
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
        setSessionAttribute(req);
        Author author = new Author();
        author.setLogin(getLogin(req));
        author.setSurname(req.getParameter("surname"));
        if (getToEdit(req) != null){
            author.setId(getToEdit(req).getId());
        }
        return author;
    }

    /**
     * Sets session attribute "booksMightHaveChanged".
     *
     * @param req object that contains the request the client has made of the servlet
     */
    protected void setSessionAttribute(HttpServletRequest req){
        req.getSession().setAttribute("booksMightHaveChanged", true);
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
