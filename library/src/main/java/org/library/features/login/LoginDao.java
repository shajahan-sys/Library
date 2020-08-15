package org.library.features.login;

import org.hibernate.SessionFactory;

public interface LoginDao {
    boolean isLoginInputCorrect(Login login);
    void save(Login login);
    boolean doesUserAlreadyExist(Login login);
    Login getLogin();
    void setVerifier(Verifiable verifiable);
    void setSessionFactory(SessionFactory factory);

}
