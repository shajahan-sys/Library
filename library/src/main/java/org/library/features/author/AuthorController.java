package org.library.features.author;


import org.library.features.login.Login;
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

@WebServlet(urlPatterns = "/authors")
public class AuthorController extends HttpServlet {
    private HttpSession session;
    private Login login;
    private AuthorService authorService;
    private int selectedAuthorId;
    private final Logger logger = LoggerFactory.getLogger(AuthorController.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (session == null) {
            session = req.getSession();
        }
        login = (Login) session.getAttribute("userLogin");
        setProperAttributesForwardRequest(req, resp);
        logger.debug("doGet");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        createSelectedAuthorId(req);
        switch (req.getParameter("button")) {
            case "edit":
                editAction();
                resp.sendRedirect("add-edit-author");
                break;
            case "delete":
                deleteAction(req, resp);
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
        logger.debug("doPost");
    }

    protected void createSelectedAuthorId(HttpServletRequest req) {
        if (req.getParameter("selected") != null) {
            selectedAuthorId = Integer.parseInt(req.getParameter("selected"));
            logger.debug("Selected author");
        }
    }

    protected void editAction() {
        session.setAttribute("edit", authorService.getAuthor(selectedAuthorId));
    }

    protected void deleteAction(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        if (authorService.deleteIfPossible(selectedAuthorId)) {
            setProperAttributesForwardRequest(req, resp);
            logger.debug("deleted author");
        } else {
            printMessage(resp, authorService.getMessage());
        }
    }

    protected void printMessage(HttpServletResponse resp, String message) throws IOException {
        PrintWriter out = resp.getWriter();
        out.println("<script type=\"text/javascript\">");
        out.println("alert('" + message + "');");
        out.println("location='author.jsp';");
        out.println("</script>");
        logger.debug("Printed message");
    }

    protected void setProperAttributesForwardRequest(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        initializeAuthorService();
        session.setAttribute("authorList", authorService.getAuthorList(login));
        req.getRequestDispatcher("author.jsp").forward(req, resp);
    }

    private void initializeAuthorService() {
        if (authorService == null) {
            authorService = new AuthorService();
            logger.debug("Initialized authorService");
        }
    }

    protected void setAuthorService(AuthorService authorService) {
        this.authorService = authorService;
    }

    public void setSession(HttpSession session) {
        this.session = session;
    }
}
