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
 * a request from this servlet to author.jsp file. Enables client to delete author, can send
 * redirect response to AddEditAuthorController and MenuController.
 *
 * @author Barbara Grabowska
 * @version %I%, %G%
 */
@WebServlet(urlPatterns = "/authors")
public class AuthorController extends HttpServlet {
    /**
     * Name of HttpSession returned by HttpServletRequest object
     */
    private HttpSession session;
    /**
     * Represents user's login data, passed by HttpSession
     */
    private Login login;
    /**
     * AuthorService instance
     */
    private AuthorService authorService;
    /**
     * Id of an author that was selected in a view
     */
    private int selectedAuthorId;
    /**
     * Logger instance for this class
     */
    private final Logger logger = LoggerFactory.getLogger(AuthorController.class);

    /**
     * Overrides doGet method. Creates HttpSession object if doesn't exist and assigns it to session.
     * Assigns session attribute "userLogin" to class attribute login. Uses initializeAuthorService
     * and setProperAttributesForwardRequest methods to bind a proper object to session
     * and forwards a request from a servlet to JSP file.
     *
     * @param req  HttpServletRequest object that contains the request the client has made of the servlet
     * @param resp HttpServletResponse object that contains the response the servlet sends to the client
     * @throws ServletException if an input or output error is detected when the servlet handles the GET request
     * @throws IOException if the request for the GET could not be handled
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (session == null) {
            session = req.getSession();
        }
        login = (Login) session.getAttribute("userLogin");
        setProperAttributesForwardRequest(req, resp);
        logger.debug("doGet");
    }

    /**
     * Overrides doPost method. Decides which action should be taken, based on req parameter "button",
     * and also calls createSelectedAuthorId method.
     *
     * @param req  object that contains the request the client has made of the servlet
     * @param resp an HttpServletResponse object that contains the response the servlet sends to the client
     * @throws ServletException if an input or output error is detected when the servlet handles the request
     * @throws IOException if the request for the POST could not be handled
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        createSelectedAuthorId(req);
        switch (req.getParameter("button")) {
            case "edit":
                editAction();
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
     * Assigns proper value to selectedAuthorId variable if request parameter "selected" is not null.
     *
     * @param req object that contains the request the client has made of the servlet
     */
    protected void createSelectedAuthorId(HttpServletRequest req) {
        if (req.getParameter("selected") != null) {
            selectedAuthorId = Integer.parseInt(req.getParameter("selected"));
            logger.debug("Selected author");
        }
    }

    /**
     * Sets session attribute named "edit", which is a proper Author object that is got using
     * authorService getAuthor method.
     */
    protected void editAction() {
        session.setAttribute("edit", authorService.getAuthor(selectedAuthorId));
    }

    /**
     * Passes selectedAuthorId to authorService method - deleteIfPossible, then authorService decides if
     * object with passes id can be deleted. If deleteIfPossible returns false then this method calls
     * printMessage method. If deleteIfPossible returns true it means that authorService tried to delete
     * corresponding object, so session attribute "authorList" might have changed. Therefore this method
     * sets up-to-date list of authors and forwards a request to an author.jsp file.
     *
     * @param req  object that contains the request the client has made of the servlet
     * @param resp an HttpServletResponse object that contains the response the servlet sends to the client
     * @throws ServletException if an input or output error is detected when the servlet handles the request
     * @throws IOException if an I/O exception of some sort has occurred
     */
    protected void deleteAction(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        if (authorService.deleteIfPossible(selectedAuthorId)) {
            session.setAttribute("authorList", authorService.getAuthorList(login));
            req.getRequestDispatcher("author.jsp").forward(req, resp);
            logger.debug("deleted author");
        } else {
            printMessage(resp, authorService.getMessage());
        }
    }

    /**
     * Creates PrintWriter object and uses it to print specified Strings. These Strings should display
     * an alert box (in author.jsp file) with a message that is passed to this method.
     *
     * @param message String message to be displayed in the view
     * @param resp HttpServletResponse object that contains the response the servlet sends to the client
     * @throws IOException if an I/O exception of some sort has occurred
     */
    protected void printMessage(HttpServletResponse resp, String message) throws IOException {
        PrintWriter out = resp.getWriter();
        out.println("<script type=\"text/javascript\">");
        out.println("alert('" + message + "');");
        out.println("location='author.jsp';");
        out.println("</script>");
        logger.debug("Printed message");
    }

    /**
     * Forwards a request to author.jsp file. Checks that session attribute named "authorList" equals null,
     * if so, then gets author list using authorService instance and binds it to this session.
     *
     * @param req  HttpServletRequest object that contains the request the client has made of the servlet
     * @param resp HttpServletResponse object that contains the response the servlet sends to the client
     * @throws ServletException if an input or output error is detected when the servlet handles the GET request
     * @throws IOException if an I/O exception of some sort has occurred
     */
    protected void setProperAttributesForwardRequest(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        initializeAuthorService();
        if (session.getAttribute("authorList") == null) {
            session.setAttribute("authorList", authorService.getAuthorList(login));
        }
        req.getRequestDispatcher("author.jsp").forward(req, resp);
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

    /**
     * @param session session to set
     */
    public void setSession(HttpSession session) {
        this.session = session;
    }
}
