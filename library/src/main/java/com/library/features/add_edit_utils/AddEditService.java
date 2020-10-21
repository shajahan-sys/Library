package com.library.features.add_edit_utils;

/**
 * Service interface that enables Controller class
 * to save T objects using AddEditDAO instance.
 *
 * @param <T> represents a model class
 * @author Barbara Grabowska
 * @version %I%, %G%
 */
public interface AddEditService<T> {
    /**
     * @param object to save
     */
    void save(T object);

    /**
     * @param addEditDAO to set
     */
    void setAddEditDAO(AddEditDAO<T> addEditDAO);
}
