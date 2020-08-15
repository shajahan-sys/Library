package org.library.features.book;

import org.library.features.login.Login;

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
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getSession().getAttribute("userLogin")!=null){
        bookService = new BookService();
        System.out.println("IM IN");
        List<Book> books = bookService.getBooksList((Login) req.getSession().getAttribute("userLogin"));
        req.setAttribute("books", books);
        req.getRequestDispatcher("book.jsp").forward(req, resp);}
        else {
            resp.sendRedirect("login");
        }
    }

    //@Override
   // protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    //}
}
