package org.library.features.login;

import org.library.features.author.Author;
import org.library.features.book.Book;
import org.library.features.lending.Lending;
import org.library.features.reader.Reader;

import javax.persistence.*;
import java.util.Set;

/**
 * Login POJO class to represent entity Login
 *
 * @author Barbara Grabowska
 * @version %I%
 */
@Entity
@Table(name = "login")
public class Login {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "user_name")
    private String userName;
    @Column(name = "password")
    private String password;
    @OneToMany(mappedBy = "login")
    private Set<Book> books;
    @OneToMany(mappedBy = "login")
    private Set<Author> authors;
    @OneToMany(mappedBy = "login")
    private Set<Lending> lendings;
    @OneToMany(mappedBy = "login")
    private Set<Reader> readers;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
