package com.example.elibrary.dao.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Borrow_copy {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    
}
