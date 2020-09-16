package org.library.features.reader;

import org.library.features.lend_book.Lending;
import org.library.features.login.Login;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;

@WebServlet(urlPatterns = "readers")
public class ReaderController extends HttpServlet {
    private Login login;
    private HttpSession session;
    private ReaderService readerService;
    private Reader selectedReader;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (session == null) {
            session = req.getSession();
        }
        login = (Login) session.getAttribute("userLogin");
        if (login != null) {
            initializeReaderService();
            setProperAttributesForwardRequest(req, resp);
        } else {
            resp.sendRedirect("login");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        createProperSelectedReader(req);
        switch (req.getParameter("button")) {
            case "edit":
                resolveEdit();
                resp.sendRedirect("add-edit-reader");
                break;
            case "delete":
                resolveDelete(req, resp);
                break;
            case "lend":
                resolveLend();
                resp.sendRedirect("lend-book");
                break;
            case "return":
                resolveReturn(resp);
                break;
            case "add new":
                resp.sendRedirect("add-edit-reader");
                break;
            case "menu":
                resp.sendRedirect("menu");
                break;
            default:
                throw new IllegalArgumentException("Wrong button value!");
        }
    }

    protected void createProperSelectedReader(HttpServletRequest req) {
        if (req.getParameter("selected") != null) {
            selectedReader = readerService.getReader(Integer.parseInt(req.getParameter("selected")));
        }
    }

    protected void resolveEdit() {
        session.setAttribute("edit", selectedReader);
    }

    protected void resolveDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        if (selectedReader.getLendings().isEmpty()) {
            readerService.delete(selectedReader);
            session.setAttribute("readers", readerService.getReadersList(login));
            req.getRequestDispatcher("reader.jsp").forward(req, resp);
        } else {
            printMessage("Cannot delete selected reader. The reader has not returned borrowed books yet.", resp);
        }
    }

    protected void resolveLend() {
        session.setAttribute("selReader", selectedReader);
    }

    protected void resolveReturn(HttpServletResponse resp) throws IOException {
        Set<Lending> lendingSet = selectedReader.getLendings();
        if (!lendingSet.isEmpty()) {
            session.setAttribute("lendings", lendingSet);
            session.setAttribute("reader", selectedReader);
            resp.sendRedirect("return-book");
        } else {
            printMessage("Selected reader does not have any books to return.", resp);
        }
    }

    protected void setProperAttributesForwardRequest(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (session.getAttribute("readers") == null) {
            session.setAttribute("readers", readerService.getReadersList(login));
        }
        req.getRequestDispatcher("reader.jsp").forward(req, resp);
    }

    protected void printMessage(String message, HttpServletResponse resp) throws IOException {
        PrintWriter out = resp.getWriter();
        out.println("<script type=\"text/javascript\">");
        out.println("alert('" + message + "');");
        out.println("location='reader.jsp';");
        out.println("</script>");
    }

    private void initializeReaderService() {
        if (readerService == null) {
            readerService = new ReaderService();
        }
    }

    protected void setSession(HttpSession session) {
        this.session = session;
    }

    protected void setReaderService(ReaderService readerService) {
        this.readerService = readerService;
    }

}
