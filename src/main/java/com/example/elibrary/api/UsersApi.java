package com.example.elibrary.api;

import com.example.elibrary.dao.entity.Book;
import com.example.elibrary.dao.entity.User;
import com.example.elibrary.manager.AuthorManager;
import com.example.elibrary.manager.BookCopyManager;
import com.example.elibrary.manager.BookManager;
import com.example.elibrary.manager.UserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/users")
@RestController
public class UsersApi {

    private UserManager userManager;

    public UsersApi() {}

    @Autowired
    public UsersApi(UserManager userManager) {
        this.userManager = userManager;
    }

    @CrossOrigin
    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public Iterable<User> getAllUsers() {
        return userManager.findAll();
    }

    @CrossOrigin
    @DeleteMapping("/delete")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteUser(@RequestParam Long id) {
        userManager.deleteById(id);
    }

    @CrossOrigin
    @GetMapping("/filter")
    @PreAuthorize("hasRole('ADMIN')")
    public List<User> getFilteredUsers(@RequestParam String username) {
        return userManager.findFilteredUsers(username);
    }
}