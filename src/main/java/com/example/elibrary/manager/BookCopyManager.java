package com.example.elibrary.manager;

import com.example.elibrary.dao.BookCopyRepo;
import com.example.elibrary.dao.entity.Book;
import com.example.elibrary.dao.entity.Book_copy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BookCopyManager {

    private BookCopyRepo bookCopyRepo;

    public BookCopyManager() {}

    @Autowired
    public BookCopyManager(BookCopyRepo bookCopyRepo) {
        this.bookCopyRepo = bookCopyRepo;
    }

    public Optional<Book_copy> find(Long id) {
        return bookCopyRepo.findById(id);
    }

    public Iterable<Book_copy> findAll() {
        return bookCopyRepo.findAll();
    }

    public Book_copy save(Book_copy copy) {
        return bookCopyRepo.save(copy);
    }

    public void deleteById(Long id) {
        bookCopyRepo.deleteById(id);
    }
}
