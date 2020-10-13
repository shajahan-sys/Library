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
 * a request from this servlet to a reader.jsp file. Enables user to delete a reader, can send
 * a redirect response to LendingController, AddEditReaderController and MenuController.
 *
 * @author Barbara Grabowska
 * @version %I%, %G%
 */
@WebServlet(urlPatterns = "/readers")
public class ReaderController extends HttpServlet {
    private ReaderService readerService;
    /**
     * Logger instance for this class
     */
    private static final Logger logger = LoggerFactory.getLogger(ReaderController.class);

    /**
     * Overrides doGet method. Initializes readerService, calls setProperAttributes method,
     * that binds a proper object to session and forwards a request from a servlet to JSP file.
     *
     * @param req  HttpServletRequest object that contains the request the client has made of the servlet
     * @param resp HttpServletResponse object that contains the response the servlet sends to the client
     * @throws ServletException if an input or output error is detected when the servlet handles the GET request
     * @throws IOException      if the request for the GET could not be handled
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        initializeReaderService();
        setProperAttributes(req);
        req.getRequestDispatcher("reader.jsp").forward(req, resp);
        logger.debug("doGet");
    }

    /**
     * Overrides doPost method. Decides what action to take, based on req parameter "button" value.
     * Throws IllegalArgumentException by default.
     *
     * @param req  object that contains the request the client has made of the servlet
     * @param resp an HttpServletResponse object that contains the response the servlet sends to the client
     * @throws ServletException if an input or output error is detected when the servlet handles the request
     * @throws IOException      if the request for the POST could not be handled
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        switch (req.getParameter("button")) {
            case "edit":
                editAction(req);
                resp.sendRedirect("add-edit-reader");
                break;
            case "delete":
                deleteAction(req, resp);
                break;
            case "lend":
                lendAction(req);
                resp.sendRedirect("lend-book");
                break;
            case "return":
                returnAction(req, resp);
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
     * Sets session attribute - proper Reader object named "edit", according to req parameter "selected".
     *
     * @param req object that contains the request the client has made of the servlet
     */
    protected void editAction(HttpServletRequest req) {
        HttpSession session = req.getSession();
        session.setAttribute("edit", readerService.getReader((Login) session.getAttribute("userLogin"),
                Integer.parseInt(req.getParameter("selected"))));
        logger.debug("Set reader to edit");
    }

    /**
     * Deletes Reader object using readerService method - deleteIfPossible, that decides if
     * object with given id can be deleted. If deleteIfPossible returns true it means that
     * readerService has tried to delete a corresponding object, so a session attribute
     * "readers" might have changed. Therefore this method sets "readers" attribute with
     * up-to-date list of readers and forwards a request to reader.jsp file.
     * If deleteIfPossible returns false then this method calls printMessage method.
     *
     * @param req  object that contains the request the client has made of the servlet
     * @param resp an HttpServletResponse object that contains the response the servlet sends to the client
     * @throws ServletException if an input or output error is detected when the servlet handles the request
     * @throws IOException      if an I/O exception of some sort has occurred
     */
    protected void deleteAction(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        HttpSession session = req.getSession();
        Login login = (Login) session.getAttribute("userLogin");
        if (readerService.deleteIfPossible(login, Integer.parseInt(req.getParameter("selected")))) {
            logger.debug("deleted reader");
            session.setAttribute("readers", readerService.getReadersList(login));
            req.getRequestDispatcher("reader.jsp").forward(req, resp);
        } else {
            printMessage(resp);
        }
    }

    /**
     * Binds proper Reader object to a session, using name "selReader"
     */
    protected void lendAction(HttpServletRequest req) {
        HttpSession session = req.getSession();
        session.setAttribute("selReader", readerService.getReader((Login) session.getAttribute("userLogin"), Integer.parseInt(req.getParameter("selected"))));
        logger.debug("Set selReader");
    }

    /**
     * Checks that reader with given id has any books to return using readerService method -
     * - isReaderLendingSetEmpty. If reader's lending set is not empty then this method binds
     * a proper lending set and Reader object to a session, using names "lendings" and
     * "reader" respectively. If reader doesn't have any borrowed books this method
     * calls printMessage method.
     *
     * @param resp an HttpServletResponse object that contains the response the servlet sends to the client
     * @throws IOException if an I/O exception of some sort has occurred
     */
    protected void returnAction(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession();
        Login login = (Login) session.getAttribute("userLogin");
        int readerId = Integer.parseInt(req.getParameter("selected"));
        if (!readerService.isReaderLendingSetEmpty(login, readerId)) {
            Reader reader = readerService.getReader(login, readerId);
        //    session.setAttribute("lendings", reader.getLendings());
            session.setAttribute("reader", reader);
            resp.sendRedirect("return-book");
            logger.debug("Sent a redirect response to ReturnBookController");
        } else {
            printMessage(resp);
        }
    }

    /**
     * Sets a proper list of Reader objects as "readers" session attribute.
     * If session attribute "saved" does not equal null, then calls authorService
     * deleteFromMap method and removes the "saved" attribute.
     *
     * @param req HttpServletRequest object that contains the request the client has made of the servlet
     */
    protected void setProperAttributes(HttpServletRequest req) {
        HttpSession session = req.getSession();
        Login login = (Login) session.getAttribute("userLogin");
        deleteFromMapIfNeeded(req, "saved", "readersMightHaveChanged");
        session.setAttribute("readers", readerService.getReadersList(login));
        logger.debug("Set session attribute readers");
    }
    /**
     * Removes session attributes if exist.
     *
     * @param req object that contains the request the client has made of the servlet
     * @param attributeNames Strings that represent names of session attributes to remove
     */
    protected void deleteFromMapIfNeeded(HttpServletRequest req, String... attributeNames){
        HttpSession session = req.getSession();
        for (String name:attributeNames
        ) {
            if (session.getAttribute(name) != null) {
                readerService.deleteFromMap((Login) session.getAttribute("userLogin"));
                session.removeAttribute(name);
            }
        }
    }
    /**
     * Creates PrintWriter object and uses it to print specified Strings. These Strings
     * should display an alert box (in reader.jsp file) with a message got using
     * readerService method - getMessage.
     *
     * @param resp HttpServletResponse object that contains the response the servlet sends to the client
     * @throws IOException if an I/O exception of some sort has occurred
     */
    protected void printMessage(HttpServletResponse resp) throws IOException {
        PrintWriter out = resp.getWriter();
        out.println("<script type=\"text/javascript\">");
        out.println("alert('" + readerService.getMessage() + "');");
        out.println("location='reader.jsp';");
        out.println("</script>");
        logger.debug("Printed message");
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
     * @param readerService readerService to set
     */
    protected void setReaderService(ReaderService readerService) {
        this.readerService = readerService;
    }

}
