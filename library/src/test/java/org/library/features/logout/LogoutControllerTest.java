package org.library.features.logout;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.library.features.login.Login;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LogoutControllerTest {
    @Mock
    private HttpServletRequest req;
    @Mock
    private HttpServletResponse resp;
    private LogoutController logoutController;
    private HttpSession session;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        req = mock(HttpServletRequest.class);
        session = mock(HttpSession.class);
        when(req.getSession()).thenReturn(session);
        logoutController = new LogoutController();
    }

    @AfterEach
    void tearDown() {
        logoutController = null;
    }


    @Test
    void tes() throws IOException {
        session.setAttribute("userLogin", new Login());
        logoutController.doPost(req, resp);
        assertNull(req.getSession().getAttribute("userLogin"));
    }
    @Test
    void tee() throws IOException {
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        logoutController.doPost(req, resp);
        verify(resp).sendRedirect(captor.capture());
        assertEquals("login", captor.getValue());
    }
}