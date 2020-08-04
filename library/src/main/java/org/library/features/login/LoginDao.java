package org.library.features.login;

public interface LoginDao {
    boolean check(String userName, String password);
    void save(Login login);
}
