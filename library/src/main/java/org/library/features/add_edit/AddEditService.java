package org.library.features.add_edit;

public interface AddEditService<T> {
    void save(T object);
    void setAddEditDAO(AddEditDAO<T> addEditDAO);
}
