package org.library.features.login;

import org.hibernate.SessionFactory;
import org.library.hibernate_util.HibernateUtil;

public class LoginService implements Verifiable {
    private LoginDao loginDao;
    private String message;
    private SessionFactory sessionFactory;

    protected boolean loginToProperAccount(Login login) {
        initializeLoginDao();
        if (loginDao.doesUserAlreadyExist(login)) {
            return loginDao.isLoginInputCorrect(login);
        }
        return false;
    }

    protected boolean createNewAccount(Login login) {
        initializeLoginDao();
        if (!loginDao.doesUserAlreadyExist(login)) {
            loginDao.save(login);
            return true;
        }
        return false;
    }

    private void initializeLoginDao() {
        loginDao = new LoginDAOImpl();
        loginDao.setVerifier(this);
        if (sessionFactory != null) {
            loginDao.setSessionFactory(sessionFactory);
        } else {
            loginDao.setSessionFactory(HibernateUtil.getSessionFactory());
        }
    }

    protected void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    protected Login getLogin() {
        return loginDao.getLogin();
    }

    protected String getMessage() {
        return message;
    }

    @Override
    public void setMessage(String message) {
        this.message = message;
    }

}
