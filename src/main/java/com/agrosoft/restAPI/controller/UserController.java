package com.agrosoft.restAPI.controller;

import com.agrosoft.restAPI.model.User;
import com.agrosoft.restAPI.repository.UserRepository;
import com.agrosoft.restAPI.security.CurrentUser;
import com.agrosoft.restAPI.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping({"/api"})
public class UserController {
    private UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(path = {"/users"})
    public List findAll() {
        return userRepository.findAll();
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping(path = {"/user/me"})
    public ResponseEntity<UserPrincipal> getCurrentUser(@CurrentUser UserPrincipal currentUser) {
        return ResponseEntity.ok().body(currentUser);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping(path = {"/user/{id}"})
    public ResponseEntity<User> findById(@PathVariable long id) {
        return userRepository.findById(id)
                .map(record -> ResponseEntity.ok().body(record))
                .orElse(ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasRole('BOSS')")
    @PatchMapping(path = "/user/{id}")
    public ResponseEntity<User> partialUpdate(@PathVariable("id") long id, @RequestBody Map<String, Object> updates) {
        return userRepository.findById(id)
                .map(record -> {
                    if (updates.containsKey("first_name")) {
                        record.setFirst_name((String) updates.get("first_name"));
                    }
                    if (updates.containsKey("last_name")) {
                        record.setLast_name((String) updates.get(("last_name")));
                    }
                    if (updates.containsKey("username")) {
                        record.setUsername((String) updates.get("username"));
                    }
                    User updated = userRepository.save(record);
                    return ResponseEntity.ok().body(updated);
                }).orElse(ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasRole('BOSS')")
    @DeleteMapping(path = {"/user/{id}"})
    public ResponseEntity<?> delete(@PathVariable("id") long id) {
        return userRepository.findById(id)
                .map(record -> {
                    userRepository.deleteById(id);
                    return ResponseEntity.ok().build();
                }).orElse(ResponseEntity.notFound().build());
    }
}
