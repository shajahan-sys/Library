package org.library.features.author;


import org.library.features.login.Login;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet(urlPatterns = "authors")
public class AuthorController extends HttpServlet {
private HttpSession session;
private Login login;
private AuthorService authorService;
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (session == null) {
            session = req.getSession();
        }
        login = (Login) session.getAttribute("userLogin");
        if (login != null) {
            setProperAttributesForwardRequest(req, resp);
        }
        else {
            resp.sendRedirect("login.jsp");}
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        switch (req.getParameter("button")) {
            case "edit":
                resolveEdit(req);
                resp.sendRedirect("add-edit-author");
                break;
            case "delete":
                resolveDelete(req);
                setProperAttributesForwardRequest(req, resp);
                break;
            case "add new":
                resp.sendRedirect("add-edit-author");
            case "logout":
                resp.sendRedirect("logout");
                break;
            default:
                throw new IllegalArgumentException("Wrong button value!");
        }
    }
    protected void resolveEdit(HttpServletRequest req){
        session.setAttribute("edit", authorService.getAuthor(Integer.parseInt(req.getParameter("selected"))));
    }
    protected void resolveDelete(HttpServletRequest req){
        authorService.delete(authorService.getAuthor(Integer.parseInt(req.getParameter("selected"))));
    }

    protected void setProperAttributesForwardRequest(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        initializeAuthorService();
        List<Author> authors = authorService.getAuthorList(login);
        req.setAttribute("authorList", authors);
        req.getRequestDispatcher("author.jsp").forward(req, resp);

    }

    private void initializeAuthorService() {
        if (authorService == null) {
            authorService = new AuthorService();
        }
    }
    protected void setAuthorService(AuthorService authorService){
        this.authorService = authorService;
    }

}
