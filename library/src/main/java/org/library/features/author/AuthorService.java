package org.library.features.author;

import org.library.features.login.Login;
import org.library.hibernate_util.HibernateUtil;

import java.util.List;

public class AuthorService {
    private AuthorDAO authorDAO;
    private List<Author> authorList;
    private String message;

    private void initializeAuthorDAO() {
        if (authorDAO == null) {
            authorDAO = new AuthorDAOImpl();
        }
        authorDAO.setSessionFactory(HibernateUtil.getSessionFactory());
    }

    protected List<Author> getAuthorList(Login login) {
        initializeAuthorDAO();
        authorList = authorDAO.getAuthorsList(login);
        return authorList;
    }

    protected Author getAuthor(int id) {
        return authorList.stream().filter(author -> author.getId() == id).findAny().orElse(null);
    }

    protected boolean deleteIfPossible(int id) {
        Author author = getAuthor(id);
        if (author.getBooks().size() == 0) {
            authorDAO.delete(author);
            return true;
        } else {
            setMessage("Cannot delete selected author. There are some books assigned to this author, delete these books or change the author first.");
            return false;
        }
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
