package org.library.features.login;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

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
        if (isInputProper(req)) {
            sendRedirectIfInputIsCorrect(req, resp);
        } else {
            resp.getWriter().println("Provide login and password");
        }
    }

    protected void sendRedirectIfInputIsCorrect(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if (isLoginAndPasswordValid()) {
            HttpSession session = req.getSession();
            session.setAttribute("userLogin", loginService.getLogin());
            resp.sendRedirect("books");
        } else {
            resp.getWriter().println(loginService.getMessage());
        }
    }

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

    protected boolean isLoginAndPasswordValid() {
        Login login = new Login();
        login.setUserName(userName);
        login.setPassword(userPassword);
        return resolveAction(login);
    }

    protected boolean isInputProper(HttpServletRequest req) {
        userName = req.getParameter("username");
        userPassword = req.getParameter("password");
        value = req.getParameter("command");
        return !userName.equals("") && !userPassword.equals("");
    }
    protected void setLoginService(LoginService loginService){
        this.loginService = loginService;
    }
}
