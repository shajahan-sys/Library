package org.library.features.reader;

import org.library.features.lending.Lending;
import org.library.features.login.Login;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Set;

@WebServlet(urlPatterns = "readers")
public class ReaderController extends HttpServlet {
    private Login login;
    private HttpSession session;
    private ReaderService readerService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (session == null) {
            session = req.getSession();
        }
        login = (Login) session.getAttribute("userLogin");
        if (login != null) {
            setProperAttributesForwardRequest(req, resp); }
        else {
        resp.sendRedirect("login.jsp");}
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        switch (req.getParameter("button")) {
            case "edit":
                resolveEdit(req);
                resp.sendRedirect("add-edit-reader");
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
                resp.sendRedirect("add-edit-reader");
            case "logout":
                resp.sendRedirect("logout");
                break;
            default:
                throw new IllegalArgumentException("Wrong button value!");
        }
    }
    protected void resolveEdit(HttpServletRequest req){
        session.setAttribute("edit", readerService.getReader(Integer.parseInt(req.getParameter("selected"))));
    }
    protected void resolveDelete(HttpServletRequest req){
        readerService.delete(readerService.getReader(Integer.parseInt(req.getParameter("selected"))));
    }
    protected void resolveLend(HttpServletRequest req){
        Reader reader = readerService.getReader(Integer.parseInt(req.getParameter("selected")));
        session.setAttribute("selReader", reader);
    }
    protected void resolveReturn(HttpServletRequest req){
        Reader reader = readerService.getReader(Integer.parseInt(req.getParameter("selected")));
        Set<Lending> lendingSet = reader.getLendings();
        session.setAttribute("lendings", lendingSet);
        session.setAttribute("reader", reader);

    }
    protected void setProperAttributesForwardRequest(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        initializeReaderService();
        req.setAttribute("readers", readerService.getReadersList(login));
        req.getRequestDispatcher("reader.jsp").forward(req, resp);

    }

    private void initializeReaderService() {
        if (readerService == null) {
            readerService = new ReaderService();
        }
    }
    protected void setReaderService(ReaderService readerService){
        this.readerService = readerService;
    }

}
