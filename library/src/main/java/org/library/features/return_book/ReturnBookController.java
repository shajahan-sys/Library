package org.library.features.return_book;

import org.library.features.lend_book.Lending;
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

@WebServlet("/return-book")
public class ReturnBookController extends HttpServlet {
    private ReturnBookService returnBookService;
    private Reader reader;
    private HttpSession session;
    private Set<Lending> lendingSet;
    private Login login;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        session = req.getSession();
        login = (Login) session.getAttribute("userLogin");
            setProperRequestAttributes(req);
            req.getRequestDispatcher("returnBook.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if (req.getParameter("button").equals("return")) {
            returnBookService.deleteLending(new Lending(Integer.parseInt(req.getParameter("selected"))));
            removeSessionAttributes();
            resp.sendRedirect("books");
        } else if (req.getParameter("button").equals("cancel")) {
            removeSessionAttributes();
            resp.sendRedirect("menu");
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
        initializeReturnBookService();
        reader = (Reader) session.getAttribute("reader");
        lendingSet = (Set<Lending>) session.getAttribute("lendings");
        String button = req.getParameter("button");
        if (button == null && reader != null && lendingSet != null) {
            req.setAttribute("selectedReader", reader);
            req.setAttribute("lendings", lendingSet);
        }
        if (button != null && button.equals("submit")) {
            resolveSubmit(req);
        }
        setActiveReaders(req);
    }

    protected void setActiveReaders(HttpServletRequest req) {
        if (req.getAttribute("readers") == null) {
            req.setAttribute("readers", returnBookService.getActiveReadersList(login));
        }
    }

    protected void initializeReturnBookService() {
        if (returnBookService == null) {
            returnBookService = new ReturnBookService();
        }
    }

    protected void resolveSubmit(HttpServletRequest req) {
        String readerId = req.getParameter("selReader");
        if (!readerId.equals("no reader")) {
            Reader reader = returnBookService.getReader(Integer.parseInt(readerId));
            Set<Lending> lendingSet = reader.getLendings();
            req.setAttribute("lendings", lendingSet);
            req.setAttribute("selectedReader", reader);
        }
    }
}
