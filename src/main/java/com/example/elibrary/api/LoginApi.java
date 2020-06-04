package com.example.elibrary.api;

import com.example.elibrary.auth.JwtUtils;
import com.example.elibrary.auth.UserDetailsImpl;
import com.example.elibrary.dao.RoleRepo;
import com.example.elibrary.dao.UserRepo;
import com.example.elibrary.dao.entity.Role;
import com.example.elibrary.dao.entity.User;
import com.example.elibrary.help.ERole;
import com.example.elibrary.manager.UserManager;
import com.example.elibrary.payload.request.LoginRequest;
import com.example.elibrary.payload.request.SignupRequest;
import com.example.elibrary.payload.response.JwtResponse;
import com.example.elibrary.payload.response.MessageResponse;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.util.matcher.PathPatternParserServerWebExchangeMatcher;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.swing.*;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.*;
import java.util.stream.Collectors;


@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/authentication")
@RestController
public class LoginApi {

    private UserManager userManager;

    @Autowired
    public LoginApi(UserManager userManager) {
        this.userManager = userManager;
    }

    @RequestMapping(value="/login", method = RequestMethod.POST)
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        return userManager.authenticateUser(loginRequest, response);
    }

    @RequestMapping(value="/login/google", method = RequestMethod.POST)
    public ResponseEntity<?> authenticateGoogleUser(@RequestParam String token, HttpServletResponse response) throws Exception {
        return userManager.authenticateGoogleUser(token, response);
    }
}
