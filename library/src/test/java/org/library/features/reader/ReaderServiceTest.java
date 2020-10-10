package org.library.features.reader;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.library.features.lend_book.Lending;
import org.library.features.login.Login;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ReaderServiceTest {
    private ReaderService readerService;
    private ReaderDAO readerDAO;
    private Login login;
    private List<Reader> readers;
    private Set<Lending> lendingSet;
    private Reader myReader;
    @Mock
    private ReaderDAOImpl readerDAOImpl;

    @BeforeEach
    void setUp() {
        readerService = new ReaderService();
        MockitoAnnotations.openMocks(this);
        readers = new ArrayList<>();
        myReader = new Reader(2);
        login = new Login();
        readerDAO = readerDAOImpl;
        readerService.setReaderDAO(readerDAOImpl);
        when(readerDAO.getReadersList(login)).thenReturn(readers);
    }

    @AfterEach
    void tearDown() {
        readerService = null;
        readers = null;
        myReader = null;
        readerDAO = null;
    }

    @Test
    void test_getReadersList() {
        assertEquals(readers, readerService.getReadersList(login));
    }

   @Test
    void test_getReader() {
        Reader wantedReader = new Reader(3);
        readers.add(myReader);
        readers.add(wantedReader);
        readerService.getReadersList(login);
        assertEquals(wantedReader, readerService.getReader(login, 3));
    }

    @Test
    void test_deleteIfPossible_when_lending_set_is_empty() {
        myReader.setLendings(new HashSet<>());
        readers.add(myReader);
        readerService.getReadersList(login);
        assertAll(
                () -> assertTrue(readerService.deleteIfPossible(login, 2)),
                () -> verify(readerDAO).delete(myReader)
        );
    }

    @Test
    void test_deleteIfPossible_when_lending_set_is_not_empty() {
        lendingSet = new HashSet<>();
        lendingSet.add(new Lending());
        myReader.setLendings(lendingSet);
        String message = "Cannot delete selected reader. The reader has not returned borrowed books yet.";
        readers.add(myReader);
        readerService.getReadersList(login);
        assertAll(
                () -> assertFalse(readerService.deleteIfPossible(login, 2)),
                () -> assertEquals(message, readerService.getMessage())
        );
    }

    @Test
    void test_isReaderLendingSetEmpty_when_set_is_empty() {
        myReader.setLendings(new HashSet<>());
        readers.add(myReader);
        readerService.getReadersList(login);
        String message = "Selected reader does not have any books to return.";
        assertAll(
                () -> assertTrue(readerService.isReaderLendingSetEmpty(login, 2)),
                () -> assertEquals(message, readerService.getMessage())
        );
    }

    @Test
    void test_isReaderLendingSetEmpty_when_set_is_not_empty() {
        lendingSet = new HashSet<>();
        lendingSet.add(new Lending());
        myReader.setLendings(lendingSet);
        readers.add(myReader);
        readerService.getReadersList(login);
        assertFalse(readerService.isReaderLendingSetEmpty(login, 2));
    }

}