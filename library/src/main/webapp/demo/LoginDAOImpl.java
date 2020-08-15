package org.library.features.login;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.library.hibernate_util.HibernateUtil;

import javax.persistence.NoResultException;

public class LoginDAOImpl implements LoginDao{
    @Override
    public boolean check(String userName, String password) {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = sessionFactory.openSession();
        session.beginTransaction();
try {
    Login login = (Login) session.createQuery("from Login where user_name = :username " +
            "and password = :password", Login.class)
            .setParameter("username", userName)
            .setParameter("password", password)
            .getSingleResult();
} catch (NoResultException n){
    return false;
}
   // if (login.getUserName().equals(userName)&&login.getPassword().equals(password))
     //      return true;
    return true;
    }
    @Override
    public void save(Login login) {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.save(login);
        session.getTransaction().commit();
        session.close();
    }

}
