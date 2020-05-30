package com.example.elibrary.dao;

import com.example.elibrary.dao.entity.Book;
import com.example.elibrary.dao.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public interface UserRepo extends CrudRepository<User, Long> {
    User findByLogin(String login);
}
