package org.library.features.add_edit;

import org.hibernate.SessionFactory;

public interface AddEditDAO<T> {
    void setSessionFactory(SessionFactory sessionFactory);
    void save(T object);
}
