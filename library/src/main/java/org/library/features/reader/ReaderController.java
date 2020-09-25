package org.library.features.reader;

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
 * ReaderService methods and Reader class as a model. Uses RequestDispatcher object to forward
 * a request from this servlet to reader.jsp file. Enables client to delete reader, can send
 * redirect response to LendingController, AddEditReaderController and MenuController.
 *
 * @author Barbara Grabowska
 * @version %I%, %G%
 */
@WebServlet(urlPatterns = "/readers")
public class ReaderController extends HttpServlet {
    /**
     * Represents user's login data, passed by HttpSession
     */
    private Login login;
    /**
     * Name of HttpSession returned by HttpServletRequest object
     */
    private HttpSession session;
    /**
     * ReaderService instance
     */
    private ReaderService readerService;
    /**
     * Id of a reader that was selected in a view
     */
    private int selectedReaderId;
    /**
     * Logger instance for this class
     */
    private static final Logger logger = LoggerFactory.getLogger(ReaderController.class);

    /**
     * Overrides doGet method. Creates HttpSession object if doesn't exist and assigns it to session.
     * Assigns session attribute "userLogin" to class attribute login. Uses initializeReaderService
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
        initializeReaderService();
        setProperAttributesForwardRequest(req, resp);
        logger.debug("doGet");
    }

    /**
     * Overrides doPost method. Decides which action should be taken, based on req parameter "button",
     * and also calls createSelectedReaderId method.
     *
     * @param req  object that contains the request the client has made of the servlet
     * @param resp an HttpServletResponse object that contains the response the servlet sends to the client
     * @throws ServletException if an input or output error is detected when the servlet handles the request
     * @throws IOException if the request for the POST could not be handled
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        createSelectedReaderId(req);
        switch (req.getParameter("button")) {
            case "edit":
                editAction();
                resp.sendRedirect("add-edit-reader");
                break;
            case "delete":
                deleteAction(req, resp);
                break;
            case "lend":
                lendAction();
                resp.sendRedirect("lend-book");
                break;
            case "return":
                returnAction(resp);
                break;
            case "add new":
                resp.sendRedirect("add-edit-reader");
                break;
            case "menu":
                resp.sendRedirect("menu");
                break;
            default:
                logger.warn("Wrong button value!");
                throw new IllegalArgumentException("Wrong button value!");
        }
        logger.debug("doPost");
    }

    /**
     * Gets a proper Reader object using ReaderService instance. Reader is selected
     * by selectedReaderId variable.
     *
     * @return Reader object that contains proper data and is selected by id
     */
    protected Reader getSelectedReader() {
        return readerService.getReader(selectedReaderId);
    }

    /**
     * Assigns proper value to selectedReaderId variable if request parameter "selected" is not null.
     *
     * @param req object that contains the request the client has made of the servlet
     */
    protected void createSelectedReaderId(HttpServletRequest req) {
        if (req.getParameter("selected") != null) {
            selectedReaderId = Integer.parseInt(req.getParameter("selected"));
        }
    }

    /**
     * Sets session attribute - proper Reader object named "readerToEdit"
     */
    protected void editAction() {
        session.setAttribute("readerToEdit", getSelectedReader());
    }

    /**
     * Passes Reader object to readerService method - deleteIfPossible, then readerService decides if
     * object can be deleted. If deleteIfPossible returns false then this method calls printMessage method.
     * If deleteIfPossible returns true it means that readerService tried to delete object, so attribute
     * "readers" might have changed. Therefore this method sets "readers" attribute with up to date list
     * of readers and forwards a request to reader.jsp file.
     *
     * @param req  object that contains the request the client has made of the servlet
     * @param resp an HttpServletResponse object that contains the response the servlet sends to the client
     * @throws ServletException if an input or output error is detected when the servlet handles the request
     * @throws IOException if an I/O exception of some sort has occurred
     */
    protected void deleteAction(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        if (readerService.deleteIfPossible(selectedReaderId)) {
            logger.debug("deleted reader");
            session.setAttribute("readers", readerService.getReadersList(login));
            req.getRequestDispatcher("reader.jsp").forward(req, resp);
        } else {
            printMessage(readerService.getMessage(), resp);
        }
    }

    /**
     * Binds proper Reader object to this session, using name "selReader"
     */
    protected void lendAction() {
        session.setAttribute("selReader", getSelectedReader());
    }

    /**
     * Checks that reader with given id has any books to return by passing selectedReaderId
     * to readerService method - isReaderLendingSetEmpty. If reader's lending set is not empty
     * then this method binds proper lending set and reader object to this session,
     * using names "lendings" and "reader" respectively. If reader doesn't have any borrowed
     * books this method calls printMessage method.
     *
     * @param resp an HttpServletResponse object that contains the response the servlet sends to the client
     * @throws IOException if an I/O exception of some sort has occurred
     */
    protected void returnAction(HttpServletResponse resp) throws IOException {
        if (!readerService.isReaderLendingSetEmpty(selectedReaderId)) {
            session.setAttribute("lendings", getSelectedReader().getLendings());
            session.setAttribute("reader", getSelectedReader());
            resp.sendRedirect("return-book");
        } else {
            printMessage(readerService.getMessage(), resp);
        }
    }

    /**
     * Forwards a request to reader.jsp file. Checks that session attribute "readers" equals null,
     * if so, then gets readers list using readerService instance and binds it to this session.
     * Binds proper List of Readers to this session, using name "readers" if
     *
     * @param req  HttpServletRequest object that contains the request the client has made of the servlet
     * @param resp HttpServletResponse object that contains the response the servlet sends to the client
     * @throws ServletException if an input or output error is detected when the servlet handles the GET request
     * @throws IOException if an I/O exception of some sort has occurred
     */
    protected void setProperAttributesForwardRequest(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (session.getAttribute("readers") == null) {
            session.setAttribute("readers", readerService.getReadersList(login));
            logger.debug("Set session attribute readers");
        }
        req.getRequestDispatcher("reader.jsp").forward(req, resp);
    }

    /**
     * Creates PrintWriter object and uses it to print specified Strings. These Strings
     * should display an alert box (in reader.jsp file) with a message that is passed to
     * this method.
     *
     * @param message String message to be displayed in the view
     * @param resp HttpServletResponse object that contains the response the servlet sends to the client
     * @throws IOException if an I/O exception of some sort has occurred
     */
    protected void printMessage(String message, HttpServletResponse resp) throws IOException {
        PrintWriter out = resp.getWriter();
        out.println("<script type=\"text/javascript\">");
        out.println("alert('" + message + "');");
        out.println("location='reader.jsp';");
        out.println("</script>");
    }

    /**
     * Initializes readerService if has not already been initialized.
     */
    private void initializeReaderService() {
        if (readerService == null) {
            readerService = new ReaderService();
            logger.debug("Initialized readerService");
        }
    }

    /**
     * @param session session to set
     */
    protected void setSession(HttpSession session) {
        this.session = session;
    }

    /**
     * @param readerService readerService to set
     */
    protected void setReaderService(ReaderService readerService) {
        this.readerService = readerService;
    }

}
