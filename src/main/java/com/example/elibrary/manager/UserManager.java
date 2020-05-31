package com.example.elibrary.manager;

import com.example.elibrary.dao.BookRepo;
import com.example.elibrary.dao.UserRepo;
import com.example.elibrary.dao.entity.Book;
import com.example.elibrary.dao.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class UserManager {
    private UserRepo userRepo;

    public UserManager(){}

    @Autowired
    public UserManager(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    public User findByLogin(String login) {
        return userRepo.findByLogin(login);
    }

    public User save(User user) {
        return userRepo.save(user);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void fillDB() {
        save(new User(1L, "admin", "admin"));
        save(new User(2L, "test", "test"));
    }
}
