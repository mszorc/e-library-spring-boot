package com.example.elibrary.manager;

import com.example.elibrary.dao.BookCopyRepo;
import com.example.elibrary.dao.BookRepo;
import com.example.elibrary.dao.entity.Book;
import com.example.elibrary.dao.entity.Book_copy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BookCopyManager {

    private BookCopyRepo bookCopyRepo;
    private BookRepo bookRepo;

    public BookCopyManager() {}

    @Autowired
    public BookCopyManager(BookCopyRepo bookCopyRepo, BookRepo bookRepo) {
        this.bookCopyRepo = bookCopyRepo;
        this.bookRepo = bookRepo;
    }

    public Optional<Book_copy> find(Long id) {
        return bookCopyRepo.findById(id);
    }

    public Iterable<Book_copy> findAll() {
        return bookCopyRepo.findAll();
    }

    public Book_copy save(Long bookId) {
        Optional<Book> book = bookRepo.findById(bookId);
        Book_copy bookCopy = new Book_copy();
        bookCopy.setBook(book.get());
        return bookCopyRepo.save(bookCopy);
    }

    public Book_copy save(Book_copy copy) {
        return bookCopyRepo.save(copy);
    }

    public void deleteById(Long id) {
        bookCopyRepo.deleteById(id);
    }
}
