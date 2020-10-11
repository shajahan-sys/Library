package org.library.features.reader.add_edit;

import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Test;
import org.library.features.reader.Reader;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

class AddEditReaderServiceTest {
    @Test
    void test_save(){
        AddEditReaderDAO addEditReaderDAO = Mockito.mock(AddEditReaderDAOImpl.class);
        AddEditReaderService addEditReaderService = new AddEditReaderService();
        addEditReaderService.setAddEditDAO(addEditReaderDAO);
        Reader reader = new Reader();
        addEditReaderService.save(reader);
        assertAll(
                () -> verify(addEditReaderDAO).setSessionFactory(any(SessionFactory.class)),
                () -> verify(addEditReaderDAO).save(reader)
        );
    }

}