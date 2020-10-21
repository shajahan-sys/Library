package com.library.features.return_book;

import com.library.features.lend_book.Lending;
import com.library.features.reader.Reader;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.library.features.login.Login;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ReturnBookServiceTest {
    private ReturnBookService returnBookService;
    private ReturnBookDAO returnBookDAO;
    private Login login;
    private List<Reader> readers;
    @Mock
    private ReturnBookDAOImpl returnBookDAOImpl;

    @BeforeEach
    void setUp() {
        returnBookService = new ReturnBookService();
        MockitoAnnotations.openMocks(this);
        login = new Login();
        returnBookDAO = returnBookDAOImpl;
        returnBookService.setReturnBookDAO(returnBookDAO);
    }

    @AfterEach
    void tearDown() {
        returnBookService = null;
        returnBookDAO = null;
    }

    @Test
    void test_deleteLending() {
        returnBookService.deleteLending(1);
        verify(returnBookDAO).delete(any(Lending.class));
    }

    @Test
    void test_getReader() {
       createProperListOfReaders();
        when(returnBookDAO.getActiveReadersList(login)).thenReturn(readers);
        Assertions.assertEquals(returnBookService.getReader(login, 1), readers.get(0));
    }

    private void createProperListOfReaders() {
        readers = new ArrayList<>();
        Set<Lending> lendingSet = new HashSet<>();
        lendingSet.add(new Lending(1));
        Reader reader1 = new Reader(1);
        Reader reader2 = new Reader(2);
        Reader reader3 = new Reader();
        reader1.setLendings(lendingSet);
        reader2.setLendings(lendingSet);
        reader3.setLendings(new HashSet<>());
        readers.add(reader1);
        readers.add(reader2);
        readers.add(reader3);
    }
}