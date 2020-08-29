package org.library.features.welcome;

import org.library.features.login.Login;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = "welcome")
public class WelcomeController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Login login = (Login) req.getSession().getAttribute("userLogin");
        if (login != null) {
            req.getRequestDispatcher("welcome.jsp").forward(req, resp);
        } else {
            resp.sendRedirect("login");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        checkWhichButtonWasClicked(req, resp);
    }

    protected void checkWhichButtonWasClicked(HttpServletRequest req, HttpServletResponse resp) throws IOException {
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
                resp.sendRedirect("/lending");
                break;
            case "return book":
                resp.sendRedirect("/return-book");
                break;
            default:
                throw new IllegalArgumentException("Wrong button value!");
        }
    }
}
