package org.library.features;

import org.hibernate.SessionFactory;
import org.library.features.login.Login;

import java.util.List;

public interface DAO<T> {
   void save(T object);
   void update(T object);
   List<T> getAll(Login login);
   T getOne();
}
