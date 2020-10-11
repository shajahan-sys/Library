package org.library.features.logout;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Enumeration;

/**
 * Controller class which is a Servlet implementation. Overrides doPost method,
 * can send a redirect response to LoginController.
 *
 * @author Barbara Grabowska
 * @version %I%, %G%
 */
@WebServlet(urlPatterns = "logout")
public class LogoutController extends HttpServlet {
    /**
     * Overrides doPost method, invalidates given session object, sends a
     * redirect response to LoginController.
     *
     * @param req  object that contains the request the client has made of the servlet
     * @param resp an HttpServletResponse object that contains the response the servlet sends to the client
     * @throws IOException if the request for the POST could not be handled
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession();
        //  session.removeAttribute("userLogin");
     /*   Enumeration<String> names = session.getAttributeNames();
       names.asIterator().forEachRemaining(session::removeAttribute);
      /*  while (names.hasMoreElements()){
            session.removeAttribute(names.nextElement());
        }

       */

        session.invalidate();

        resp.sendRedirect("login");
    }
}
