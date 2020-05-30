package com.example.elibrary.manager;

import com.example.elibrary.dao.RoleRepo;
import com.example.elibrary.dao.UserRepo;
import com.example.elibrary.dao.entity.Book;
import com.example.elibrary.dao.entity.Role;
import com.example.elibrary.dao.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoleManager {
    public RoleRepo roleRepo;

    public RoleManager() {}

    @Autowired
    public RoleManager(RoleRepo roleRepo) {
        this.roleRepo = roleRepo;
    }

    public Optional<Role> find(Long id) {
        return roleRepo.findById(id);
    }

    public Iterable<Role> findAll() {
        return roleRepo.findAll();
    }

    public Role save(Role role) {
        return roleRepo.save(role);
    }

    public void deleteById(Long id) {
        roleRepo.deleteById(id);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void fillDB() {
        save(new Role(1L, "admin"));
        save(new Role(2L, "user"));
    }
}
