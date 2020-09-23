package org.library.features.reader;

import org.library.features.login.Login;
import org.library.features.lend_book.Lending;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name = "readers")
public class Reader implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotNull
    @Size(max = 45)
    @Column(name = "name")
    private String name;
    @NotNull
    @Size(max = 45)
    @Column(name = "surname")
    private String surname;
    @OneToMany(mappedBy = "reader", fetch = FetchType.EAGER)
    private Set<Lending> lendings;
    @NotNull
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Login login;

    public Reader() {
    }

    public Reader(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public Login getLogin() {
        return login;
    }

    public void setLogin(Login login) {
        this.login = login;
    }

    public Set<Lending> getLendings() {
        return lendings;
    }

    public void setLendings(Set<Lending> lendings) {
        this.lendings = lendings;
    }
}
