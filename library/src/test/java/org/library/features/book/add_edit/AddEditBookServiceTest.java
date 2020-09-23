package org.library.features.book.add_edit;

import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Test;
import org.library.features.book.Book;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

class AddEditBookServiceTest {

    @Test
    void test_save(){
        AddEditBookDAO addEditBookDAO = Mockito.mock(AddEditBookDAOImpl.class);
        AddEditBookService addEditBookService = new AddEditBookService();
        addEditBookService.setAddEditDAO(addEditBookDAO);
        Book book = new Book(1);
        addEditBookService.save(book);
        assertAll(
                () -> verify(addEditBookDAO).setSessionFactory(any(SessionFactory.class)),
                () -> verify(addEditBookDAO).save(book)
        );
    }

}