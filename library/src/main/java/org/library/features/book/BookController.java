package org.library.features.book;

import org.library.features.author.Author;
import org.library.features.login.Login;
import org.library.features.lend_book.Lending;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;


@WebServlet(urlPatterns = "books")
public class BookController extends HttpServlet {
    private BookService bookService;
    private List<Book> filteredBooks;
    private Login login;
    private Book selectedBook;
    private List<Author> authors;
    private HttpSession session;


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (session == null) {
            session = req.getSession();
        }
        login = (Login) session.getAttribute("userLogin");
        if (login != null) {
            setProperAttributesForwardRequest(req, resp);
        } else {
            resp.sendRedirect("login");
        }
    }

    protected void setProperAttributesForwardRequest(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        initializeBookService();
        setProperListOfAuthors();
        setProperListOfBooks(req);
        req.getRequestDispatcher("book.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getParameter("selected") != null) {
            selectedBook = bookService.getBook(Integer.parseInt(req.getParameter("selected")));
        }
        switch (req.getParameter("button")) {
            case "edit":
                resolveEdit();
                resp.sendRedirect("add-edit-book");
                break;
            case "delete":
                resolveDelete(req, resp);
                break;
            case "lend":
                resolveLend(resp);
                break;
            case "return":
                resolveReturn(resp);
                break;
            case "add new":
                session.setAttribute("authors", authors);
                resp.sendRedirect("add-edit-book");
            case "menu":
                resp.sendRedirect("menu");
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
            session.setAttribute("books", books);
        } else if (button.equals("search")) {
            filterBooks(req);
            setManagementValueIfDoesntExist(filteredBooks);
            session.setAttribute("books", filteredBooks);
        }
    }

    protected void setProperListOfAuthors() {
        authors = bookService.getAuthorsList(login);
        if (session.getAttribute("authors") == null) {
            session.setAttribute("authors", authors);
        }
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

    void resolveReturn(HttpServletResponse resp) throws IOException {
        if (selectedBook.getLending().getReturnDate().equals("available")) {
            //  req.setAttribute("loginError", "avb");
            PrintWriter out = resp.getWriter();
            out.println("<script type=\"text/javascript\">");
            out.println("alert('Cannot return this book, because it is not rented');");
            out.println("location='book.jsp';");
            out.println("</script>");
            // req.getRequestDispatcher("book.jsp").include(req, resp);
            //  setProperAttributesForwardRequest(req, resp);
        } else {
            session.setAttribute("reader", selectedBook.getLending().getReader());
            session.setAttribute("lendings", selectedBook.getLending().getReader().getLendings());
            resp.sendRedirect("return-book");
        }
    }

    void resolveEdit() {
        session.setAttribute("edit", selectedBook);
        session.setAttribute("authors", authors);
    }

    void resolveDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        if (selectedBook.getLending() == null){
            bookService.deleteBook(selectedBook);
            setProperAttributesForwardRequest(req, resp);
        }
        else {
            PrintWriter out = resp.getWriter();
            out.println("<script type=\"text/javascript\">");
            out.println("alert('Cannot delete selected book, because it is already rented. Book must be returned to be deleted.');");
            out.println("location='book.jsp';");
            out.println("</script>");
        }

    }

    void resolveLend(HttpServletResponse resp) throws IOException {
        if (selectedBook.getLending() == null){
        session.setAttribute("selBook", selectedBook);
            resp.sendRedirect("lend-book");
        }
        else {
            PrintWriter out = resp.getWriter();
            out.println("<script type=\"text/javascript\">");
            out.println("alert('Cannot lend selected book, because it is already rented.');");
            out.println("location='book.jsp';");
            out.println("</script>");
        }
    }

    protected void filterBooks(HttpServletRequest req) {
        Book book = new Book();
        book.setTitle(req.getParameter("searchTitle"));
        if (!req.getParameter("author2").equals("no author")) {
            book.setAuthor(new Author(Integer.parseInt(req.getParameter("author2"))));
        }
        filteredBooks = bookService.filterBooks(book);
    }

    protected void setSession(HttpSession session) {
        this.session = session;
    }

    public void setBookService(BookService bookService) {
        this.bookService = bookService;
    }
}
