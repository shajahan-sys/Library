package org.library.features.lend_book;

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
import java.io.PrintWriter;

@WebServlet(urlPatterns = "lend-book")
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
        } else {
            resp.sendRedirect("login.jsp");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if (req.getParameter("button").equals("lend")) {
            resolveLend(req, resp);
            //  removeSessionAttributes();
            //   resp.sendRedirect("books");
        } else if (req.getParameter("button").equals("cancel")) {
            removeSessionAttributes();
            resp.sendRedirect("menu");
        }

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

    protected Lending getProperLendingObject(HttpServletRequest req) {
        Lending lending = new Lending();
        lending.setBook(lendingService.getBook(Integer.parseInt(req.getParameter("book"))));
        lending.setReader(lendingService.getReader(Integer.parseInt(req.getParameter("reader"))));
        lending.setReturnDate(req.getParameter("date"));
        lending.setLogin(login);
        return lending;
    }

    protected boolean isDateFormatProper(String input) {
        return input.matches("([0-9]{4})-([0-9]{2})-([0-9]{2})");
    }

    protected void resolveLend(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if (isDateFormatProper(req.getParameter("date"))) {
            lendingService.saveLending(getProperLendingObject(req));
            removeSessionAttributes();
            resp.sendRedirect("books");
        } else {
            PrintWriter out = resp.getWriter();
            out.println("<script type=\"text/javascript\">");
            out.println("alert('The specified date format is not valid. Use the yyyy-mm-dd format.');");
            out.println("location='lending.jsp';");
            out.println("</script>");
        }
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
