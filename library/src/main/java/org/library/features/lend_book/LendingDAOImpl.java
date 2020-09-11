package org.library.features.lend_book;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.library.features.book.Book;
import org.library.features.login.Login;
import org.library.features.reader.Reader;

import java.util.List;

public class LendBookDAOImpl implements LendBookDAO {
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
    public List<Book> getAvailableBooksList(Login login) {
        List books;
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.getTransaction();
            transaction.begin();
            books = session.createQuery("from Book where user_id = :id")
                    .setParameter("id", login.getId())
                    .getResultList();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw e;
        }
        return books;
    }

    @Override
    public void save(LendBook lendBook) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.getTransaction();
            transaction.begin();
            if(lendBook != null){
                session.saveOrUpdate(lendBook);
            }
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
            throw e;
        }
    }
}
