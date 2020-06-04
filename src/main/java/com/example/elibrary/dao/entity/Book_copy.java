package com.example.elibrary.dao.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.List;

@Entity
public class Book_copy {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @ManyToOne
    @JoinColumn(name="book_id")
    private Book book;

    @OneToMany(mappedBy = "bookCopy", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("bookCopy")
    private List<Borrow_copy> copies;

    public Book_copy() {}

    public Book_copy(Long id, Book book) {
        this.id = id;
        this.book = book;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }
}
