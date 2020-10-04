package org.library.features.login;

import org.hibernate.SessionFactory;

public interface LoginDao {
    void saveNewAccount(Login login);
    boolean doesUserAlreadyExist(Login login);
    Login getLogin(Login login);
    void setSessionFactory(SessionFactory factory);
    String getHashedPassword(Login login);

}
