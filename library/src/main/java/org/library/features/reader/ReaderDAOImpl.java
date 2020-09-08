package org.library.features.reader;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.library.features.login.Login;

import java.util.List;

public class ReaderDAOImpl implements ReaderDAO{
    private SessionFactory sessionFactory;
    @Override
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Reader> getReadersList(Login login) {
        List readers;
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.getTransaction();
            transaction.begin();
            readers = session.createQuery("from Reader where user_id = :id")
                    .setParameter("id", login.getId())
                    .getResultList();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw e;
        }
        return readers;
    }

    @Override
    public void delete(Reader reader) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.getTransaction();
            transaction.begin();
            reader = session.get(Reader.class, reader.getId());
            if (reader != null) {
                session.delete(reader);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw e;
        }
    }

}
