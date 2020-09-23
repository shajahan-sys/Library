package org.library.features.add_edit_utils;


import org.library.features.login.Login;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public abstract class AddEditController<T> extends HttpServlet {
    private HttpSession session;
    private Login login;
    private T toEdit;

    protected abstract void save(T object);

    protected abstract void setAddEditService(AddEditService<T> addEditService);

    protected abstract T createObjectWithProperData(HttpServletRequest req);

    protected abstract void deleteNoLongerValidSessionAttribute(HttpSession session);

    protected abstract String getProperJspName();

    protected abstract String getProperLocationName();
    protected void decide(){

    }
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (session == null) {
            session = req.getSession();
        }
        login = (Login) session.getAttribute("userLogin");
         toEdit = (T) session.getAttribute("edit");
            if (toEdit != null) {
               req.setAttribute("edit", toEdit);
            }
            req.getRequestDispatcher(getProperJspName()).forward(req, resp);
    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws SecurityException, IOException {
        if (req.getParameter("button").equals("save")) {
            save(createObjectWithProperData(req));
        }
        if (toEdit != null) {
            session.removeAttribute("edit");
        }
        deleteNoLongerValidSessionAttribute(session);
        resp.sendRedirect(getProperLocationName());
    }

    protected T getToEdit() {
        return toEdit;
    }

    protected Login getLogin() {
        return login;
    }
}
