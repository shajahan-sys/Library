package org.library.features.book;

import org.library.features.author.Author;
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
import java.util.List;

/**
 * Controller class which is a Servlet implementation. Overrides doGet and doPost methods, uses
 * BookService methods, Book and Author classes as a models. Uses RequestDispatcher object to forward
 * a request from this servlet to book.jsp file. Enables user to filter books, delete a book, can send
 * redirect response to LendingController, ReturnBookController, AddEditBookController and MenuController.
 *
 * @author Barbara Grabowska
 * @version %I%, %G%
 */
@WebServlet(urlPatterns = "/books")
public class BookController extends HttpServlet {
    private BookService bookService;
    /**
     * Logger instance for this class
     */
    private final Logger logger = LoggerFactory.getLogger(BookController.class);

    /**
     * Overrides doGet method. Binds proper session attributes using setProperListOfAuthors
     * and setProperListOfBooks methods. Forwards a request from a servlet to JSP file.
     *
     * @param req  HttpServletRequest object that contains the request the client has made of the servlet
     * @param resp HttpServletResponse object that contains the response the servlet sends to the client
     * @throws ServletException if an input or output error is detected when the servlet handles the GET request
     * @throws IOException      if the request for the GET could not be handled
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        initializeBookService();
        setProperListOfAuthors(req);
        setProperListOfBooks(req);
        req.getRequestDispatcher("book.jsp").forward(req, resp);
        logger.debug("doGet");
    }

    /**
     * Overrides doPost method. Decides what action to take, based on req parameter "button" value.
     * Throws IllegalArgumentException by default.
     *
     * @param req  object that contains the request the client has made of the servlet
     * @param resp an HttpServletResponse object that contains the response the servlet sends to the client
     * @throws ServletException if an input or output error is detected when the servlet handles the request
     * @throws IOException      if the request for the POST could not be handled
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        switch (req.getParameter("button")) {
            case "edit":
                editAction(req, resp);
                break;
            case "delete":
                deleteAction(req, resp);
                break;
            case "lend":
                lendAction(req, resp);
                break;
            case "return":
                returnAction(req, resp);
                break;
            case "add new":
                resp.sendRedirect("add-edit-book");
            case "menu":
                resp.sendRedirect("menu");
                break;
            default:
                throw new IllegalArgumentException("Wrong button value!");
        }
        logger.debug("doPost");
    }

    /**
     * Sets proper list of Book objects as session attribute "books", depending on button
     * value. If req parameter button is null, this method sets proper list using bookService
     * method - getBooksList. If the button value equals "search", this method sets proper
     * list od books using getFilteredBooks method. This method also checks session
     * attribute "saved" value. If "saved" does not equal null it means, that some
     * changes might have occurred (for example a book was edited), so list of books
     * stored in a bookService's map may be no longer up-to-date, then this method
     * calls booksService method - deleteFromMap and removes session attribute "saved".
     *
     * @param req object that contains the request the client has made of the servlet
     */
    protected void setProperListOfBooks(HttpServletRequest req) {
        HttpSession session = req.getSession();
        Login login = (Login) session.getAttribute("userLogin");
        String button = req.getParameter("button");
        if (button == null) {
            deleteFromMapIfNeeded(req, "saved", "booksMightHaveChanged");
            session.setAttribute("books", bookService.getBooksList(login));
            logger.debug("Set list of books");
        } else if (button.equals("search")) {
            session.setAttribute("books", getFilteredBooks(req));
            logger.debug("Set filtered books list");
        }
    }

    /**
     * Removes session attributes if exist.
     *
     * @param req object that contains the request the client has made of the servlet
     * @param attributeNames Strings that represent names of session attributes to remove
     */
    protected void deleteFromMapIfNeeded(HttpServletRequest req, String... attributeNames){
        HttpSession session = req.getSession();
        for (String name:attributeNames
        ) {
            if (session.getAttribute(name) != null) {
                bookService.deleteFromMap((Login) session.getAttribute("userLogin"));
                session.removeAttribute(name);
            }
        }
    }
    /**
     * Sets session attribute named "authorList" which is a  proper list of Author
     * objects using bookService method - getAuthorsList if does not already exist.
     *
     * @param req object that contains the request the client has made of the servlet
     */
    protected void setProperListOfAuthors(HttpServletRequest req) {
        HttpSession session = req.getSession();
        if (session.getAttribute("authorList") == null) {
            session.setAttribute("authorList", bookService.getAuthorsList((Login) session.getAttribute("userLogin")));
            logger.debug("Got Author objects list");
        }
    }

    /**
     * Checks that selected Book object is on a loan using bookService method -
     * - isLendingNull, if so, sets session attributes "reader" and "lendings"
     * that represents Reader object, who borrowed selected book.
     * Sends a redirect response to ReturnBookController.
     * Otherwise (when selected book is not on a loan), this method calls
     * printMessage method.
     *
     * @param req  object that contains the request the client has made of the servlet
     * @param resp an HttpServletResponse object that contains the response the servlet sends to the client
     * @throws IOException if an I/O exception of some sort has occurred
     */
    void returnAction(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession();
        Login login = (Login) session.getAttribute("userLogin");
        int bookId = Integer.parseInt(req.getParameter("selected"));
        if (!bookService.isLendingNull(login, bookId)) {
            Book book = bookService.getBook(login, bookId);
            session.setAttribute("reader", book.getLending().getReader());
            resp.sendRedirect("return-book");
            logger.debug("Sent a redirect response to ReturnBookController");
        } else {
            printMessage(resp);
        }
    }

    /**
     * Sets session attribute - proper Book object named "edit", using bookService method - getBook.
     * Sends a redirect response to AddEditBookController.
     *
     * @param req  object that contains the request the client has made of the servlet
     * @param resp an HttpServletResponse object that contains the response the servlet sends to the client
     * @throws IOException if an I/O exception of some sort has occurred
     */
    void editAction(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession();
        session.setAttribute("edit", bookService.getBook((Login) session.getAttribute("userLogin"),
                Integer.parseInt(req.getParameter("selected"))));
        resp.sendRedirect("add-edit-book");
        logger.debug("Sent a redirect response to AddEditBookController");
    }

    /**
     * Deletes selected Book object if possible, then sets up-to-date list of Book objects
     * as "books" session attribute and "authorsMightHaveChanged" attribute.
     * Forwards a request to book.jsp file. If selected book can not be deleted
     * this method calls printMessage method.
     *
     * @param req  object that contains the request the client has made of the servlet
     * @param resp an HttpServletResponse object that contains the response the servlet sends to the client
     * @throws ServletException if an input or output error is detected when the servlet handles the request
     * @throws IOException      if an I/O exception of some sort has occurred
     */
    void deleteAction(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        HttpSession session = req.getSession();
        Login login = (Login) session.getAttribute("userLogin");
        if (bookService.deleteIfPossible(login, Integer.parseInt(req.getParameter("selected")))) {
            session.setAttribute("books", bookService.getBooksList(login));
            session.setAttribute("authorsMightHaveChanged", true);
            req.getRequestDispatcher("book.jsp").forward(req, resp);
            logger.debug("deleted book");
        } else {
            printMessage(resp);
        }
    }

    /**
     * Checks that selected book can be lent, if so, then binds proper Book object to
     * a session, using name "selBook" and sends a redirect response to LendingController.
     * Otherwise calls printMessage method.
     *
     * @param req  object that contains the request the client has made of the servlet
     * @param resp an HttpServletResponse object that contains the response the servlet sends to the client
     * @throws IOException if an I/O exception of some sort has occurred
     */
    void lendAction(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        int bookId = Integer.parseInt(req.getParameter("selected"));
        HttpSession session = req.getSession();
        Login login = (Login) session.getAttribute("userLogin");
        if (bookService.isLendingNull(login, bookId)) {
            session.setAttribute("selBook", bookService.getBook(login, bookId));
            resp.sendRedirect("lend-book");
            logger.debug("Sent a redirect response to LendingController");
        } else {
            printMessage(resp);
        }
    }

    /**
     * Gets List of Book objects, that contains provided title and/or author. Creates book object
     * and sets its title and author(if has been chosen). Uses bookService method - filterBooks
     * and created book object to get proper list of books.
     *
     * @param req object that contains the request the client has made of the servlet
     * @return List of Book objects that fulfil the requirements
     */
    protected List<Book> getFilteredBooks(HttpServletRequest req) {
        Book book = new Book();
        book.setTitle(req.getParameter("searchTitle"));
        if (!req.getParameter("author2").equals("no author")) {
            book.setAuthor(new Author(Integer.parseInt(req.getParameter("author2"))));
        }
        return bookService.filterBooks(book, (Login) req.getSession().getAttribute("userLogin"));
    }

    /**
     * Creates PrintWriter object and uses it to print specified Strings. These Strings should display
     * an alert box (in book.jsp file) with a message got using bookService.
     *
     * @param resp HttpServletResponse object that contains the response the servlet sends to the client
     * @throws IOException if an I/O exception of some sort has occurred
     */
    protected void printMessage(HttpServletResponse resp) throws IOException {
        String message = bookService.getMessage();
        PrintWriter out = resp.getWriter();
        out.println("<script type=\"text/javascript\">");
        out.println("alert('" + message + "');");
        out.println("location='book.jsp';");
        out.println("</script>");
        logger.debug("Printed message " + message);
    }

    /**
     * Initializes bookService that has not already been initialized.
     */
    protected void initializeBookService() {
        if (bookService == null) {
            bookService = new BookService();
            logger.debug("Initialized bookService");
        }
    }

    /**
     * @param bookService to set
     */
    public void setBookService(BookService bookService) {
        this.bookService = bookService;
    }
}
