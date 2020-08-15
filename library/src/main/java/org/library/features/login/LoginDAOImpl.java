package org.library.features.login;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.mindrot.jbcrypt.BCrypt;

import javax.persistence.NoResultException;
import javax.persistence.Query;

public class LoginDAOImpl implements LoginDao {
    private Login login;
    private String hashedPassword;
    private Verifiable verifiable;
    private SessionFactory sessionFactory;

    @Override
    public void setSessionFactory(SessionFactory sessionFactory){
        this.sessionFactory = sessionFactory;
    }

    protected String hashPassword(String plainTextPassword) {
        return BCrypt.hashpw(plainTextPassword, BCrypt.gensalt());
    }

    protected void setProperLoginId(Login login){
        Session session = sessionFactory.openSession();
        session.getTransaction().begin();
        Query userId = session.createQuery("select id from Login where user_name = :username")
                .setParameter("username", login.getUserName());
        login.setId((Integer) userId.getSingleResult());
        session.getTransaction().commit();
        session.close();
    }
    @Override
    public boolean isLoginInputCorrect(Login login) {
        if (BCrypt.checkpw(login.getPassword(), hashedPassword)) {
            setProperLoginId(login);
            return true;
        } else {
            setMessageInVerifiable("Wrong password!");
            return false;
        }
    }

    @Override
    public void save(Login login) {
        login.setPassword(hashPassword(login.getPassword()));
        Session session = sessionFactory.openSession();
        session.getTransaction().begin();
        session.save(login);
        session.getTransaction().commit();
        session.close();
    }

    @Override
    public boolean doesUserAlreadyExist(Login login) {
        this.login = login;
        try (Session session = sessionFactory.openSession()) {
            session.getTransaction().begin();
            Query getHashPw = session.createQuery("select password from Login where user_name = :username")
                    .setParameter("username", login.getUserName());
            hashedPassword = (String) getHashPw.getSingleResult();
            session.getTransaction().commit();
            session.close();
            setMessageInVerifiable("User already exist, try different login or login instead of creating new account");
        } catch (NoResultException n) {
            setMessageInVerifiable("There is no such a user");
            return false;
        }
        return true;
    }

    @Override
    public void setVerifier(Verifiable verifiable) {
        this.verifiable = verifiable;
    }

    protected void setMessageInVerifiable(String message) {
        verifiable.setMessage(message);
    }

    @Override
    public Login getLogin() {
        return login;
    }
}
