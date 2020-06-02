package com.example.elibrary.api;

import com.example.elibrary.dao.entity.Book;
import com.example.elibrary.dao.entity.Book_copy;
import com.example.elibrary.manager.BookCopyManager;
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
    public Optional<Book_copy> getById(@RequestParam Long id) {
        return bookCopyManager.find(id);
    }

    @PostMapping("/copies/create")
    public Book_copy addCopy(@RequestParam Long bookId) {return bookCopyManager.save(bookId);}

    @PutMapping("/copies/update")
    public Book_copy updateCopy(@RequestBody Book_copy copy) {return bookCopyManager.save(copy);}

    @DeleteMapping("/copies/delete")
    public void deleteCopy(@RequestParam Long id) {bookCopyManager.deleteById(id);}
}
