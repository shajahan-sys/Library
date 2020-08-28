package org.library.features.book;

import org.library.features.author.Author;
import org.library.features.login.Login;
import org.library.features.management.Management;

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


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        login = (Login) req.getSession().getAttribute("userLogin");
        if (login != null) {
            initializeBookService();
            if (req.getParameter("button") != null && req.getParameter("button").equals("cancel")) {
                bookService.setEditBookToNull();
            }
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
                doGet(req, resp);
                break;
            case "delete":
                resolveDelete(req);
                doGet(req, resp);
                break;
            case "save":
                resolveSave(req);
                doGet(req, resp);
                break;
            case "manage":
                resolveManage(req);
                resp.sendRedirect("management");
                break;
            case "logout":
                resp.sendRedirect("logout");
                break;
            default:
                throw new IllegalArgumentException("Wrong button value!");
        }
    }

    protected void setProperListOfBooks(HttpServletRequest req) {
        List<Book> books = bookService.getBooksList(login);
        if (req.getParameter("button") == null || !req.getParameter("button").equals("search")) {
            setManagementValueIfDoesntExist(books);
            req.setAttribute("books", books);
        } else if (req.getParameter("button").equals("search")) {
            filterBooks(req);
            setManagementValueIfDoesntExist(filteredBooks);
            req.setAttribute("books", filteredBooks);
        }
    }

    protected void setProperListOfAuthors(HttpServletRequest req) {
        List<Author> authors = bookService.getAuthorsList(login);
        req.setAttribute("authors", authors);
    }

    protected void initializeBookService() {
        if (bookService == null) {
            bookService = new BookService();
        }
    }

    protected void setManagementValueIfDoesntExist(List<Book> bookList) {
        bookList.forEach(book -> {
            if (book.getManagement() == null) {
                book.setManagement(new Management());
                book.getManagement().setReturnDate("available");
            }
        });
    }

    void resolveEdit(HttpServletRequest req) {
        req.setAttribute("edit", bookService.getBook(new Book(Integer.parseInt(req.getParameter("selected")))));
    }

    void resolveDelete(HttpServletRequest req) {
        bookService.deleteBook(new Book(Integer.parseInt(req.getParameter("selected"))));
    }

    void resolveSave(HttpServletRequest req) {
        book = new Book();
        book.setTitle(req.getParameter("editTitle"));
        book.setAuthor(new Author(Integer.parseInt(req.getParameter("author1"))));
        book.setPublicationYear(req.getParameter("year"));
        book.setLogin(login);
        bookService.saveBook(book);
    }

    void resolveManage(HttpServletRequest req) {
        req.getSession().setAttribute("manageBook", bookService.getBook(new Book(Integer.parseInt(req.getParameter("selected")))));
        bookService.setEditBookToNull();
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
