package org.library.features.author;


import org.library.features.login.Login;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
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
        } else {
            resp.sendRedirect("login.jsp");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        switch (req.getParameter("button")) {
            case "edit":
                resolveEdit(req);
                resp.sendRedirect("add-edit-author");
                break;
            case "delete":
                resolveDelete(req, resp);
                break;
            case "add new":
                resp.sendRedirect("add-edit-author");
                break;
            case "menu":
                resp.sendRedirect("menu");
                break;
            default:
                throw new IllegalArgumentException("Wrong button value!");
        }
    }

    protected void resolveEdit(HttpServletRequest req) {
        session.setAttribute("edit", authorService.getAuthor(Integer.parseInt(req.getParameter("selected"))));
    }

    protected void resolveDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        Author author = authorService.getAuthor(Integer.parseInt(req.getParameter("selected")));
        if (author.getBooks().size() == 0) {
            authorService.delete(author);
            setProperAttributesForwardRequest(req, resp);
        } else {
            PrintWriter out = resp.getWriter();
            out.println("<script type=\"text/javascript\">");
            out.println("alert('Cannot delete selected author. There are some books assigned to this author, delete these books or change the author first.');");
            out.println("location='author.jsp';");
            out.println("</script>");
        }
    }

    protected void setProperAttributesForwardRequest(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        initializeAuthorService();
        List<Author> authors = authorService.getAuthorList(login);
        session.setAttribute("authorList", authors);
        req.getRequestDispatcher("author.jsp").forward(req, resp);

    }

    private void initializeAuthorService() {
        if (authorService == null) {
            authorService = new AuthorService();
        }
    }

    protected void setAuthorService(AuthorService authorService) {
        this.authorService = authorService;
    }

}
