package com.example.elibrary.dao;

import com.example.elibrary.dao.entity.Book_copy;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

@Component
public interface BookCopyRepo extends CrudRepository<Book_copy, Long> {
}
