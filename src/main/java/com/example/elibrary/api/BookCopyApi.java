package com.example.elibrary.api;

import com.example.elibrary.dao.entity.Book;
import com.example.elibrary.dao.entity.Book_copy;
import com.example.elibrary.manager.BookCopyManager;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api")
public class BookCopyApi {

    private BookCopyManager bookCopyManager;

    public BookCopyApi(BookCopyManager bookCopyManager) {
        this.bookCopyManager = bookCopyManager;
    }

    @GetMapping("/copies/all")
    public Iterable<Book_copy> getAllBookCopies() {
        return bookCopyManager.findAll();
    }

    @GetMapping("/copies")
    @PreAuthorize("hasRole('ADMIN')")
    public Optional<Book_copy> getById(@RequestParam Long id) {
        return bookCopyManager.find(id);
    }

    @PostMapping("/copies/create")
    @PreAuthorize("hasRole('ADMIN')")
    public Book_copy addCopy(@RequestParam Long bookId) {return bookCopyManager.save(bookId);}

    @PutMapping("/copies/update")
    @PreAuthorize("hasRole('ADMIN')")
    public Book_copy updateCopy(@RequestBody Book_copy copy) {return bookCopyManager.save(copy);}

    @DeleteMapping("/copies/delete")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteCopy(@RequestParam Long id) {bookCopyManager.deleteById(id);}
}
