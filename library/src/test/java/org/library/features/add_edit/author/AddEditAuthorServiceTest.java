package org.library.features.add_edit.author;

import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Test;
import org.library.features.add_edit.author.AddEditAuthorDAO;
import org.library.features.add_edit.author.AddEditAuthorDAOImpl;
import org.library.features.add_edit.author.AddEditAuthorService;
import org.library.features.author.Author;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

class AddEditAuthorServiceTest {
    @Test
    void test_save(){
        AddEditAuthorDAO addEditBookDAO = Mockito.mock(AddEditAuthorDAOImpl.class);
        AddEditAuthorService addEditAuthorService = new AddEditAuthorService();
        addEditAuthorService.setAddEditDAO(addEditBookDAO);
        Author author = new Author(1);
        addEditAuthorService.save(author);
        assertAll(
                () -> verify(addEditBookDAO).setSessionFactory(any(SessionFactory.class)),
                () -> verify(addEditBookDAO).save(author)
        );
    }

}