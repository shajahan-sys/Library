package org.library.features.menu;

import org.library.features.login.Login;
import org.library.features.return_book.ReturnBookController;
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
 * Controller class which is a Servlet implementation. Overrides doGet and doPost methods,
 * uses a RequestDispatcher object to forward a request from this servlet to menu.jsp file.
 * This class can send a redirect response to BookController, ReaderController,
 * AuthorController, LendingController and ReturnBookController.
 *
 * @author Barbara Grabowska
 * @version %I%, %G%
 */
@WebServlet(urlPatterns = "/menu")
public class MenuController extends HttpServlet {
    /**
     * Logger instance for this class
     */
    private final Logger logger = LoggerFactory.getLogger(MenuController.class);

    /**
     * Overrides doGet method. Forwards a request from a servlet to a JSP file,
     * sets login object username as a session attribute "user".
     *
     * @param req HttpServletRequest object that contains the request the client has made of the servlet
     * @param resp HttpServletResponse object that contains the response the servlet sends to the client
     * @throws ServletException if an input or output error is detected when the servlet handles the GET request
     * @throws IOException if the request for the GET could not be handled
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        Login login = (Login) session.getAttribute("userLogin");
        session.setAttribute("user", login.getUserName());
        req.getRequestDispatcher("menu.jsp").forward(req, resp);
        logger.debug("doGet");
    }

    /**
     * Overrides doPost method. Decides based on a req parameter "button" value
     * to which servlet a redirect response should be sent. Throws IllegalArgumentException
     * by default.
     *
     * @param req  object that contains the request the client has made of the servlet
     * @param resp an HttpServletResponse object that contains the response the servlet sends to the client
     * @throws IOException if the request for the POST could not be handled
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        switch (req.getParameter("button")) {
            case "books":
                resp.sendRedirect("books");
                break;
            case "readers":
                resp.sendRedirect("readers");
                break;
            case "authors":
                resp.sendRedirect("authors");
                break;
            case "lend book":
                resp.sendRedirect("lend-book");
                break;
            case "return book":
                resp.sendRedirect("return-book");
                break;
            default:
                throw new IllegalArgumentException("Wrong button value!");
        }
        logger.debug("doPost");
    }
}
