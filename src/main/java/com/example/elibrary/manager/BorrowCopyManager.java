package com.example.elibrary.manager;

import com.example.elibrary.auth.JwtUtils;
import com.example.elibrary.dao.BookRepo;
import com.example.elibrary.dao.BorrowCopyRepo;
import com.example.elibrary.dao.UserRepo;
import com.example.elibrary.dao.entity.Book;
import com.example.elibrary.dao.entity.Book_copy;
import com.example.elibrary.dao.entity.Borrow_copy;
import com.example.elibrary.dao.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
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
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<Book> book = bookRepo.findById(bookId);
        if (book.isEmpty())
            return null;

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
                JwtUtils jwtUtils = new JwtUtils();
                Borrow_copy borrowCopy = new Borrow_copy();
                borrowCopy.setBookCopy(copy);
                borrowCopy.setBorrowDate(LocalDate.now());
                borrowCopy.setExpectedReturnDate(LocalDate.now().plusMonths(1));
                borrowCopy.setUsers(userRepo.findByUsername(userDetails.getUsername()).get());
                save(borrowCopy);
                return borrowCopy;
            }
        }

        return null;
    }

    public Borrow_copy returnCopy(Long id) {
        Optional<Borrow_copy> borrowCopy = findUsersBorrowedCopy(id);

        if (borrowCopy.isEmpty())
            return null;

        Borrow_copy borrowedCopy = borrowCopy.get();
        
        if (borrowedCopy.getReturnDate() != null)
            return null;

        borrowedCopy.setReturnDate(LocalDate.now());
        Period period = Period.between(borrowedCopy.getExpectedReturnDate(), borrowedCopy.getReturnDate());
        if (period.isNegative())
            borrowedCopy.setFine(0D);
        else
            borrowedCopy.setFine(period.getDays() * 0.10D);

        save(borrowedCopy);
        return borrowedCopy;
    }

    public Optional<Borrow_copy> findUsersBorrowedCopy(Long id) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<Borrow_copy> borrowCopy = borrowCopyRepo.findById(id);
        Optional<User> loggedUser = userRepo.findByUsername(userDetails.getUsername());

        if (borrowCopy.isEmpty() || loggedUser.isEmpty())
            return null;

        if (borrowCopy.get().getUsers().getUsername().equals(loggedUser.get().getUsername()))
            return borrowCopy;
        else
            return null;
    }

    public Iterable<Borrow_copy> findAll() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<User> loggedUser = userRepo.findByUsername(userDetails.getUsername());

        if (loggedUser.isEmpty())
            return null;

        Iterable<Borrow_copy> allBorrowedCopies = borrowCopyRepo.findAll();
        List<Borrow_copy> usersBorrowedCopies = new ArrayList<Borrow_copy>();
        for(Borrow_copy copy: allBorrowedCopies) {
            if (copy.getUsers().getUsername().toLowerCase().equals(loggedUser.get().getUsername().toLowerCase())
                    && copy.getReturnDate() == null)
                usersBorrowedCopies.add(copy);
        }
        return usersBorrowedCopies;
    }

    public int getBorrowedCopiesQuantity(List<Book_copy> copies) {
        Iterable<Borrow_copy> allBorrowed = borrowCopyRepo.findAll();
        int counter = 0;
        for (Book_copy copy: copies) {
            if (StreamSupport.stream(allBorrowed.spliterator(), false)
                    .filter(e -> e.getReturnDate() == null && e.getBookCopy().getId() == copy.getId())
                    .collect(Collectors.toList()).size() == 0) {
                counter++;
            }
        }
        return counter;
    }

    public Borrow_copy save(Borrow_copy borrowCopy) {
        return borrowCopyRepo.save(borrowCopy);
    }
}
