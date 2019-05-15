package com.agrosoft.restAPI.controller;


import com.agrosoft.restAPI.exception.ResourceNotFoundException;
import com.agrosoft.restAPI.model.Silo;
import com.agrosoft.restAPI.model.User;
import com.agrosoft.restAPI.payload.CreateSiloRequest;
import com.agrosoft.restAPI.repository.SiloRepository;
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
@RequestMapping({"/api/silos"})
public class SiloController {
    private SiloRepository siloRepository;
    private UserRepository userRepository;

    public SiloController(SiloRepository siloRepository, UserRepository userRepository) {
        this.siloRepository = siloRepository;
        this.userRepository = userRepository;
    }

    @GetMapping()
    public List findAll(@CurrentUser UserPrincipal currentUser) {
        Long currentUserID = currentUser.getUser_id();
        return userRepository.findById(currentUserID)
                .map(user -> siloRepository.findByFarm(user.getFarm()))
                .orElse(Collections.emptyList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Silo> getSilo(@PathVariable Long id, @CurrentUser UserPrincipal currentUser) {
        List<Silo> availableSilos = findAll(currentUser);
        for (Silo silo : availableSilos) {
            if (silo.getSilo_id().equals(id)) {
                return ResponseEntity.ok().body(silo);
            }
        }
        return ResponseEntity.notFound().build();
    }

    @PatchMapping(path = "/{id}")
    public ResponseEntity<Silo> partialUpdate(@PathVariable("id") Long id, @CurrentUser UserPrincipal currentUser, @RequestBody Map<String, Object> updates) {
        List<Silo> availableSilos = findAll(currentUser);
        for (Silo silo : availableSilos) {
            if (silo.getSilo_id().equals(id)) {
                if (updates.containsKey("content")) {
                    silo.setContent((String) updates.get("content"));
                }
                if (updates.containsKey("fill_level")) {
                    silo.setFill_level((Double) updates.get("fill_level"));
                }
                Silo updated = siloRepository.save(silo);
                return ResponseEntity.ok().body(updated);
            }
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<?> createSilo(@Valid @RequestBody CreateSiloRequest createSiloRequest, @CurrentUser UserPrincipal currentUser) {
        User user = userRepository.findById(currentUser.getUser_id())
                .orElseThrow(() -> new ResourceNotFoundException("Current user not found " + currentUser.getUser_id()));
        Silo silo = new Silo();
        silo.setFill_level(createSiloRequest.getFill_level());
        silo.setContent(createSiloRequest.getContent());
        silo.setCapacity(createSiloRequest.getCapacity());
        silo.setFarm(user.getFarm());

        Silo toSave = siloRepository.save(silo);
        return ResponseEntity.ok().body(toSave);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMachine(@PathVariable Long id, @CurrentUser UserPrincipal currentUser) {

        List<Silo> availableSilos = findAll(currentUser);
        for (Silo silo : availableSilos) {
            if (silo.getSilo_id().equals(id)) {
                siloRepository.deleteById(silo.getSilo_id());
                return ResponseEntity.noContent().build();

            }
        }
        return ResponseEntity.status(404).build();
    }

}