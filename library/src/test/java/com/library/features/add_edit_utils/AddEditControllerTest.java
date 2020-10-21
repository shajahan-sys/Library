package com.library.features.add_edit_utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.library.features.reader.Reader;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AddEditControllerTest {
    private AddEditController addEditController = mock(
            AddEditController.class,
            Mockito.CALLS_REAL_METHODS);
    @Mock
    private HttpSession session;
    @Mock
    private HttpServletRequest req;
    @Mock
    private HttpServletResponse resp;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(req.getSession()).thenReturn(session);
    }

    @Test
    void test_doPost_when_button_equals_save_and_edit_attribute_exists() throws IOException {
        when(req.getParameter("button")).thenReturn("save");
        when(session.getAttribute("edit")).thenReturn(new Reader());
        addEditController.doPost(req, resp);
        assertAll(
                () -> verify(session).setAttribute("saved", true),
                () -> verify(session).removeAttribute("edit"),
                () -> verify(resp).sendRedirect(any())
        );
    }

    @Test
    void test_doGet_when_edit_attribute_exists() throws ServletException, IOException {
        Reader reader = new Reader(1);
        when(session.getAttribute("edit")).thenReturn(reader);
        RequestDispatcher rd = Mockito.mock(RequestDispatcher.class);
        when(req.getRequestDispatcher(any())).thenReturn(rd);
        addEditController.doGet(req, resp);
        assertAll(
                () -> verify(req).setAttribute("edit", reader),
                () -> verify(req).getRequestDispatcher(any())
        );
    }
}