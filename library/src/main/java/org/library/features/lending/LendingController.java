package org.library.features.lending;

import org.library.features.book.Book;
import org.library.features.login.Login;
import org.library.features.reader.Reader;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(urlPatterns = "lending")
public class LendingController extends HttpServlet {
    private LendingService lendingService;
    private Login login;
    private Reader selectedReader;
    private Book selectedBook;
    private HttpSession session;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        session = req.getSession();
        login = (Login) session.getAttribute("userLogin");
        selectedBook = (Book) session.getAttribute("selBook");
        selectedReader = (Reader) session.getAttribute("selReader");
        if (login != null) {
            initializeLendingService();
            setRequestAttributes(req);
            req.getRequestDispatcher("lending.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if (req.getParameter("button").equals("lend")) {
            resolveLend(req);
        }
        removeSessionAttributes();
        resp.sendRedirect("books");
    }

    protected void setRequestAttributes(HttpServletRequest req) {
        req.setAttribute("books", lendingService.getAvailableBooksList(login));
        req.setAttribute("readers", lendingService.getReadersList(login));
        if (selectedBook != null) {
            req.setAttribute("selBook", selectedBook);
        }
        if (selectedReader != null) {
            req.setAttribute("selReader", selectedReader);
        }

    }

    private void initializeLendingService() {
        if (lendingService == null) {
            lendingService = new LendingService();
        }
    }

    protected Lending createProperLendingObj(HttpServletRequest req) {
        Lending lending = new Lending();
        lending.setBook(lendingService.getBook(Integer.parseInt(req.getParameter("book"))));
        lending.setReader(lendingService.getReader(Integer.parseInt(req.getParameter("reader"))));
        lending.setReturnDate(req.getParameter("date"));
        lending.setLogin(login);
        return lending;
    }

    protected void resolveLend(HttpServletRequest req) {
        lendingService.saveLending(createProperLendingObj(req));
        removeSessionAttributes();
    }

    protected void removeSessionAttributes() {
        if (selectedReader != null) {
            session.removeAttribute("selReader");
        }
        if (selectedBook != null) {
            session.removeAttribute("selBook");
        }
    }
}
