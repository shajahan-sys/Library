package com.library.features.lend_book;

import com.library.features.book.Book;
import com.library.features.reader.Reader;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "lending")
public class Lending implements Serializable {
    private static final long serialVersionUID = 12343L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotNull
    @OneToOne
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;
    @NotNull
    @Temporal(TemporalType.DATE)
    @Column(name = "return_date")
    private Date returnDate;
    @NotNull
    @ManyToOne
    @JoinColumn(name = "reader_id", nullable = false)
    private Reader reader;

    public Lending() {
    }

    public Lending(int id) {
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

    public Date getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }
}
