package com.example.elibrary.manager;

import com.example.elibrary.dao.BookRepo;
import com.example.elibrary.dao.BorrowCopyRepo;
import com.example.elibrary.dao.UserRepo;
import com.example.elibrary.dao.entity.Book;
import com.example.elibrary.dao.entity.Book_copy;
import com.example.elibrary.dao.entity.Borrow_copy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class BorrowCopyManager {

    private BorrowCopyRepo borrowCopyRepo;
    private BookRepo bookRepo;
    private UserRepo userRepo;

    @Autowired
    public BorrowCopyManager(BorrowCopyRepo borrowCopyRepo, BookRepo bookRepo, UserRepo userRepo) {
        this.borrowCopyRepo = borrowCopyRepo;
        this.bookRepo = bookRepo;
        this.userRepo = userRepo;
    }

    public Borrow_copy borrowFirstAvailable(Long bookId) {
        Optional<Book> book = bookRepo.findById(bookId);
        Iterable<Borrow_copy> borrowCopyList = borrowCopyRepo.findAll();
        List<Book_copy> bookCopies = book.get()
                .getCopies();
        List<Long> notReturnedList = StreamSupport.stream(borrowCopyList.spliterator(), false)
                .filter(e -> e.getReturnDate() == null && e.getBookCopy().getBook().getId() == bookId)
                .map(e -> e.getBookCopy().getId())
                .collect(Collectors.toList());

        for (Book_copy copy: bookCopies) {
            List<Long> x = notReturnedList.stream().filter(e -> e == copy.getId()).collect(Collectors.toList());
            if (x.size() == 0) {
                Borrow_copy borrowCopy = new Borrow_copy();
                borrowCopy.setBookCopy(copy);
                borrowCopy.setBorrowDate(LocalDate.now());
                borrowCopy.setExpectedReturnDate(LocalDate.now().plusMonths(1));
                //TODO: change to actual user
                borrowCopy.setUsers(userRepo.findByLogin("admin"));
                save(borrowCopy);
                return borrowCopy;
            }
        }

        return null;
    }

    public Borrow_copy returnCopy(Borrow_copy borrowCopy) {
        borrowCopy.setReturnDate(LocalDate.now());
        Period period = Period.between(borrowCopy.getExpectedReturnDate(), borrowCopy.getReturnDate());
        if (period.isNegative())
            borrowCopy.setFine(0D);
        else
            borrowCopy.setFine(period.getDays() * 0.10D);

        save(borrowCopy);
        return borrowCopy;
    }

    public Optional<Borrow_copy> find(Long id) {
        return borrowCopyRepo.findById(id);
    }

    public Iterable<Borrow_copy> findAll() {
        return borrowCopyRepo.findAll();
    }

    public Borrow_copy save(Borrow_copy borrowCopy) {
        return borrowCopyRepo.save(borrowCopy);
    }
}
