package org.library.features.login;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.library.hibernate_util.HibernateUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Controller class which is a Servlet implementation
 *
 * @author Barbara Grabowska
 * @version %I%, %G%
 */
@WebServlet(urlPatterns = {"login"})
public class LoginController extends HttpServlet {
    private String value;
    private String userName;
    private String userPassword;
    private LoginService loginService;


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
           setProperVariables(req);
            sendRedirectIfInputIsCorrect(req, resp);
    }

    /**
     * If isLoginAndPasswordValid method returns true this method creates HttpSession object and sets attribute
     * named userLogin to value of LoginService class method getLogin. Next sends redirect using response
     * to BookController class. If isLoginAndPasswordValid returns false this method uses PrintWriter object
     * to send String message to client.
     *
     * @param req
     * @param resp
     * @throws IOException
     */
    protected void sendRedirectIfInputIsCorrect(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if (isLoginAndPasswordValid()) {
            HttpSession session = req.getSession();
            session.setAttribute("userLogin", loginService.getLogin());
            resp.sendRedirect("welcome");
        } else {
            resp.getWriter().println(loginService.getMessage());
        }
    }

    /**
     * Checks if variable value equals to 0 or 1, if not then throws IllegalArgumentException.
     * If value equals to 0 this method calls LoginService class method to check if it is possible to
     * login to proper account based on data stored in login object.
     * If value equals to 1 this method calls Login Service class method to check if it is possible to
     * create new account using data stored in given login object.
     *
     * @param login Login object stores username and password
     * @return true if user was successfully signed-in when value equals to 0, or new account was successfully
     * saved when value equals to 1, otherwise false
     */
    protected boolean resolveAction(Login login) {
        if (loginService == null) {
            loginService = new LoginService();
        }
        if (value.equals("0")) {
            return loginService.loginToProperAccount(login);
        } else if (value.equals("1")) {
            return loginService.createNewAccount(login);
        } else {
            throw new IllegalArgumentException("Wrong value! Value should equal to 0 or 1");
        }
    }

    /**
     * First creates new Login instance, sets this login username to instance variable userName
     * and password to variable userPassword and then passes this login to resolveAction method and
     * returns its boolean value
     *
     * @return true if resolveAction method returns true, false if resolveAction returns false
     */
    protected boolean isLoginAndPasswordValid() {
        Login login = new Login();
        login.setUserName(userName);
        login.setPassword(userPassword);
        return resolveAction(login);
    }

    /*
     * Checks if login data collected from user is correct - both fields were filed in.
     * Assigns instance variables - userName, userPassword and value to parameters provided by
     * HttpServletRequest object
     *
     * @param req HttpServletRequest object contains parameters (user input) from view
     * @return true if neither userName nor userPassword equals "", otherwise false
     */
    protected void setProperVariables(HttpServletRequest req) {
        userName = req.getParameter("username");
        userPassword = req.getParameter("password");
        value = req.getParameter("command");
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
