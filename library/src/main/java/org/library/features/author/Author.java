package org.library.features.author;

import org.library.features.book.Book;
import org.library.features.login.Login;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Set;

/**
 * POJO class that represents entity Author
 *
 * @author Barbara Grabowska
 * @version %I%
 */
@Entity
@Table(name = "authors")
public class Author implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotNull
    @Size(max = 45)
    @Column(name = "surname")
    private String surname;
    @OneToMany(mappedBy = "author", fetch = FetchType.EAGER)
    private Set<Book> books;
    @NotNull
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Login login;

    public Author() {
    }

    public Author(int id) {
        this.id = id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public Set<Book> getBooks() {
        return books;
    }

    public void setBooks(Set<Book> books) {
        this.books = books;
    }

    public Login getLogin() {
        return login;
    }

    public void setLogin(Login login) {
        this.login = login;
    }
}
