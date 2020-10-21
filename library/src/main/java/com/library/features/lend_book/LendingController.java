package com.library.features.lend_book;

import com.library.features.login.Login;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Controller class which is a Servlet implementation. Overrides doGet and doPost methods, uses
 * lendingService methods, Lending class as a model. Uses RequestDispatcher object to forward
 * a request from this servlet to lending.jsp file. Enables user to save a new Lending. Can send
 * a redirect response to BookController and MenuController.
 *
 * @author Barbara Grabowska
 * @version %I%, %G%
 */
@WebServlet(urlPatterns = "/lend-book")
public class LendingController extends HttpServlet {
    private LendingService lendingService;
    /**
     * Logger instance for this class
     */
    private final Logger logger = LoggerFactory.getLogger(LendingController.class);


    /**
     * Overrides doGet method. Binds proper session attributes using setAttributes method.
     * Forwards a request from a servlet to JSP file.
     *
     * @param req  HttpServletRequest object that contains the request the client has made of the servlet
     * @param resp HttpServletResponse object that contains the response the servlet sends to the client
     * @throws ServletException if an input or output error is detected when the servlet handles the GET request
     * @throws IOException      if the request for the GET could not be handled
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        initializeLendingService();
        setAttributes(req);
        req.getRequestDispatcher("lending.jsp").forward(req, resp);
        logger.debug("doGet");
    }

    /**
     * Overrides doPost method. Decides what action to take, based on req parameter "button" value.
     *
     * @param req  object that contains the request the client has made of the servlet
     * @param resp an HttpServletResponse object that contains the response the servlet sends to the client
     * @throws IOException if the request for the POST could not be handled
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if (req.getParameter("button").equals("lend")) {
            try {
                resolveLend(req, resp);
            } catch (ParseException e) {
                e.printStackTrace();
                logger.error(e.getMessage());
            }
        } else if (req.getParameter("button").equals("cancel")) {
            removeSessionAttributes(req);
            resp.sendRedirect("menu");
            logger.debug("Cancel");
        }

    }

    /**
     * Sets session attributes "avbBooks" and "readers" using lendingService
     * methods - getAvailableBooksList and getReadersList respectively.
     *
     * @param req object that contains the request the client has made of the servlet
     */
    protected void setAttributes(HttpServletRequest req) {
        HttpSession session = req.getSession();
        Login login = (Login) session.getAttribute("userLogin");
        session.setAttribute("avbBooks", lendingService.getAvailableBooksList(login));
        if (session.getAttribute("readers") == null) {
              session.setAttribute("readers", lendingService.getReadersList(login));
        }
        logger.debug("Set session attributes");
    }

    /**
     * Initializes lendingService that has not already been initialized.
     */
    private void initializeLendingService() {
        if (lendingService == null) {
            lendingService = new LendingService();
            logger.debug("Initialized lendingService");
        }
    }

    /**
     * Returns a proper Lending object, that is created by using lendingService methods
     * with given req parameters.
     *
     * @param req object that contains the request the client has made of the servlet
     * @return Lending object that contains data provided by user
     * @throws ParseException when an error has been reached unexpectedly while parsing
     */
    protected Lending getProperLendingObject(HttpServletRequest req) throws ParseException {
        Login login = (Login) req.getSession().getAttribute("userLogin");
        Lending lending = new Lending();
        lending.setBook(lendingService.getBook(login, Integer.parseInt(req.getParameter("book"))));
        lending.setReader(lendingService.getReader(login, Integer.parseInt(req.getParameter("reader"))));
        lending.setReturnDate(new SimpleDateFormat("yyyy-MM-dd").parse(req.getParameter("date")));
        logger.debug("Created Lending object");
        return lending;
    }

    /**
     * Saves Lending object if date format is proper, sets session attributes to inform that changes
     * might have occurred calls removeSessionAttributes method and sends a redirect response
     * to BookController. If date format is wrong this method calls printMessage method.
     *
     * @param req  object that contains the request the client has made of the servlet
     * @param resp an HttpServletResponse object that contains the response the servlet sends to the client
     * @throws IOException    if the request for the POST could not be handled
     * @throws ParseException when an error has been reached unexpectedly while parsing
     */
    protected void resolveLend(HttpServletRequest req, HttpServletResponse resp) throws IOException, ParseException {
        if (lendingService.isDateFormatProper(req.getParameter("date"))) {
            lendingService.saveLending(getProperLendingObject(req));
            req.getSession().setAttribute("readersMightHaveChanged", true);
            req.getSession().setAttribute("booksMightHaveChanged", true);
            removeSessionAttributes(req);
            resp.sendRedirect("books");
            logger.debug("Lent a book");
        } else {
            printMessage(resp);
        }
    }

    /**
     * Creates PrintWriter object and uses it to print specified Strings. These Strings should display
     * an alert box (in lending.jsp file) with a message got using lendingService.
     *
     * @param resp HttpServletResponse object that contains the response the servlet sends to the client
     * @throws IOException if an I/O exception of some sort has occurred
     */
    protected void printMessage(HttpServletResponse resp) throws IOException {
        PrintWriter out = resp.getWriter();
        out.println("<script type=\"text/javascript\">");
        out.println("alert('" + lendingService.getMessage() + "');");
        out.println("location='lending.jsp';");
        out.println("</script>");
        logger.debug("Printed message");
    }

    /**
     * Removes session attributes "selReader" and "selBook" if they exist
     *
     * @param req object that contains the request the client has made of the servlet
     */
    protected void removeSessionAttributes(HttpServletRequest req) {
        HttpSession session = req.getSession();
        if (session.getAttribute("selReader") != null) {
            session.removeAttribute("selReader");
            logger.debug("Deleted 'selReader' attribute");
        }
        if (session.getAttribute("selBook") != null) {
            session.removeAttribute("selBook");
            logger.debug("Deleted 'selBook' attribute");
        }
    }

    /**
     * @param lendingService lendingService to set
     */
    protected void setLendingService(LendingService lendingService) {
        this.lendingService = lendingService;
    }

}
