package com.example.elibrary.dao;

import com.example.elibrary.dao.entity.Role;
import com.example.elibrary.help.ERole;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RoleRepo extends CrudRepository<Role, Long> {
    Optional<Role> findByName(ERole role);
}
