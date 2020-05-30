package com.example.elibrary.api;

import com.example.elibrary.dao.entity.Author;
//import com.example.elibrary.dao.entity.AuthorBook;
import com.example.elibrary.dao.entity.Book;
//import com.example.elibrary.manager.AuthorBookManager;
import com.example.elibrary.manager.AuthorManager;
import com.example.elibrary.manager.BookManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api")
public class BookApi {

    private BookManager bookManager;
    private AuthorManager authorManager;

    public BookApi() {}

    @Autowired
    public BookApi(BookManager bookManager, AuthorManager authorManager) {
        this.bookManager = bookManager;
        this.authorManager = authorManager;
    }

    @GetMapping("/books/all")
    public Iterable<Book> getAllBooks() {
        return bookManager.findAll();
    }

    @GetMapping("/authors/all")
    public Iterable<Author> getAllAuthors() {
        return authorManager.findAll();
    }

    @GetMapping
    public Optional<Book> getById(@RequestParam Long id) {
        return bookManager.find(id);
    }

    @PostMapping
    public Author addBook(@RequestBody Author author) {
        return authorManager.save(author);
    }

    @PutMapping
    public Book updateBook(@RequestBody Book book) {
        return bookManager.save(book);
    }

    @DeleteMapping
    public void deleteBook(@RequestParam Long id) {
        bookManager.deleteById(id);
    }

}
