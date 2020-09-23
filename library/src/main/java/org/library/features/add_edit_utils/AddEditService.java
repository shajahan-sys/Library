package org.library.features.add_edit_utils;

public interface AddEditService<T> {
    void save(T object);
    void setAddEditDAO(AddEditDAO<T> addEditDAO);
}
