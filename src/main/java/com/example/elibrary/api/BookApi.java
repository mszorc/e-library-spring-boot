package com.example.elibrary.api;

import com.example.elibrary.dao.entity.Author;
//import com.example.elibrary.dao.entity.AuthorBook;
import com.example.elibrary.dao.entity.Book;
//import com.example.elibrary.manager.AuthorBookManager;
import com.example.elibrary.dao.entity.Book_copy;
import com.example.elibrary.manager.AuthorManager;
import com.example.elibrary.manager.BookCopyManager;
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
    public BookApi(BookManager bookManager, AuthorManager authorManager, BookCopyManager bookCopyManager) {
        this.bookManager = bookManager;
        this.authorManager = authorManager;
    }

    @CrossOrigin
    @GetMapping("/books/all")
    public Iterable<Book> getAllBooks() {
        return bookManager.findAll();
    }

    @CrossOrigin
    @GetMapping("/authors/all")
    public Iterable<Author> getAllAuthors() {
        return authorManager.findAll();
    }

    @CrossOrigin
    @GetMapping("/books")
    public Optional<Book> getById(@RequestParam Long id) {
        return bookManager.find(id);
    }

    @CrossOrigin
    @PostMapping("/books/create")
    public Author addBook(@RequestBody Author author) {
        return authorManager.save(author);
    }

    @CrossOrigin
    @PutMapping("/books/update")
    public Book updateBook(@RequestBody Book book) {
        return bookManager.save(book);
    }

    @CrossOrigin
    @DeleteMapping("/books/delete")
    public void deleteBook(@RequestParam Long id) {
        bookManager.deleteById(id);
    }
}
