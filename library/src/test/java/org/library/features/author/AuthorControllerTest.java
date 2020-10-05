package org.library.features.author;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.library.features.login.Login;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthorControllerTest {
    private AuthorController authorController;
    @Mock
    private AuthorService authorService;
    @Mock
    HttpServletResponse resp;
    @Mock
    HttpServletRequest req;
    @Spy
    HttpSession session;
    @Mock
    private RequestDispatcher rd;
    private List<Author> authors;
    private Login myLogin;
    private Author myAuthor;
    private final ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);

    @BeforeEach
    void setUp() {
        authorController = new AuthorController();
        MockitoAnnotations.openMocks(this);
        myLogin = new Login();
        when(req.getSession()).thenReturn(session);
        authorController.setAuthorService(authorService);
    }

    @AfterEach
    void tearDown() {
        authorController = null;
        myLogin = null;
    }


    @Test
    void test_editAction() {
        myAuthor = new Author(1);
        mockSelectedParameterAndUserLoginAttribute();
        when(authorService.getAuthor(1, myLogin)).thenReturn(myAuthor);
        setRequestDispatcher();
        authorController.editAction(req);
        verify(session).setAttribute("edit", myAuthor);
    }

    @Test
    void test_deleteAction_when_author_can_be_deleted() throws IOException, ServletException {
        mockSelectedParameterAndUserLoginAttribute();
        when(authorService.deleteIfPossible(1, myLogin)).thenReturn(true);
        prepareAuthorList();
        setRequestDispatcher();
        authorController.deleteAction(req, resp);
        assertAll(
                () -> verifyNoInteractions(resp),
                () -> verify(session).setAttribute("authorList", authors),
                () -> verify(req).getRequestDispatcher(captor.capture()),
                () -> assertEquals("author.jsp", captor.getValue()));

    }

    @Test
    void test_deleteAction_when_author_can_not_be_deleted() throws IOException, ServletException {
        mockSelectedParameterAndUserLoginAttribute();
        when(authorService.deleteIfPossible(1, myLogin)).thenReturn(false);
        PrintWriter pw = mock(PrintWriter.class);
        when(resp.getWriter()).thenReturn(pw);
        authorController.deleteAction(req, resp);
        assertAll(
                () -> verify(resp).getWriter(),
                () -> verify(authorService).getMessage()
        );
    }

    @Test
    void test_setProperAttributes_when_saved_attribute_does_not_exist() {
        prepareAuthorList();
        when(session.getAttribute("userLogin")).thenReturn(myLogin);
        setRequestDispatcher();
        authorController.setProperAttributes(req);
        verify(session).setAttribute("authorList", authors);

    }

    @Test
    void test_setProperAttributes_when_saved_attribute_exists() {
        prepareAuthorList();
        when(session.getAttribute("userLogin")).thenReturn(myLogin);
        when(session.getAttribute("saved")).thenReturn(true);
        setRequestDispatcher();
        authorController.setProperAttributes(req);
        assertAll(
                () -> verify(authorService).deleteFromMap(myLogin),
                () -> verify(session).removeAttribute("saved"),
                () -> verify(session).setAttribute("authorList", authors));
    }


    @Test
    void test_doGet() throws ServletException, IOException {
        setRequestDispatcher();
        authorController.doGet(req, resp);
        assertAll(
                () -> verify(session).getAttribute("userLogin"),
                () -> verify(req).getRequestDispatcher(captor.capture()),
                () -> assertEquals("author.jsp", captor.getValue()));
    }

    @Test
    void test_doPost_when_button_equals_edit() throws ServletException, IOException {
        mockSelectedParameterAndUserLoginAttribute();
        doPostUtils("edit", "add-edit-author");
    }

    @Test
    void test_doPost_when_button_equals_add_new() throws ServletException, IOException {
        doPostUtils("add new", "add-edit-author");
    }

    @Test
    void test_doPost_when_button_equals_menu() throws ServletException, IOException {
        doPostUtils("menu", "menu");
    }

    @Test
    void test_doPost_when_button_value_is_not_proper() {
        when(req.getParameter("button")).thenReturn("wrong");
        assertThrows(IllegalArgumentException.class, () -> authorController.doPost(req, resp));
    }

    private void prepareAuthorList() {
        authors = new ArrayList<>();
        authors.add(myAuthor);
        when(authorService.getAuthorList(myLogin)).thenReturn(authors);

    }

    private void mockSelectedParameterAndUserLoginAttribute() {
        when(req.getParameter("selected")).thenReturn("1");
        when(session.getAttribute("userLogin")).thenReturn(myLogin);
    }

    private void doPostUtils(String buttonName, String servletPath) throws ServletException, IOException {
        when(req.getParameter("button")).thenReturn(buttonName);
        authorController.doPost(req, resp);
        assertAll(
                () -> verify(resp).sendRedirect(captor.capture()),
                () -> assertEquals(servletPath, captor.getValue())
        );
    }

    private void setRequestDispatcher() {
        when(req.getRequestDispatcher(captor.capture())).thenReturn(rd);
    }
}