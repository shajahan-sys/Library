package org.library.features.login;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Controller class which is a Servlet implementation
 *
 * @author Barbara Grabowska
 * @version %I%, %G%
 */
@WebServlet(urlPatterns = {"login"})
public class LoginController extends HttpServlet {
    private String action;
    private LoginService loginService;
    private Login login;


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        setProperData(req);
        if (isLoginAndPasswordValid()) {
            HttpSession session = req.getSession();
            session.setAttribute("userLogin", loginService.getLogin());
            resp.sendRedirect("menu");
        } else {
            printMessage(resp);
        }
    }

    /**
     * Creates PrintWriter object and uses it to print specified Strings. These Strings
     * should display an alert box (in login.jsp) with a message that is returned by a
     * LoginService class getMessage method.
     *
     * @param resp HttpServletResponse object is used to create PrintWriter object
     * @throws IOException
     */

    protected void printMessage(HttpServletResponse resp) throws IOException {
        PrintWriter out = resp.getWriter();
        out.println("<script type=\"text/javascript\">");
        out.println("alert('" + loginService.getMessage() + "');");
        out.println("location='login.jsp';");
        out.println("</script>");
    }

    /**
     * Checks if variable value equals 0 or 1, if not then throws IllegalArgumentException.
     * If value equals to 0 this method calls LoginService class method to check if it is possible to
     * login to proper account based on data stored in login object.
     * If value equals to 1 this method calls Login Service class method to check if it is possible to
     * create new account using data stored in given login object.
     *
     * @return true if user was successfully signed-in when value equals to 0, or new account was successfully
     * saved when value equals to 1, otherwise false
     */
    protected boolean isLoginAndPasswordValid() {
        initializeLoginService();
        switch (action) {
            case "login":
                return loginService.loginToProperAccount(login);
            case "create":
                return loginService.createNewAccount(login);
            default:
                throw new IllegalArgumentException("Wrong value! Value should equal to login or create");
        }
    }

    /**
     * Creates Login object, sets its username to request parameter "username" value
     * and password to request parameter "password" value. Assigns request parameter
     * "command" value to action variable
     */
    protected void setProperData(HttpServletRequest req) {
        if (login == null) {
            login = new Login();
        }
        login.setUserName(req.getParameter("username"));
        login.setPassword(req.getParameter("password"));
        action = req.getParameter("command");
    }

    private void initializeLoginService() {
        if (loginService == null) {
            loginService = new LoginService();
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
