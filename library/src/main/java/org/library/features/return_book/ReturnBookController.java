package org.library.features.return_book;

import org.library.features.login.Login;
import org.library.features.reader.Reader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Controller class which is a Servlet implementation. Overrides doGet and doPost methods, uses
 * returnBookService methods, Lending class as a model. Uses RequestDispatcher object to forward
 * a request from this servlet to returnBook.jsp file. Enables user to delete a Lending object.
 * Can send a redirect response to BookController and MenuController.
 *
 * @author Barbara Grabowska
 * @version %I%, %G%
 */
@WebServlet("/return-book")
public class ReturnBookController extends HttpServlet {
    private ReturnBookService returnBookService;
    /**
     * Logger instance for this class
     */
    private final Logger logger = LoggerFactory.getLogger(ReturnBookController.class);

    /**
     * Overrides doGet method. Binds proper request attributes using setProperRequestAttributes.
     * Forwards a request from a servlet to a JSP file.
     *
     * @param req  HttpServletRequest object that contains the request the client has made of the servlet
     * @param resp HttpServletResponse object that contains the response the servlet sends to the client
     * @throws ServletException if an input or output error is detected when the servlet handles the GET request
     * @throws IOException      if the request for the GET could not be handled
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        setProperRequestAttributes(req);
        req.getRequestDispatcher("returnBook.jsp").forward(req, resp);
        logger.debug("doGet");
    }

    /**
     * Overrides doPost method. Decides what action to take, based on req parameter
     * "button" value. If "button" value equals return, this method tries to delete
     * a selected Lending object using returnBookService, sets session attributes
     * "readersMightHaveChanged" and "booksMightHaveChanged". Sends a redirect
     * response to BookController. If button value equals "cancel" sends a redirect
     * response to MenuController.
     *
     * @param req  object that contains the request the client has made of the servlet
     * @param resp an HttpServletResponse object that contains the response the servlet sends to the client
     * @throws IOException if the request for the POST could not be handled
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if (req.getParameter("button").equals("return")) {
            returnBookService.deleteLending(Integer.parseInt(req.getParameter("selected")));
            req.getSession().setAttribute("readersMightHaveChanged", true);
            req.getSession().setAttribute("booksMightHaveChanged", true);
            resp.sendRedirect("books");
            logger.debug("Returned book");
        } else if (req.getParameter("button").equals("cancel")) {
            resp.sendRedirect("menu");
            logger.debug("Redirect to menu");
        }
        removeSessionAttributeReader(req);
    }

    /**
     * Removes session attributes "reader" if exists.
     *
     * @param req object that contains the request the client has made of the servlet
     */
    protected void removeSessionAttributeReader(HttpServletRequest req) {
        HttpSession session = req.getSession();
        if (session.getAttribute("reader") != null) {
            session.removeAttribute("reader");
            logger.debug("Removed reader attribute");
        }
    }

    /**
     * Sets proper req attributes depending on a req parameter "button" value.
     * If button is null, this method calls setActiveReaders if also session
     * attribute "reader" is not null then sets req attributes Reader object
     * and it's lendings as "selectedReader" and "lendings" respectively.
     * If button value equals "submit", this method calls resolveSubmit method.
     *
     * @param req object that contains the request the client has made of the servlet
     */
    protected void setProperRequestAttributes(HttpServletRequest req) {
        initializeReturnBookService();
        HttpSession session = req.getSession();
        Reader reader = (Reader) session.getAttribute("reader");
        String button = req.getParameter("button");
        if (button == null) {
            setActiveReaders(req);
            if (reader != null) {
                req.setAttribute("selectedReader", reader);
                req.setAttribute("lendings", reader.getLendings());
            }
        } else if (button.equals("submit")) {
            logger.debug("Submit button clicked");
            resolveSubmit(req);
        }
    }

    /**
     * Sets session attribute - proper list of active readers that is assigned to given
     * login, using returnBookService getActiveReadersList method as "activeReaders".
     *
     * @param req HttpServletRequest object that contains the request the client has made of the servlet
     */
    protected void setActiveReaders(HttpServletRequest req) {
        req.getSession().setAttribute("activeReaders", returnBookService.getActiveReadersList(
                (Login) req.getSession().getAttribute("userLogin")));
        logger.debug("Set active readers");
    }


    /**
     * Sets proper req attributes if req parameter "selReader" doesn't equal "no reader".
     * Uses returnBookService getReader method to get proper Reader object.
     *
     * @param req object that contains the request the client has made of the servlet
     */
    protected void resolveSubmit(HttpServletRequest req) {
        String readerId = req.getParameter("selReader");
        if (!readerId.equals("no reader")) {
            Reader reader = returnBookService.getReader(
                    (Login) req.getSession().getAttribute("userLogin"), Integer.parseInt(readerId));
            req.setAttribute("selectedReader", reader);
            req.setAttribute("lendings", reader.getLendings());
            logger.debug("Set req attributes 'lendings' and 'selectedReader'");
        }
    }

    /**
     * Initializes returnBookService that has not already been initialized.
     */
    protected void initializeReturnBookService() {
        if (returnBookService == null) {
            returnBookService = new ReturnBookService();
            logger.debug("Initialized returnBookService");
        }
    }

    /**
     * @param returnBookService returnBookService to set
     */
    protected void setReturnBookService(ReturnBookService returnBookService) {
        this.returnBookService = returnBookService;
    }

}
