package org.library;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.library.features.login.Login;
import org.library.features.reader.Reader;
import org.library.hibernate_util.HibernateTestUtil;
import org.library.hibernate_util.HibernateUtil;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

@WebServlet(name = "org.library.AddServlet", urlPatterns = {"add"}, loadOnStartup = 1, initParams = { @WebInitParam(name = "saveDir", value = "D:/FileUpload"),
        @WebInitParam(name = "allowedTypes", value = "jpg,jpeg,gif,png")
})
public class AddServlet extends HttpServlet {
    private int result;
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
       //int number1 = Integer.parseInt(request.getParameter("num1"));
       //int number2 = Integer.parseInt(request.getParameter("num2"));
       //result = number1 + number2;
        response.getWriter().println("the result is:" + result);
    }
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int number1 = Integer.parseInt(request.getParameter("num1"));
        int number2 = Integer.parseInt(request.getParameter("num2"));
        result = number1 + number2;
        HttpSession session = request.getSession();
        if (number1 != 3){
        Cookie cookie = new Cookie("kk", result+"");
        response.addCookie(cookie);

        session.setAttribute("res", result);
        session.setAttribute("s", "SSSSS");
        response.sendRedirect("square");}
       else {
            //request.setAttribute("user", name);
            session.setAttribute("s", "SSSSS");
            Reader r1 = new Reader();
            r1.setName("pol");
            Reader r2 = new Reader();
            r2.setName("kool");
            List<Reader> rl = Arrays.asList(r1, r2);
            request.setAttribute("r", rl);
            request.getRequestDispatcher("home.jsp").forward(request, response);
        }  // response.sendRedirect("square?k=" + result);
        //RequestDispatcher dispatcher = request.getRequestDispatcher("square");
        //request.setAttribute("result", result);
        //dispatcher.forward(request, response);

    }
    static void should(){
        Login login = new Login();
        login.setUserName("pp");
        login.setPassword("ko");
        SessionFactory factory = HibernateTestUtil.getTestSessionFactory();
        Session session = factory.openSession();
        session.getTransaction().begin();
        session.save(login);
        session.getTransaction().commit();
        session.close();

        // assertNotNull(se.createNewAccount(login));
    }

    public static void main(String[] args) {
        should();
    }


}
