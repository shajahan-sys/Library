package com.library.features.add_edit_utils;


import com.library.features.login.Login;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Abstract Controller class  which is a Servlet implementation. Overrides doPost
 * and doGet methods, uses T object as a model. Enables user to save a T object.
 *
 * @param <T> Model for class
 * @author Barbara Grabowska
 * @version %I%, %G%
 */
public abstract class AddEditController<T> extends HttpServlet {
    /**
     * Saves T object
     *
     * @param object that is about to be saved
     */
    protected abstract void save(T object);

    /**
     * @param addEditService to set
     */
    protected abstract void setAddEditService(AddEditService<T> addEditService);

    /**
     * @param req HttpServletRequest object that contains the request the client has made of the servlet
     * @return T object that contains data provided by user
     */
    protected abstract T createObjectWithProperData(HttpServletRequest req);

    /**
     * @return String that represents name of proper JSP file
     */
    protected abstract String getProperJspName();

    /**
     * @return String which contains a proper location URL
     */
    protected abstract String getProperLocationName();

    /**
     * Overrides doGet method. Sets a req attribute T object named "edit" if this object
     * is not null. Uses RequestDispatcher object to forward a request from this servlet
     * to a proper JSP file, which name is got by using getProperJspName method.
     *
     * @param req  HttpServletRequest object that contains the request the client has made of the servlet
     * @param resp HttpServletResponse object that contains the response the servlet sends to the client
     * @throws ServletException if an input or output error is detected when the servlet handles the GET request
     * @throws IOException      if the request for the GET could not be handled
     */
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        T toEdit = (T) req.getSession().getAttribute("edit");
        if (toEdit != null) {
            req.setAttribute("edit", toEdit);
        }
        req.getRequestDispatcher(getProperJspName()).forward(req, resp);
    }

    /**
     * Overrides doPost method. Decides what action to take. If req parameter "button" value
     * equals "save", saves proper object and sets "saved" session attribute. If session
     * attribute "edit" is not null, removes it. This method sends a redirect response to
     * a location, which is got using getProperLocationName.
     *
     * @param req  object that contains the request the client has made of the servlet
     * @param resp an HttpServletResponse object that contains the response the servlet sends to the client
     * @throws SecurityException if a security violation occurred
     * @throws IOException       if the request for the POST could not be handled
     */
    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws SecurityException, IOException {
        HttpSession session = req.getSession();
        if (req.getParameter("button").equals("save")) {
            save(createObjectWithProperData(req));
            session.setAttribute("saved", true);
        }
        if (session.getAttribute("edit") != null) {
            session.removeAttribute("edit");
        }
        resp.sendRedirect(getProperLocationName());
    }

    /**
     * Gets T object that is assigned to a session as a "edit" attribute.
     *
     * @param req HttpServletRequest object that contains the request the client has made of the servlet
     * @return T object
     */
    protected T getToEdit(HttpServletRequest req) {
        return (T) req.getSession().getAttribute("edit");
    }

    /**
     * Gets Login object that is assigned to a session as a "userLogin" attribute.
     *
     * @param req HttpServletRequest object that contains the request the client has made of the servlet
     * @return Login object that contains login data
     */
    protected Login getLogin(HttpServletRequest req) {
        return (Login) req.getSession().getAttribute("userLogin");
    }
}
