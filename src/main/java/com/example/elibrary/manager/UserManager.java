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
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class UserManager {
    private UserRepo userRepo;

    public UserManager(){}

    @Autowired
    public UserManager(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    public Optional<User> findByUsername(String login) {
        return userRepo.findByUsername(login);
    }

    public User save(User user) {
        return userRepo.save(user);
    }

    public Iterable<User> findAll() {
        return userRepo.findAll();
    }

    public void deleteById(Long id) {
        userRepo.deleteById(id);
    }

    public List<User> findFilteredUsers(String username) {
        Iterable<User> users = userRepo.findAll();
        List<User> filteredUsers = StreamSupport.stream(users.spliterator(), false)
                .filter(e -> e.getUsername().toLowerCase().contains(username.toLowerCase()))
                .collect(Collectors.toList());

        return filteredUsers;
    }

}