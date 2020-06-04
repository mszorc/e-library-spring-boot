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
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

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

    public Author findByName(String name, String surname) {
        Iterable<Author> authors = authorRepo.findAll();
        List<Author> filteredAuthors = StreamSupport.stream(authors.spliterator(), false)
                .filter(e -> e.getName().toLowerCase().equals(name.toLowerCase()) &&
                        e.getSurname().toLowerCase().equals(surname.toLowerCase()))
                .collect(Collectors.toList());
        Author author = new Author();
        if(filteredAuthors.size() == 0) {
            author = new Author();
            author.setName(name);
            author.setSurname(surname);
            save(author);
        } else {
            author = filteredAuthors.get(0);
        }
        return author;
    }
}
