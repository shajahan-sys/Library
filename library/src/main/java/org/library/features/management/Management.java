package org.library.features.management;

import org.library.features.book.Book;
import org.library.features.reader.Reader;

import javax.persistence.*;

@Entity
@Table(name = "management")
public class Management {
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
}
