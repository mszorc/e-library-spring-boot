package com.example.elibrary.api;

import com.example.elibrary.dao.entity.Author;
import com.example.elibrary.dao.entity.Book;
import com.example.elibrary.dao.entity.Borrow_copy;
import com.example.elibrary.dao.entity.User;
import com.example.elibrary.manager.BookCopyManager;
import com.example.elibrary.manager.BorrowCopyManager;
import com.example.elibrary.manager.UserManager;
import org.apache.tomcat.jni.Local;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class BorrowCopyApi {

    private BorrowCopyManager borrowCopyManager;
    private UserManager userManager;

    @Autowired
    public BorrowCopyApi(BorrowCopyManager borrowCopyManager, UserManager userManager) {
        this.borrowCopyManager = borrowCopyManager;
        this.userManager = userManager;
    }

    @GetMapping("/borrowed/all")
    public Iterable<Borrow_copy> getAllBorrowedCopies() {
        return borrowCopyManager.findAll();
    }

    @GetMapping("/borrowed")
    public Optional<Borrow_copy> getBorrowedCopyById(@RequestParam Long id) {
        return borrowCopyManager.find(id);
    }

    @PostMapping("/borrowed")
    public Borrow_copy borrowCopy(@RequestParam Long bookId) {
        return borrowCopyManager.borrowFirstAvailable(bookId);
    }

    @PutMapping("/borrowed")
    public Borrow_copy returnCopy(@RequestBody Borrow_copy borrowCopy) {
        return borrowCopyManager.returnCopy(borrowCopy);
    }
}
