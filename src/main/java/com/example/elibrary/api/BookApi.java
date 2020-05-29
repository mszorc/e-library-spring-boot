package com.example.elibrary.api;

import com.example.elibrary.dao.entity.Book;
import com.example.elibrary.manager.BookManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/books")
public class BookApi {

    private BookManager bookManager;

    @Autowired
    public BookApi(BookManager bookManager) {
        this.bookManager = bookManager;
    }

    @GetMapping("/all")
    public Iterable<Book> getAll() {
        return bookManager.findAll();
    }

    @GetMapping
    public Optional<Book> getById(@RequestParam Long index) {
        return bookManager.find(index);
    }

    @PostMapping
    public Book addBook(@RequestBody Book book) {
        return bookManager.save(book);
    }

    @PutMapping
    public Book updateBook(@RequestBody Book book) {
        return bookManager.save(book);
    }

    @DeleteMapping
    public void deleteBook(@RequestBody Long id) {
        bookManager.deleteById(id);
    }

}
