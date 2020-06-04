package com.example.elibrary.manager;

import com.example.elibrary.dao.BookRepo;
import com.example.elibrary.dao.RoleRepo;
import com.example.elibrary.dao.UserRepo;
import com.example.elibrary.dao.entity.Book;
import com.example.elibrary.dao.entity.Role;
import com.example.elibrary.dao.entity.User;
import com.example.elibrary.help.ERole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class UserManager {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private RoleRepo roleRepo;

    @Autowired
    PasswordEncoder passwordEncoder;

    public UserManager(){}

    @Autowired
    public UserManager(UserRepo userRepo, RoleRepo roleRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
        this.passwordEncoder = passwordEncoder;
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

    @EventListener(ApplicationReadyEvent.class)
    public void fillDB() {
        User user = new User("administrator", passwordEncoder.encode("admin123"));
        Set<Role> roles = new HashSet<>();
        Role adminRole = roleRepo.findByName(ERole.ROLE_ADMIN)
                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
        roles.add(adminRole);
        user.setRoles(roles);
        save(user);
    }

}