package org.library.features.reader.add_edit;

import org.library.features.add_edit_utils.AddEditController;
import org.library.features.add_edit_utils.AddEditService;
import org.library.features.reader.Reader;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;

/**
 * Controller class which is a Servlet implementation, extends AddEditController abstract class.
 *
 * @author Barbara Grabowska
 * @version %I%, %G%
 */
@WebServlet(urlPatterns = "add-edit-reader")
public class AddEditReaderController extends AddEditController<Reader> {
    private AddEditReaderService addEditReaderService;

    @Override
    protected void save(Reader readerToSave) {
        if (addEditReaderService == null) {
            addEditReaderService = new AddEditReaderService();
        }
        addEditReaderService.save(readerToSave);
    }

    @Override
    protected void setAddEditService(AddEditService<Reader> addEditService) {
        addEditReaderService = (AddEditReaderService) addEditService;
    }

    @Override
    protected Reader createObjectWithProperData(HttpServletRequest req) {
        Reader reader = new Reader();
        reader.setName(req.getParameter("name"));
        reader.setLogin(getLogin(req));
        reader.setSurname(req.getParameter("surname"));
        if (getToEdit(req) != null) {
            reader.setId(getToEdit(req).getId());
        }
        return reader;
    }

    @Override
    protected String getProperJspName() {
        return "addEditReader.jsp";
    }

    @Override
    protected String getProperLocationName() {
        return "readers";
    }

}
