package com.agrosoft.restAPI.controller;

import com.agrosoft.restAPI.model.User;
import com.agrosoft.restAPI.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping({"/users"})
public class UserController {
    private UserRepository repository;

    public UserController(UserRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List findAll() {
        return repository.findAll();
    }

    @GetMapping(path = {"/{id}"})
    public ResponseEntity<User> findById(@PathVariable long id) {
        return repository.findById(id)
                .map(record -> ResponseEntity.ok().body(record))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public User create(@RequestBody User user) {
        return repository.save(user);
    }

    @PatchMapping(value = "/{id}")
    public ResponseEntity<User> partialUpdate(@PathVariable("id") long id, @RequestBody Map<String, Object> updates) {
        return repository.findById(id)
                .map(record -> {
                    if (updates.containsKey("first_name")) {
                        record.setFirst_name((String) updates.get("first_name"));
                    }
                    if (updates.containsKey("surname")) {
                        record.setSurname((String) updates.get(("surname")));
                    }
                    if (updates.containsKey("email")) {
                        record.setEmail((String) updates.get("email"));
                    }
                    if (updates.containsKey("password_hash")) {
                        record.setPassword_hash((String) updates.get("password_hash"));
                    }
                    User updated = repository.save(record);
                    return ResponseEntity.ok().body(updated);
                }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping(path = {"/{id}"})
    public ResponseEntity<?> delete(@PathVariable("id") long id) {
        return repository.findById(id)
                .map(record -> {
                    repository.deleteById(id);
                    return ResponseEntity.ok().build();
                }).orElse(ResponseEntity.notFound().build());
    }
}