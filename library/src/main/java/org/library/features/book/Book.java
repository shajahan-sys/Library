package org.library.features.book;

import org.library.features.author.Author;
import org.library.features.login.Login;
import org.library.features.lend_book.Lending;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * POJO class that represents entity Book
 *
 * @author Barbara Grabowska
 * @version %I%
 */
@Entity
@Table(name = "books")
public class Book implements Serializable {
    private static final long serialVersionUID =12342L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotNull
    @Size(max = 45)
    @Column(name = "title")
    private String title;
    @NotNull
    @Size(max = 4)
    @Column(name = "publication_year")
    private String publicationYear;
    @NotNull
    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private Author author;
    @OneToOne(mappedBy = "book")
    private Lending lending;
    @NotNull
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Login login;

    public Book() {
    }

    public Book(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPublicationYear() {
        return publicationYear;
    }

    public void setPublicationYear(String publicationYear) {
        this.publicationYear = publicationYear;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public Login getLogin() {
        return login;
    }

    public void setLogin(Login login) {
        this.login = login;
    }

    public Lending getLending() {
        return lending;
    }

    public void setLending(Lending lending) {
        this.lending = lending;
    }
}
