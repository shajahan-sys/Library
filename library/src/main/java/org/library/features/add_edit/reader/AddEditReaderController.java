package org.library.features.add_edit.reader;

import org.library.features.add_edit.AddEditController;
import org.library.features.add_edit.AddEditService;
import org.library.features.add_edit.book.AddEditBookService;
import org.library.features.login.Login;
import org.library.features.reader.Reader;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(urlPatterns = "add-edit-reader")
public class AddEditReaderController extends AddEditController<Reader> {
    private AddEditReaderService addEditReaderService;
    private Login login;
    private Reader readerToEdit;
    private HttpSession session;
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (session == null){
            session =req.getSession();
        }
        login = (Login) session.getAttribute("userLogin");
        readerToEdit = (Reader) session.getAttribute("edit");
        if (login != null) {
            if (readerToEdit != null){
                req.setAttribute("edit", readerToEdit);
            }
            req.getRequestDispatcher("addEditReader.jsp").forward(req, resp);
        } else {
            resp.sendRedirect("login");
        }
    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws SecurityException, IOException {
        if (req.getParameter("button").equals("save")) {
            save(createReaderWithProperData(req));
        }
        if (readerToEdit != null) {
            req.getSession().removeAttribute("edit");
        }
        resp.sendRedirect("readers");

    }
    protected Reader createReaderWithProperData(HttpServletRequest req){
        Reader reader = new Reader();
        reader.setName(req.getParameter("name"));
        reader.setLogin(login);
        reader.setSurname(req.getParameter("surname"));
        if (readerToEdit != null){
            reader.setId(readerToEdit.getId());
        }
        return reader;
    }
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
}
