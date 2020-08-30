package org.library.features.book;

import org.library.features.author.Author;
import org.library.features.login.Login;
import org.library.features.lending.Lending;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;


@WebServlet(urlPatterns = "books")
public class BookController extends HttpServlet {
    private BookService bookService;
    private List<Book> filteredBooks;
    private Login login;
    private Book book;
    private List<Author> authors;


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        login = (Login) req.getSession().getAttribute("userLogin");
        if (login != null) {
            initializeBookService();
            setProperListOfAuthors(req);
            setProperListOfBooks(req);
            req.getRequestDispatcher("book.jsp").forward(req, resp);
        } else {
            resp.sendRedirect("login");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (book == null) {
            book = new Book();
        }
        switch (req.getParameter("button")) {
            case "edit":
                resolveEdit(req);
                resp.sendRedirect("add-edit-book");
                break;
            case "delete":
                resolveDelete(req);
                doGet(req, resp);
                break;
            case "lend":
                resolveLend(req);
                resp.sendRedirect("lending");
                break;
            case "return":
                resolveReturn(req);
                resp.sendRedirect("return-book");
                break;
            case "add new":
                resp.sendRedirect("add-edit-book");
            case "logout":
                resp.sendRedirect("logout");
                break;
            default:
                throw new IllegalArgumentException("Wrong button value!");
        }
    }

    protected void setProperListOfBooks(HttpServletRequest req) {
        List<Book> books = bookService.getBooksList(login);
        if (req.getParameter("button") == null) {
            setManagementValueIfDoesntExist(books);
            req.setAttribute("books", books);
        } else if (req.getParameter("button").equals("search")) {
            filterBooks(req);
            setManagementValueIfDoesntExist(filteredBooks);
            req.setAttribute("books", filteredBooks);
        }
    }

    protected void setProperListOfAuthors(HttpServletRequest req) {
        authors = bookService.getAuthorsList(login);
        req.setAttribute("authors", authors);
    }

    protected void initializeBookService() {
        if (bookService == null) {
            bookService = new BookService();
        }
    }

    protected void setManagementValueIfDoesntExist(List<Book> bookList) {
        bookList.forEach(book -> {
            if (book.getLending() == null) {
                book.setLending(new Lending());
                book.getLending().setReturnDate("available");
            }
        });
    }

    void resolveReturn(HttpServletRequest req) {
        req.getSession().setAttribute("reader", bookService.getBook(new Book(Integer.parseInt(req.getParameter("selected")))).getLending().getReader());
    }

    void resolveEdit(HttpServletRequest req) {
        req.getSession().setAttribute("edit", bookService.getBook(new Book(Integer.parseInt(req.getParameter("selected")))));
        req.getSession().setAttribute("authors", authors);
    }

    void resolveDelete(HttpServletRequest req) {
        bookService.deleteBook(new Book(Integer.parseInt(req.getParameter("selected"))));
    }

    void resolveLend(HttpServletRequest req) {
        req.getSession().setAttribute("lend", bookService.getBook(new Book(Integer.parseInt(req.getParameter("selected")))));
    }

    protected void filterBooks(HttpServletRequest req) {
        Book book = new Book();
        book.setTitle(req.getParameter("searchTitle"));
        if (!req.getParameter("author2").equals("no author")) {
            book.setAuthor(new Author(Integer.parseInt(req.getParameter("author2"))));
        }
        filteredBooks = bookService.filterBooks(book);
    }

    public void setBookService(BookService bookService) {
        this.bookService = bookService;
    }
}
