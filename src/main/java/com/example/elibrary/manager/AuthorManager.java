package com.example.elibrary.manager;

import com.example.elibrary.dao.AuthorRepo;
import com.example.elibrary.dao.BookRepo;
import com.example.elibrary.dao.entity.Author;
import com.example.elibrary.dao.entity.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class AuthorManager {
    private AuthorRepo authorRepo;

    public AuthorManager(){}

    @Autowired
    public AuthorManager(AuthorRepo authorRepo) {
        this.authorRepo = authorRepo;
    }

    public Optional<Author> find(Long id) {
        return authorRepo.findById(id);
    }

    public Iterable<Author> findAll() {
        return authorRepo.findAll();
    }

    public Author save(Author author) {
        return authorRepo.save(author);
    }

    public void deleteById(Long id) {
        authorRepo.deleteById(id);
    }

    //Only for test, triggered when application starts
    @EventListener(ApplicationReadyEvent.class)
    public void fillDB() {
        save(new Author(1L, "Adam", "Mickiewicz", LocalDate.of(2015, 1, 1)));
        save(new Author(2L, "Henryk", "Sienkiewicz", LocalDate.of(2020, 1, 1)));
    }
}
