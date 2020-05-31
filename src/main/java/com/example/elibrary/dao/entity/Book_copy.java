package com.example.elibrary.dao.entity;

import javax.persistence.*;

@Entity
public class Book_copy {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @ManyToOne
    @JoinColumn(name="book_id", nullable = false)
    private Book book;

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
