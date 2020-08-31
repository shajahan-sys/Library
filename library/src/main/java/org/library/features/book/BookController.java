package org.library.features.book;

import org.library.features.author.Author;
import org.library.features.login.Login;
import org.library.features.lending.Lending;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;


@WebServlet(urlPatterns = "books")
public class BookController extends HttpServlet {
    private BookService bookService;
    private List<Book> filteredBooks;
    private Login login;
    private Book book;
    private List<Author> authors;
    private HttpSession session;


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (session ==null){session= req.getSession();}
        login = (Login) session.getAttribute("userLogin");
        if (login != null) {
           setProperAttributesForwardRequest(req, resp);
        } else {
            resp.sendRedirect("login");
        }
    }
    protected void setProperAttributesForwardRequest(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        initializeBookService();
        setProperListOfAuthors(req);
        setProperListOfBooks(req);
        req.getRequestDispatcher("book.jsp").forward(req, resp);
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
                setProperAttributesForwardRequest(req, resp);
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
                session.setAttribute("authors", authors);
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
        String button = req.getParameter("button");
        if (button == null || button.equals("delete")) {
            setManagementValueIfDoesntExist(books);
            req.setAttribute("books", books);
        } else if (button.equals("search")) {
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
        session.setAttribute("reader", bookService.getBook(new Book(Integer.parseInt(req.getParameter("selected")))).getLending().getReader());
    }

    void resolveEdit(HttpServletRequest req) {
        session.setAttribute("edit", bookService.getBook(new Book(Integer.parseInt(req.getParameter("selected")))));
        session.setAttribute("authors", authors);
    }

    void resolveDelete(HttpServletRequest req) {
        bookService.deleteBook(new Book(Integer.parseInt(req.getParameter("selected"))));
    }

    void resolveLend(HttpServletRequest req) {
        session.setAttribute("lend", bookService.getBook(new Book(Integer.parseInt(req.getParameter("selected")))));
    }

    protected void filterBooks(HttpServletRequest req) {
        Book book = new Book();
        book.setTitle(req.getParameter("searchTitle"));
        if (!req.getParameter("author2").equals("no author")) {
            book.setAuthor(new Author(Integer.parseInt(req.getParameter("author2"))));
        }
        filteredBooks = bookService.filterBooks(book);
    }
    protected void setSession(HttpSession session){
        this.session = session;
    }
    public void setBookService(BookService bookService) {
        this.bookService = bookService;
    }
}
