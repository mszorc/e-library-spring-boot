package com.example.elibrary.manager;

import com.example.elibrary.dao.AuthorRepo;
import com.example.elibrary.dao.BookRepo;
import com.example.elibrary.dao.entity.Author;
import com.example.elibrary.dao.entity.Book;
import com.example.elibrary.dao.entity.Book_copy;
import com.example.elibrary.dao.entity.Borrow_copy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class BookManager {

    private BookRepo bookRepo;
    private AuthorManager authorManager;
    private BookCopyManager bookCopyManager;
    private BorrowCopyManager borrowCopyManager;

    public BookManager(){}

    @Autowired
    public BookManager(BookRepo bookRepo, AuthorManager authorManager, BookCopyManager bookCopyManager, BorrowCopyManager borrowCopyManager) {
        this.bookRepo = bookRepo;
        this.authorManager = authorManager;
        this.bookCopyManager = bookCopyManager;
        this.borrowCopyManager = borrowCopyManager;
    }

    public Optional<Book> find(Long id) {
        return bookRepo.findById(id);
    }

    public Iterable<Book> findAll() {
        return bookRepo.findAll();
    }

    public Book save(Book book) {
        return bookRepo.save(book);
    }

    public void deleteById(Long id) {
        bookRepo.deleteById(id);
    }

    public List<Book> findFilteredBooks(String title, String author, Long year) {
        Iterable<Book> books = bookRepo.findAll();
        List<Book> filteredBooks = (List<Book>) books;
        if (!title.isBlank())
            filteredBooks = StreamSupport.stream(books.spliterator(), false)
                    .filter(e -> e.getTitle().toLowerCase().contains(title.toLowerCase()))
                    .collect(Collectors.toList());

        if (!author.isBlank()) {
            for (Book book: books) {
                List<Author> authors = book.getAuthors().stream()
                        .filter(e -> e.getName().toLowerCase().contains(author.toLowerCase())
                                || e.getSurname().toLowerCase().contains(author.toLowerCase()))
                        .collect(Collectors.toList());

                if (authors.size() == 0)
                    filteredBooks.remove(book);
            }
        }

        if (year != null)
            filteredBooks = filteredBooks.stream().filter(e -> e.getPublicationDate().getYear() == year)
                .collect(Collectors.toList());

        return filteredBooks;
    }

    public Book addBook(Book book) {
        List<Author> authors_tmp = book.getAuthors();
        book.setAuthors(new ArrayList<Author>());
        book.setCopies(new ArrayList<Book_copy>());
        save(book);
        book = findFilteredBooks(book.getTitle(),"",null).get(0);
        List<Author> authors = new ArrayList<Author>();
        for(Author a : authors_tmp) {
            Author findAuthor = authorManager.findByName(a.getName(), a.getSurname());

            List<Book> bookList = findAuthor.getBooks();
            if(bookList == null) bookList = new ArrayList<Book>();
            bookList.add(book);
            findAuthor.setBooks(bookList);
            authorManager.save(findAuthor);
            authors.add(findAuthor);
        }
        Book_copy copy = new Book_copy();
        copy.setBook(book);
        bookCopyManager.save(copy);
        book.setCopies(Arrays.asList(copy));
        book.setAuthors(authors);
        return save(book);
    }

    public int getCopiesQuantity(Long id) {
        Optional<Book> book = bookRepo.findById(id);
        if (book.isPresent()) {
            return book.get().getCopies().size();
        }
        else {
            return 0;
        }
    }

    public int getNotCheckedOutCopiesQuantity(Long id) {
        Optional<Book> book = bookRepo.findById(id);
        if (book.isPresent()) {
            return borrowCopyManager.getBorrowedCopiesQuantity(book.get().getCopies());
        }
        else {
            return 0;
        }
    }
}
