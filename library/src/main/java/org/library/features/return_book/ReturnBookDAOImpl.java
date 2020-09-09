package org.library.features.return_book;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.library.features.lending.Lending;
import org.library.features.login.Login;
import org.library.features.reader.Reader;

import java.util.List;

public class ReturnBookDAOImpl implements ReturnBookDAO {
    private SessionFactory sessionFactory;

    @Override
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List getReadersList(Login login) {
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
    public void delete(Lending lending) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            lending = session.get(Lending.class, lending.getId());
            if (lending != null) {
                session.delete(lending);
            }
            transaction.commit();
        } catch (RuntimeException e) {
            try {
                if (transaction != null)
                    transaction.rollback();
            } catch (HibernateException e1) {
                //  log.error("Transaction roleback not succesful");
            }
            throw e;
        }
    }
}
