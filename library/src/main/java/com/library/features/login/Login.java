package com.library.features.login;

import com.library.features.author.Author;
import com.library.features.reader.Reader;
import com.library.features.book.Book;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Set;

/**
 * POJO class to represent entity Login
 *
 * @author Barbara Grabowska
 * @version %I%
 */
@Entity
@Table(name = "login")
public class Login implements Serializable {
    private static final long serialVersionUID =12344L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotNull
    @Column(name = "user_name")
    private String userName;
    @NotNull
    @Column(name = "password")
    private String password;
    @OneToMany(mappedBy = "login")
    private Set<Book> books;
    @OneToMany(mappedBy = "login")
    private Set<Author> authors;
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
