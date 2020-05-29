package com.example.elibrary.dao;

import com.example.elibrary.dao.entity.Author;
import com.example.elibrary.dao.entity.Book;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

@Component
public interface AuthorRepo extends CrudRepository<Author, Long> {
}
