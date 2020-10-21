package com.library.features.return_book;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import com.library.features.lend_book.Lending;
import com.library.features.login.Login;
import com.library.features.reader.Reader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * ReturnBookDAO implementation.
 *
 * @author Barbara Grabowska
 * @version %I%, %G%
 */
public class ReturnBookDAOImpl implements ReturnBookDAO {
    private SessionFactory sessionFactory;
    /**
     * Logger instance for this class
     */
    private final Logger logger = LoggerFactory.getLogger(ReturnBookDAOImpl.class);


    @Override
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Reader> getActiveReadersList(Login login) {
        Session session = sessionFactory.openSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Reader> cr = cb.createQuery(Reader.class);
        Root<Reader> root = cr.from(Reader.class);
        Predicate[] predicates = new Predicate[2];
        predicates[0] = cb.equal(root.get("login"), login.getId());
        predicates[1] = cb.isNotEmpty(root.get("lendings"));
        cr.select(root).where(predicates);
        Query<Reader> query = session.createQuery(cr);
        return query.getResultList();
    }

    @Override
    public void delete(Lending lending) {
        Session session = sessionFactory.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.delete(lending);
            tx.commit();
        } catch (HibernateException e) {
            logger.error(e.getMessage());
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }
}
