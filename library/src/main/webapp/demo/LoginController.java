package org.library.features.login;

import org.library.features.login.LoginDAOImpl;
import org.library.features.login.LoginDao;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(urlPatterns = {"login"})
public class LoginController extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = req.getParameter("username");
        String password = req.getParameter("pas");
        LoginDao loginDao = new LoginDAOImpl();
        if (loginDao.check(name, password)){
            HttpSession session = req.getSession();
            session.setAttribute("uname", name);
            resp.sendRedirect("secure.jsp");
        }
        else {
            resp.sendRedirect("login.jsp");
        }
    }
}
