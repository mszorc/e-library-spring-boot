package com.example.elibrary.manager;

import com.example.elibrary.dao.BookRepo;
import com.example.elibrary.dao.entity.Author;
import com.example.elibrary.dao.entity.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class BookManager {

    private BookRepo bookRepo;

    public BookManager(){}

    @Autowired
    public BookManager(BookRepo bookRepo) {
        this.bookRepo = bookRepo;
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
}
