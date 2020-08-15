package org.library.features.book;

import org.hibernate.Session;
import org.library.features.login.Login;
import org.library.hibernate_util.HibernateUtil;

import javax.persistence.Query;
import java.util.List;

public class BookDAOImpl implements BookDAO {
    @Override
    public void save(Book object) {

    }

    @Override
    public void update(Book object) {

    }

    @Override
    public List<Book> getAll(Login login) {
       Session session = HibernateUtil.getSessionFactory().openSession();
       session.getTransaction().begin();
        List<Book> books = session.createQuery("from Book where user_id = :id")
                .setParameter("id", login.getId())
                .getResultList();
        session.getTransaction().commit();
        session.close();
        return books;
    }

    @Override
    public Book getOne() {
        return null;
    }
}
