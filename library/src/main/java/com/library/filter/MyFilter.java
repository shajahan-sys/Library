package com.library.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * This class is a Filter implementation. Checks if user is logged in using session
 * attribute "userLogin", if not sends a redirect response to LoginController.
 *
 * @author Barbara Grabowska
 * @version %I%, %G%
 */
@WebFilter(urlPatterns = {"/authors", "/books", "/lend-book", "/menu", "/readers", "/return-book", "/add-edit-author", "/add-edit-reader", "/add-edit-book"})
public class MyFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        HttpSession session = req.getSession();
        if (session.getAttribute("userLogin") != null) {
            chain.doFilter(request, response);
        } else {
            resp.sendRedirect("login");
        }
    }

    @Override
    public void destroy() {

    }
}
