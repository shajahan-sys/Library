package org.library.features.menu;

import org.library.features.login.Login;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = "/menu")
public class MenuController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("menu.jsp").forward(req, resp);
    }

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
    }
}
