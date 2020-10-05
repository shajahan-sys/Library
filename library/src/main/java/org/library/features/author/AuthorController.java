package org.library.features.author;


import org.library.features.login.Login;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Controller class which is a Servlet implementation. Overrides doGet and doPost methods, uses
 * AuthorService methods and Author class as a model. Uses RequestDispatcher object to forward
 * a request from this servlet to author.jsp file. Enables user to delete author, can send a
 * redirect response to AddEditAuthorController and MenuController.
 *
 * @author Barbara Grabowska
 * @version %I%, %G%
 */
@WebServlet(urlPatterns = "/authors")
public class AuthorController extends HttpServlet {
    private AuthorService authorService;
    /**
     * Logger instance for this class
     */
    private final Logger logger = LoggerFactory.getLogger(AuthorController.class);

    /**
     * Overrides doGet method. Initializes AuthorService object, uses setProperAttributes method
     * to bind a proper object to a session object and forwards a request from a servlet to JSP file.
     *
     * @param req  HttpServletRequest object that contains the request the client has made of the servlet
     * @param resp HttpServletResponse object that contains the response the servlet sends to the client
     * @throws ServletException if an input or output error is detected when the servlet handles the GET request
     * @throws IOException      if the request for the GET could not be handled
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        initializeAuthorService();
        setProperAttributes(req);
        req.getRequestDispatcher("author.jsp").forward(req, resp);
        logger.debug("doGet");
    }

    /**
     * Overrides doPost method. Decides what action to take, based on req parameter "button" value.
     * Throws IllegalArgumentException by default.
     *
     * @param req  object that contains the request the client has made of the servlet
     * @param resp an HttpServletResponse object that contains the response the servlet sends to the client
     * @throws ServletException if an input or output error is detected when the servlet handles the request
     * @throws IOException if the request for the POST could not be handled
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        switch (req.getParameter("button")) {
            case "edit":
                editAction(req);
                resp.sendRedirect("add-edit-author");
                break;
            case "delete":
                deleteAction(req, resp);
                break;
            case "add new":
                resp.sendRedirect("add-edit-author");
                break;
            case "menu":
                resp.sendRedirect("menu");
                break;
            default:
                throw new IllegalArgumentException("Wrong button value!");
        }
        logger.debug("doPost");
    }

    /**
     * Sets session attribute named "edit", which is a proper Author object that is got using
     * authorService getAuthor method.
     */
    protected void editAction(HttpServletRequest req) {
        HttpSession session = req.getSession();
        session.setAttribute("edit", authorService.getAuthor
                (Integer.parseInt(req.getParameter("selected")), (Login) session.getAttribute("userLogin")));
    }

    /**
     * Deletes selected author object if possible, then sets up-to-date list of author objects
     * as "authorList" session attribute and forwards a request to author.jsp file.
     * If selected author can not be deleted this method calls printMessage method.
     *
     * @param req  object that contains the request the client has made of the servlet
     * @param resp an HttpServletResponse object that contains the response the servlet sends to the client
     * @throws ServletException if an input or output error is detected when the servlet handles the request
     * @throws IOException      if an I/O exception of some sort has occurred
     */
    protected void deleteAction(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        HttpSession session = req.getSession();
        Login login = (Login) session.getAttribute("userLogin");
        if (authorService.deleteIfPossible(Integer.parseInt(req.getParameter("selected")), login)) {
            session.setAttribute("authorList", authorService.getAuthorList(login));
            req.getRequestDispatcher("author.jsp").forward(req, resp);
            logger.debug("deleted author");
        } else {
            printMessage(resp);
        }
    }

    /**
     * Creates PrintWriter object and uses it to print specified Strings. These Strings should display
     * an alert box (in author.jsp file) with a message got using authorService.
     *
     * @param resp HttpServletResponse object that contains the response the servlet sends to the client
     * @throws IOException if an I/O exception of some sort has occurred
     */
    protected void printMessage(HttpServletResponse resp) throws IOException {
        PrintWriter out = resp.getWriter();
        out.println("<script type=\"text/javascript\">");
        out.println("alert('" + authorService.getMessage() + "');");
        out.println("location='author.jsp';");
        out.println("</script>");
        logger.debug("Printed message");
    }

    /**
     * Sets proper list of author objects as "authorList" session attribute.
     * If session attribute "saved" does not equals null, then calls authorService
     * deleteFromMap method and removes the "saved" attribute
     *
     * @param req HttpServletRequest object that contains the request the client has made of the servlet
     */
    protected void setProperAttributes(HttpServletRequest req) {
        HttpSession session = req.getSession();
        Login login = (Login) session.getAttribute("userLogin");
        if (session.getAttribute("saved") != null) {
            authorService.deleteFromMap(login);
            session.removeAttribute("saved");
        }
        session.setAttribute("authorList", authorService.getAuthorList(login));
        logger.debug("Got list of authors");
    }

    /**
     * Initializes readerService that has not already been initialized.
     */
    private void initializeAuthorService() {
        if (authorService == null) {
            authorService = new AuthorService();
            logger.debug("Initialized authorService");
        }
    }

    /**
     * @param authorService authorService to set
     */
    protected void setAuthorService(AuthorService authorService) {
        this.authorService = authorService;
    }

}
