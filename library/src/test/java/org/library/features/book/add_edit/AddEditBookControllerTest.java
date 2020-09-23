package org.library.features.book.add_edit;

import org.library.features.book.Book;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Spy;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

class AddEditBookControllerTest {
    @Mock
    private AddEditBookService addEditBookService;
    @Mock
    private HttpServletRequest req;
    @Mock
    private HttpServletResponse resp;
    @Spy
    HttpSession session;

    private AddEditBookController addEditBookController;
    private final ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
    private Book myBook;

    /*@BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        addEditBookController = new AddEditBookController();
        myBook = new Book();
    }

    @AfterEach
    void tearDown() {
        addEditBookController = null;
        myBook = null;
    }

    @Test
    void test_createBookWithProperData() {
        when(req.getParameter("editTitle")).thenReturn("title");
        when(req.getParameter("author1")).thenReturn("1");
        when(req.getParameter("year")).thenReturn("2020");
        myBook.setTitle("title");
        myBook.setAuthor(new Author(1));
        myBook.setPublicationYear("2020");
        Book bookActual = addEditBookController.createBookWithProperData(req);
        assertAll(
                () -> assertEquals(bookActual.getTitle(), myBook.getTitle()),
                () -> assertEquals(bookActual.getAuthor().getId(), myBook.getAuthor().getId()),
                () -> assertEquals(bookActual.getPublicationYear(), myBook.getPublicationYear()),
                () -> assertNull(bookActual.getLogin())
        );
    }

    @Test
    void test_saveBook_when_editedBook_is_null() {
        addEditBookController.setAddEditService(addEditBookService);
        addEditBookController.save(myBook);
        verify(addEditBookService).save(myBook);
    }

    @Test
    void test_saveBook_when_editedBook_is_not_null_should_set_id() throws ServletException, IOException {
        when(req.getSession()).thenReturn(session);
        when(session.getAttribute("edit")).thenReturn(new Book(2));
        addEditBookController.setAddEditService(addEditBookService);
        addEditBookController.doGet(req, resp);
        Book newBook = new Book(2);
        addEditBookController.save(myBook);
        assertEquals(newBook.getId(), myBook.getId());
    }

    @Test
    void test_setProperAttributes_when_editBook_is_null() {
        List<Author> list = new ArrayList<>();
        when(req.getSession()).thenReturn(session);
        when(session.getAttribute("authors")).thenReturn(list);
        addEditBookController.setProperAttributes(req);
        verify(req).setAttribute("authors", list);
    }

    @Test
    void test_setProperAttributes_when_editBook_is_not_null() throws ServletException, IOException {
        List<Author> list = new ArrayList<>();
        myBook = new Book(2);
        when(req.getSession()).thenReturn(session);
        when(session.getAttribute("edit")).thenReturn(myBook);
        when(session.getAttribute("authors")).thenReturn(list);
        addEditBookController.doGet(req, resp);
        addEditBookController.setProperAttributes(req);
        verify(req).setAttribute("edit", myBook);
    }

    @Test
    void test_doPost_when_button_does_not_equal_save_and_editedBook_is_null() throws IOException {
        when(req.getParameter("button")).thenReturn("cancel");
        addEditBookController.doPost(req, resp);
        assertAll(
                () -> verify(resp).sendRedirect(captor.capture()),
                () -> assertEquals("books", captor.getValue())
        );
    }

    @Test
    void test_doPost_when_button_equals_save_and_editedBook_is_null() throws IOException {
        when(req.getParameter("button")).thenReturn("save");
        when(req.getParameter("author1")).thenReturn("1");
        addEditBookController.setAddEditService(addEditBookService);
        addEditBookController.doPost(req, resp);
        verify(addEditBookService).save(any(Book.class));
    }

    @Test
    void test_doPost_when_editedBook_is_not_null_should_remove_edit_attribute() throws ServletException, IOException {
        when(req.getSession()).thenReturn(session);
        when(req.getParameter("button")).thenReturn("cancel");
        when(session.getAttribute("edit")).thenReturn(new Book());
        doNothing().when(resp).sendRedirect("login");
        addEditBookController.doGet(req, resp);
        addEditBookController.doPost(req, resp);
        verify(session).removeAttribute("edit");
    }
    @Test
    void test_doGet_when_userLogin_is_null() throws ServletException, IOException {
        when(req.getSession()).thenReturn(session);
        addEditBookController.doGet(req, resp);
        assertAll(
                () -> verify(resp).sendRedirect(captor.capture()),
                () -> assertEquals("login", captor.getValue())
        );
    }
    @Test
    void test_doGet_when_session_attribute_userLogin_is_not_null() throws ServletException, IOException {
        List<Author> list = new ArrayList<>();
        when(req.getSession()).thenReturn(session);
        when(session.getAttribute("userLogin")).thenReturn(new Login());
        RequestDispatcher rd = mock(RequestDispatcher.class);
        when(req.getRequestDispatcher(captor.capture())).thenReturn(rd);
        when(session.getAttribute("authors")).thenReturn(list);
        addEditBookController.doGet(req, resp);
        assertAll(
                () -> verify(req).setAttribute("authors", list),
                () -> verify(req).getRequestDispatcher(captor.capture()),
                () -> assertEquals("addEditBook.jsp", captor.getValue())
        );



    }
 */
}