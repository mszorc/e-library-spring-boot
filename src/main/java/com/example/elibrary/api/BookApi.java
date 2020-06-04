package com.example.elibrary.api;

import com.example.elibrary.auth.JwtUtils;
import com.example.elibrary.dao.RoleRepo;
import com.example.elibrary.dao.UserRepo;
import com.example.elibrary.dao.entity.Author;
//import com.example.elibrary.dao.entity.AuthorBook;
import com.example.elibrary.dao.entity.Book;
//import com.example.elibrary.manager.AuthorBookManager;
import com.example.elibrary.dao.entity.Book_copy;
import com.example.elibrary.manager.AuthorManager;
import com.example.elibrary.manager.BookCopyManager;
import com.example.elibrary.manager.BookManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class BookApi {
    private BookManager bookManager;
    private AuthorManager authorManager;
    private BookCopyManager bookCopyManager;

    public BookApi() {}

    @Autowired
    public BookApi(BookManager bookManager, AuthorManager authorManager, BookCopyManager bookCopyManager) {
        this.bookManager = bookManager;
        this.authorManager = authorManager;
        this.bookCopyManager = bookCopyManager;
    }

    @CrossOrigin
    @GetMapping("/books/all")
    public Iterable<Book> getAllBooks() {
        return bookManager.findAll();
    }

    @CrossOrigin
    @GetMapping("/books/filter")
    public Iterable<Book> getFilteredBooks(@RequestParam String title, @RequestParam String author, @RequestParam Long year) {
        return bookManager.findFilteredBooks(title, author, year);
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
    @PutMapping("/books/update")
    public Book updateBook(@RequestBody Book book) {
        return bookManager.save(book);
    }

    @CrossOrigin
    @DeleteMapping("/books/delete")
    public void deleteBook(@RequestParam Long id) {
        bookManager.deleteById(id);
    }

    @CrossOrigin
    @PostMapping("/books/create")
    @PreAuthorize("hasRole('ADMIN')")
    public Book addBook(@RequestBody Book book) {
        return bookManager.addBook(book);
    }

    @CrossOrigin
    @GetMapping("/books/quantity")
    public int getCopiesQuantity(@RequestParam Long id) {
        return bookManager.getCopiesQuantity(id);
    }

    @CrossOrigin
    @GetMapping("/books/notCheckedOutQuantity")
    public int getNotCheckedOutCopiesQuantity(@RequestParam Long id) {
        return bookManager.getNotCheckedOutCopiesQuantity(id);
    }
}
