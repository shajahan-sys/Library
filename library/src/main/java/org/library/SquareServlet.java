package org.library;

import javax.servlet.ServletContext;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "org.library.SquareServlet", urlPatterns = {"square"})
public class SquareServlet extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter out = response.getWriter();
        int a = (int) request.getSession().getAttribute("res");
        //int a = Integer.parseInt(request.getParameter("k"));
        int resuult = a * a;
        out.println("<html><body bgcolor = 'cyan'>");
        out.println("SQUAREE " + resuult + "<br>" + request.getCookies()[0].getValue());
        out.println("</body></html>");
        ServletContext ctx = getServletContext();

        System.out.println(getInitParameter("saveDir") + " " + ctx.getInitParameterNames());

        // out.println("SQUAREE " + request.getAttribute("result").toString());
    }
}
