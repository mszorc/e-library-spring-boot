package com.example.elibrary.dao.entity;

import javax.persistence.*;

@Entity
@Table(name = "User_Entity")
public class User {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;
    private String login;
    private String passwd;

    public User() {}

    public User(Long id, String login, String passwd) {
        this.id = id;
        this.login = login;
        this.passwd = passwd;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }
}
