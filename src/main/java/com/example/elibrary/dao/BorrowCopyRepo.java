package com.example.elibrary.dao;

import com.example.elibrary.dao.entity.Borrow_copy;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

@Component
public interface BorrowCopyRepo extends CrudRepository<Borrow_copy, Long> {
}
