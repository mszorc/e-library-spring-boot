package com.example.elibrary.api;

import com.example.elibrary.auth.JwtUtils;
import com.example.elibrary.auth.UserDetailsImpl;
import com.example.elibrary.dao.RoleRepo;
import com.example.elibrary.dao.UserRepo;
import com.example.elibrary.dao.entity.Role;
import com.example.elibrary.dao.entity.User;
import com.example.elibrary.help.ERole;
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
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepo userRepo;

    @Autowired
    RoleRepo roleRepo;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtUtils jwtUtils;

    public LoginApi(AuthenticationManager authenticationManager, UserRepo userRepo, RoleRepo roleRepo, PasswordEncoder passwordEncoder, JwtUtils jwtUtils) {
        this.authenticationManager = authenticationManager;
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
    }

    @RequestMapping(value="/login", method = RequestMethod.POST)
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        Optional<User> user = userRepo.findByUsername(loginRequest.getUsername());
        if (user.isPresent()) {
            if (user.get().isGoogleUser())
                return null;
        }

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        Cookie cookie = new Cookie("token",jwt);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(900);
        cookie.setPath("/");

        response.addCookie(cookie);

        return new ResponseEntity<JwtResponse>(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                roles), HttpStatus.OK);
    }

    @RequestMapping(value="/login/google", method = RequestMethod.POST)
    public ResponseEntity<?> authenticateGoogleUser(@RequestParam String token, HttpServletResponse response) throws Exception {

        String secret = "~5Wrm?D%O`Y{{#i";
        String googleClientId = "438315145104-qrkfsoqg5gdavq23m3sh7sq66gg178gh.apps.googleusercontent.com";
        HttpTransport transport = new NetHttpTransport();
        JsonFactory jsonFactory = new JacksonFactory();
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
                .setAudience(Collections.singletonList(googleClientId))
                .build();

        try {
            GoogleIdToken idToken = verifier.verify(token);

            if (idToken != null) {
                GoogleIdToken.Payload payload = idToken.getPayload();

                String userId = payload.getSubject();

                // Get profile information from payload
                String email = payload.getEmail();
                if (payload.getEmailVerified()) {
                    Optional<User> user = userRepo.findByUsername(payload.getEmail());
                    User newUser;
                    if (user.isEmpty()) {
                        newUser = new User();
                        Set<Role> roles = new HashSet<Role>();
                        Role userRole = roleRepo.findByName(ERole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);

                        newUser.setUsername(email);
                        newUser.setRoles(roles);
                        newUser.setPassword(passwordEncoder.encode(secret));
                        newUser.setGoogleUser(true);
                        userRepo.save(newUser);
                    }
                    else {
                        newUser = user.get();
                        if (!newUser.isGoogleUser()) {
                            throw new UsernameNotFoundException("Not a google account.");
                        }
                    }

                    Authentication authentication = authenticationManager.authenticate(
                            new UsernamePasswordAuthenticationToken(newUser.getUsername(),secret));

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    String jwt = jwtUtils.generateJwtToken(authentication);

                    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
                    List<String> roles = userDetails.getAuthorities().stream()
                            .map(item -> item.getAuthority())
                            .collect(Collectors.toList());

                    Cookie cookie = new Cookie("token",jwt);
                    cookie.setHttpOnly(true);
                    cookie.setMaxAge(900);
                    cookie.setPath("/");

                    response.addCookie(cookie);

                    return new ResponseEntity<JwtResponse>(new JwtResponse(jwt,
                            userDetails.getId(),
                            userDetails.getUsername(),
                            roles), HttpStatus.OK);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        throw new Exception("Token is invalid or has already expired.");
    }

    @CrossOrigin
    @PostMapping("/signup")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> registerUser(@RequestBody SignupRequest signUpRequest) {
        if (userRepo.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }
        User user = new User(signUpRequest.getUsername(),
                passwordEncoder.encode(signUpRequest.getPassword()));

        Set<String> strRoles = signUpRequest.getRoles();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepo.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepo.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);

                        break;
                    default:
                        Role userRole = roleRepo.findByName(ERole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                }
            });
        }

        user.setRoles(roles);
        userRepo.save(user);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }
}
