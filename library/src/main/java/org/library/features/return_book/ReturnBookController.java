package org.library.features.return_book;

import org.library.features.lending.Lending;
import org.library.features.login.Login;
import org.library.features.reader.Reader;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Set;

@WebServlet("return-book")
public class ReturnBookController extends HttpServlet {
    private ReturnBookService returnBookService;
    private Reader reader;
    private HttpSession session;
    private Set<Lending> lendingSet;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        session = req.getSession();
        Login login = (Login) session.getAttribute("userLogin");
        if (login != null) {
            setProperRequestAttributes(req);
            req.setAttribute("readers", returnBookService.getActiveReadersList(login));
            req.getRequestDispatcher("returnBook.jsp").forward(req, resp);
        } else {
            resp.sendRedirect("login.jsp");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if (req.getParameter("button").equals("return")) {
            returnBookService.deleteLending(new Lending(Integer.parseInt(req.getParameter("selected"))));
            removeSessionAttributes();
            resp.sendRedirect("books");
        } else if (req.getParameter("button").equals("cancel")) {
            removeSessionAttributes();
            resp.sendRedirect("welcome");
        }

    }

    protected void removeSessionAttributes() {
        if (reader != null) {
            session.removeAttribute("reader");
        }
        if (lendingSet != null) {
            session.removeAttribute("lendings");
        }
    }

    protected void setProperRequestAttributes(HttpServletRequest req) {
        reader = (Reader) session.getAttribute("reader");
        lendingSet = (Set<Lending>) session.getAttribute("lendings");
        initializeReturnBookService();
        if (reader != null && lendingSet != null) {
            req.setAttribute("selectedReader", reader);
            req.setAttribute("lendings", lendingSet);
        }
        if (req.getParameter("button") != null && req.getParameter("button").equals("submit"))
            resolveSubmit(req);
    }

    protected void initializeReturnBookService() {
        if (returnBookService == null) {
            returnBookService = new ReturnBookService();
        }
    }

    protected void resolveSubmit(HttpServletRequest req) {
        Reader reader = returnBookService.getReader(Integer.parseInt(req.getParameter("selReader")));
        Set<Lending> lendingSet = reader.getLendings();
        req.setAttribute("lendings", lendingSet);
        req.setAttribute("selectedReader", reader);
    }
}
