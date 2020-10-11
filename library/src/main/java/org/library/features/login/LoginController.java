package org.library.features.login;

import org.library.features.reader.ReaderController;
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
 * LoginService methods and Login class as a model. Uses RequestDispatcher object to forward
 * a request from this servlet to login.jsp file. Enables client to login or create new account.
 * Can send a redirect response to MenuController.
 *
 * @author Barbara Grabowska
 * @version %I%, %G%
 */
@WebServlet(urlPatterns = {"/login"})
public class LoginController extends HttpServlet {
    /**
     * Represents LoginService class
     */
    private LoginService loginService;
    /**
     * Logger instance for this class
     */
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    /**
     * Overrides doGet method. Forwards a request from a servlet to a JSP file.
     *
     * @param req  HttpServletRequest object that contains the request the client has made of the servlet
     * @param resp HttpServletResponse object that contains the response the servlet sends to the client
     * @throws ServletException if an input or output error is detected when the servlet handles the GET request
     * @throws IOException if the request for the GET could not be handled
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("login.jsp").forward(req, resp);
        logger.debug("doGet");
    }
    /**
     * Overrides doPost method. Gets proper Login object, checks that data provided by user is proper and
     * chosen action can be executed, using loginService methods. If loginService method loginIfPossible
     * returns true, this method creates HttpSession object, sets "userLogin" attribute and
     * sends a redirect response to MenuController. Otherwise this method calls printMessage method.
     *
     * @param req  object that contains the request the client has made of the servlet
     * @param resp an HttpServletResponse object that contains the response the servlet sends to the client
     * @throws IOException if the request for the POST could not be handled
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        initializeLoginService();
        Login login = getLoginWithProperData(req);
        logger.debug("doPost");
        if (loginService.loginIfPossible(login, req.getParameter("command"))) {
            HttpSession session = req.getSession();
            session.setAttribute("userLogin", loginService.getLogin(login));
            resp.sendRedirect("menu");
        } else {
            printMessage(resp);
        }
    }

    /**
     * Creates PrintWriter object and uses it to print specified Strings. These Strings
     * should display an alert box (in login.jsp) with a message that is returned by a
     * LoginService getMessage method.
     *
     * @param resp HttpServletResponse object is used to create PrintWriter object
     * @throws IOException if I/O operations failed or interrupted
     */

    protected void printMessage(HttpServletResponse resp) throws IOException {
        PrintWriter out = resp.getWriter();
        out.println("<script type=\"text/javascript\">");
        out.println("alert('" + loginService.getMessage() + "');");
        out.println("location='login.jsp';");
        out.println("</script>");
        logger.debug("Printed message");
    }

    /**
     * Creates Login object, sets its username to request parameter "username" value
     * and password to request parameter "password" value.
     */
    protected Login getLoginWithProperData(HttpServletRequest req) {
        Login login = new Login();
        login.setUserName(req.getParameter("username"));
        login.setPassword(req.getParameter("password"));
        logger.debug("Created proper Login object");
        return login;
    }

    /**
     * Initializes loginService if has not already been initialized.
     */
    private void initializeLoginService() {
        if (loginService == null) {
            loginService = new LoginService();
            logger.debug("Initialized loginService");
        }
    }

    /**
     * Sets LoginService object to make this class more testable
     *
     * @param loginService Login Service object
     */
    protected void setLoginService(LoginService loginService) {
        this.loginService = loginService;
    }
}
