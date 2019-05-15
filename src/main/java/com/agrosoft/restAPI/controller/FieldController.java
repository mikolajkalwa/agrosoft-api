package com.agrosoft.restAPI.controller;

import com.agrosoft.restAPI.exception.ResourceNotFoundException;
import com.agrosoft.restAPI.model.Field;
import com.agrosoft.restAPI.model.User;
import com.agrosoft.restAPI.payload.CreateFieldRequest;
import com.agrosoft.restAPI.repository.FieldRepository;
import com.agrosoft.restAPI.repository.UserRepository;
import com.agrosoft.restAPI.security.CurrentUser;
import com.agrosoft.restAPI.security.UserPrincipal;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping({"/api/fields"})
public class FieldController {
    private FieldRepository fieldRepository;
    private UserRepository userRepository;

    public FieldController(FieldRepository fieldRepository, UserRepository userRepository) {
        this.fieldRepository = fieldRepository;
        this.userRepository = userRepository;
    }

    @GetMapping()
    public List findAll(@CurrentUser UserPrincipal currentUser) {
        Long currentUserID = currentUser.getUser_id();
        return userRepository.findById(currentUserID)
                .map(user -> fieldRepository.findByFarm(user.getFarm()))
                .orElse(Collections.emptyList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Field> getField(@PathVariable Long id, @CurrentUser UserPrincipal currentUser) {
        List<Field> availableFields = findAll(currentUser);
        for (Field field : availableFields) {
            if (field.getField_id().equals(id)) {
                return ResponseEntity.ok().body(field);
            }
        }
        return ResponseEntity.notFound().build();
    }

    @PatchMapping(path = "/{id}")
    public ResponseEntity<Field> partialUpdate(@PathVariable("id") Long id, @CurrentUser UserPrincipal currentUser, @RequestBody Map<String, Object> updates) {
        List<Field> availableFields = findAll(currentUser);
        for (Field field : availableFields) {
            if (field.getField_id().equals(id)) {
                if (updates.containsKey("crop")) {
                    field.setCrop((String) updates.get("crop"));
                }
                if (updates.containsKey("status")) {
                    field.setStatus((String) updates.get("status"));
                }
                Field updated = fieldRepository.save(field);
                return ResponseEntity.ok().body(updated);
            }
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<?> createField(@Valid @RequestBody CreateFieldRequest createFieldRequest, @CurrentUser UserPrincipal currentUser) {
        User user = userRepository.findById(currentUser.getUser_id())
                .orElseThrow(() -> new ResourceNotFoundException("Current user not found " + currentUser.getUser_id()));
        Field field = new Field();
        field.setArea(createFieldRequest.getArea());
        field.setCrop(createFieldRequest.getCrop());
        field.setStatus(createFieldRequest.getStatus());
        field.setFarm(user.getFarm());

        Field toSave = fieldRepository.save(field);
        return ResponseEntity.ok().body(toSave);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMachine(@PathVariable Long id, @CurrentUser UserPrincipal currentUser) {

        List<Field> availableFields = findAll(currentUser);
        for (Field field : availableFields) {
            if (field.getField_id().equals(id)) {
                fieldRepository.deleteById(field.getField_id());
                return ResponseEntity.noContent().build();

            }
        }
        return ResponseEntity.status(403).build();
    }

}
