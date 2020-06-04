package com.example.elibrary.dao.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.ManyToAny;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
public class Borrow_copy {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="copy_id", nullable = false)
    private Book_copy bookCopy;

    @ManyToOne
    @JoinColumn(name="user_id", nullable = false)
    private User users;

    private LocalDate borrowDate;
    private LocalDate expectedReturnDate;
    private LocalDate returnDate;
    private Double fine;

    public Borrow_copy() {

    }

    public Borrow_copy(Long id, Book_copy bookCopy, User users, LocalDate borrowDate, LocalDate expectedReturnDate, LocalDate returnDate, Double fine) {
        this.id = id;
        this.bookCopy = bookCopy;
        this.users = users;
        this.borrowDate = borrowDate;
        this.expectedReturnDate = expectedReturnDate;
        this.returnDate = returnDate;
        this.fine = fine;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Book_copy getBookCopy() {
        return bookCopy;
    }

    public void setBookCopy(Book_copy bookCopy) {
        this.bookCopy = bookCopy;
    }

    public User getUsers() {
        return users;
    }

    public void setUsers(User users) {
        this.users = users;
    }

    public LocalDate getBorrowDate() {
        return borrowDate;
    }

    public void setBorrowDate(LocalDate borrowDate) {
        this.borrowDate = borrowDate;
    }

    public LocalDate getExpectedReturnDate() {
        return expectedReturnDate;
    }

    public void setExpectedReturnDate(LocalDate expectedReturnDate) {
        this.expectedReturnDate = expectedReturnDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public Double getFine() {
        return fine;
    }

    public void setFine(Double fine) {
        this.fine = fine;
    }
}
