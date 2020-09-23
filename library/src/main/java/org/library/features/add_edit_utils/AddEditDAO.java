package org.library.features.add_edit_utils;

import org.hibernate.SessionFactory;

public interface AddEditDAO<T> {
    void setSessionFactory(SessionFactory sessionFactory);
    void save(T object);
}
