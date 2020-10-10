package org.library.features.lend_book;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.library.features.book.Book;
import org.library.features.login.Login;
import org.library.features.reader.Reader;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class LendingServiceTest {
    private LendingService lendingService;
    private Login login;
    private List<Reader> readers;
    private List<Book> bookList;

    private LendingDAO lendingDAO;
    @Mock
    private LendingDAOImpl lendingDAOImpl;

    @BeforeEach
    void setUp() {
        lendingService = new LendingService();
        MockitoAnnotations.openMocks(this);
        lendingDAO = lendingDAOImpl;
        bookList = new ArrayList<>();
        readers = new ArrayList<>();
        login = new Login();
        lendingService.setLendingDAO(lendingDAO);
        when(lendingDAO.getReadersList(login)).thenReturn(readers);
        when(lendingDAO.getAvailableBooksList(login)).thenReturn(bookList);

    }

    @AfterEach
    void tearDown() {
        lendingService = null;
        login = null;
    }

    @Test
    void test_isDateFormatProper() {
        String properDate = "2020-09-09";
        String notValidDate = "09-09-2029";
        assertAll(
                () -> assertTrue(lendingService.isDateFormatProper(properDate)),
                () -> assertFalse(lendingService.isDateFormatProper(notValidDate)),
                () -> assertEquals("The specified date format is not valid. Use the yyyy-mm-dd format.", lendingService.getMessage())
        );
    }

    @Test
    void test_getReader() {
        Reader wantedReader = new Reader(3);
        Reader myReader = new Reader(1);
        readers.add(myReader);
        readers.add(wantedReader);
        lendingService.getReadersList(login);
        assertEquals(wantedReader, lendingService.getReader(login, 3));
    }

    @Test
    void test_getBook() {
        Book myBook = new Book(3);
        bookList.add(myBook);
        bookList.add(new Book(5));
        assertEquals(myBook, lendingService.getBook(login, 3));
    }

    @Test
    void test_saveLending() {
        Lending lending = new Lending(1);
        lendingService.saveLending(lending);
        verify(lendingDAO).save(lending);
    }

    @Test
    void test_getReadersList() {
        lendingService.getReadersList(login);
        verify(lendingDAO).getReadersList(login);
    }

    @Test
    void test_getAvailableBooksList() {
        bookList.add(new Book(1));
        bookList.add(new Book(2));
        Book book = new Book();
        book.setLending(new Lending());
        bookList.add(book);
        assertEquals(2, lendingService.getAvailableBooksList(login).size());
    }
}