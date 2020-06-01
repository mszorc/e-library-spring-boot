package com.example.elibrary.api;

import com.example.elibrary.dao.entity.User;
import com.example.elibrary.manager.AuthorManager;
import com.example.elibrary.manager.BookManager;
import com.example.elibrary.manager.UserManager;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.Optional;

@RestController
public class LoginApi {

    private UserManager userManager;

    @Autowired
    public LoginApi(UserManager userManager) {
        this.userManager = userManager;
    }

    @PostMapping("/login")
    public String login(@RequestBody User user) {

        Long currentTimeMillis = System.currentTimeMillis();
        User user_check = userManager.findByLogin(user.getLogin());
        if(user_check.getPasswd().equals(user.getPasswd())) {
            return Jwts.builder()
                    .setSubject(user.getLogin())
                    .claim("roles", "user")
                    .setIssuedAt(new Date(currentTimeMillis))
                    .setExpiration(new Date(currentTimeMillis + 900000))
                    .signWith(SignatureAlgorithm.HS512, "`c?kBe{,/=3OCNf")
                    .compact();
        }
        return "Error";
    }
}
