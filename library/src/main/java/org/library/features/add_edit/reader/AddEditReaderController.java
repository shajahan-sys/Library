package org.library.features.add_edit.reader;

import org.library.features.add_edit.AddEditController;
import org.library.features.add_edit.AddEditService;
import org.library.features.reader.Reader;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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
        reader.setLogin(getLogin());
        reader.setSurname(req.getParameter("surname"));
        if (getToEdit() != null) {
            reader.setId(getToEdit().getId());
        }
        return reader;
    }

    @Override
    protected void deleteNoLongerValidSessionAttribute(HttpSession session) {
        session.removeAttribute("readers");
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
