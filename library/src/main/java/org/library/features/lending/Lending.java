package org.library.features.lending;

import org.library.features.book.Book;
import org.library.features.login.Login;
import org.library.features.reader.Reader;

import javax.persistence.*;

@Entity
@Table(name = "lending")
public class Lending {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @OneToOne
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;
    @Column(name = "return_date")
    private String returnDate;
    @ManyToOne
    @JoinColumn(name = "reader_id", nullable = false)
    private Reader reader;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Login login;
    public Lending(){}
    public Lending(int id){
        this.id = id;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public void setReader(Reader reader) {
        this.reader = reader;
    }

    public Reader getReader() {
        return reader;
    }
    public String getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(String returnDate) {
        this.returnDate = returnDate;
    }

    public Login getLogin() {
        return login;
    }

    public void setLogin(Login login) {
        this.login = login;
    }
}
