package org.library.features.welcome;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class WelcomeControllerTest {
    private WelcomeController welcomeController;
    @Mock
    private HttpServletRequest req;
    @Mock
    private HttpServletResponse resp;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        welcomeController = new WelcomeController();
    }

    @AfterEach
    void tearDown() {
        welcomeController = null;
    }

    @Test
    void shouldThrowExceptionWhenButtonValueDoesntAlreadyExistInSwitchCase() {
        when(req.getParameter("button")).thenReturn("wrong button name");
        assertThrows(IllegalArgumentException.class, () -> welcomeController.checkWhichButtonWasClicked(req, resp));
    }
    @Test
    void shouldntThrowWhenButtonValueIsProper(){
        when(req.getParameter("button")).thenReturn("Manage lending");
        assertDoesNotThrow(()->welcomeController.checkWhichButtonWasClicked(req, resp));
    }
    @Test
    void shouldRedirectToBooksIfButtonValueEqualsBooks() throws IOException {
        when(req.getParameter("button")).thenReturn("Books");
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        welcomeController.checkWhichButtonWasClicked(req, resp);
        verify(resp).sendRedirect(captor.capture());
        assertEquals("books", captor.getValue());
    }
}