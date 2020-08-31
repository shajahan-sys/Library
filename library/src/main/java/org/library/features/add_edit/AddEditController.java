package org.library.features.add_edit;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public abstract class AddEditController<T> extends HttpServlet {
    protected abstract void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException;
    protected abstract void doPost(HttpServletRequest req, HttpServletResponse resp) throws SecurityException, IOException;
    protected abstract void save(T object);
    protected abstract void setAddEditService(AddEditService<T> addEditService);

    }
