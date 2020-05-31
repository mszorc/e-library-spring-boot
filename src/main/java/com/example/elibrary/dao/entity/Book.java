package com.example.elibrary.dao.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Entity
public class Book {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;
    private String title;
    public LocalDate publicationDate;

    public Book() {}

    public Book(Long id, String title, LocalDate publicationDate) {
        this.id = id;
        this.title = title;
        this.publicationDate = publicationDate;
    }

    @ManyToMany(mappedBy = "books")
    @JsonIgnoreProperties("books")
    private List<Author> authors;

    @OneToMany(mappedBy = "book")
    @JsonIgnoreProperties("book")
    private List<Book_copy> copies;

    public long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDate getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(LocalDate publicationDate) {
        this.publicationDate = publicationDate;
    }

    public List<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(List<Author> authors) {
        this.authors = authors;
    }

    public List<Book_copy> getCopies() {return copies;}

    public void setCopies(List<Book_copy> copier) {this.copies = copies;}
}
