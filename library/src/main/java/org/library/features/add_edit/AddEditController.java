package org.library.features.add_edit;


import org.library.features.login.Login;
import org.library.features.reader.Reader;

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
    private String properJspName;

    //   protected abstract void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException;
    //  protected abstract void doPost(HttpServletRequest req, HttpServletResponse resp) throws SecurityException, IOException;
    protected abstract void save(T object);

    protected abstract void setAddEditService(AddEditService<T> addEditService);

    protected abstract T createObjectWithProperData(HttpServletRequest req);

    protected abstract String getProperJspName();

    protected abstract String getProperLocationName();

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (session == null) {
            session = req.getSession();
        }
        login = (Login) session.getAttribute("userLogin");
        toEdit = (T) session.getAttribute("edit");
        if (login != null) {
            if (toEdit != null) {
                req.setAttribute("edit", toEdit);
            }
            req.getRequestDispatcher(getProperJspName()).forward(req, resp);
        } else {
            resp.sendRedirect("login");
        }
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws SecurityException, IOException {
        if (req.getParameter("button").equals("save")) {
            save(createObjectWithProperData(req));
        }
        if (toEdit != null) {
            session.removeAttribute("edit");
        }
        resp.sendRedirect(getProperLocationName());
    }

    protected T getToEdit() {
        return toEdit;
    }

    protected Login getLogin() {
        return login;
    }
}
