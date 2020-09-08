package org.library.features.author;

import org.library.features.login.Login;
import org.library.hibernate_util.HibernateUtil;

import java.util.List;

public class AuthorService {
    private AuthorDAO authorDAO;
    private List<Author> authorList;

    private void initializeAuthorDAO() {
        if (authorDAO == null) {
            authorDAO = new AuthorDAOImpl();
        }
        authorDAO.setSessionFactory(HibernateUtil.getSessionFactory());
    }

    protected List<Author> getAuthorList(Login login){
        initializeAuthorDAO();
        authorList = authorDAO.getAuthorsList(login);
        return authorList;
    }
    protected Author getAuthor(int id){
        return authorList.stream().filter(author -> author.getId() == id).findAny().orElse(null);
    }
    protected void delete(Author author){
        authorDAO.delete(author);
    }
}
