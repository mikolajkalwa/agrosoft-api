package com.agrosoft.restAPI.controller;

import com.agrosoft.restAPI.exception.ResourceNotFoundException;
import com.agrosoft.restAPI.model.User;
import com.agrosoft.restAPI.repository.UserRepository;
import com.agrosoft.restAPI.security.CurrentUser;
import com.agrosoft.restAPI.security.UserPrincipal;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping({"/api/users"})
public class UserController {
    private UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping()
    public List findAll() {
        return userRepository.findAll();
    }

    @GetMapping(path = {"/me"})
    public ResponseEntity<UserPrincipal> getCurrentUser(@CurrentUser UserPrincipal currentUser) {
        return ResponseEntity.ok().body(currentUser);
    }

    @GetMapping(path = {"/{id}"})
    public ResponseEntity<User> findById(@PathVariable long id) {
        return userRepository.findById(id)
                .map(record -> ResponseEntity.ok().body(record))
                .orElseThrow(() -> new ResourceNotFoundException("User not found!"));
    }

    @PatchMapping(path = "/{id}")
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
                })
                .orElseThrow(() -> new ResourceNotFoundException("User not found!"));
    }

    @DeleteMapping(path = {"/{id}"})
    public ResponseEntity<?> delete(@PathVariable("id") long id) {
        return userRepository.findById(id)
                .map(record -> {
                    userRepository.deleteById(id);
                    return ResponseEntity.ok().build();
                })
                .orElseThrow(() -> new ResourceNotFoundException("User not found!"));
    }
}
