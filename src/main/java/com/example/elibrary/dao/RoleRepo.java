package com.example.elibrary.dao;

import com.example.elibrary.dao.entity.Role;
import org.springframework.data.repository.CrudRepository;

public interface RoleRepo extends CrudRepository<Role, Long> {
}
