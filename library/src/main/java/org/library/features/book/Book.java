package org.library.features.book;

import org.library.features.author.Author;
import org.library.features.login.Login;
import org.library.features.lend_book.Lending;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "books")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "title")
    private String title;
    @Column(name = "publication_year")
    private String publicationYear;
    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private Author author;
    @OneToOne(mappedBy = "book")
    private Lending lending;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Login login;

    public Book() {
    }

    public Book(int id) {
        this.id = id;
        List l = new ArrayList();
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
