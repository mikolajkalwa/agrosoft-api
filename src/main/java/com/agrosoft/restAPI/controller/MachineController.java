package com.agrosoft.restAPI.controller;

import com.agrosoft.restAPI.exception.ResourceNotFoundException;
import com.agrosoft.restAPI.model.Machine;
import com.agrosoft.restAPI.model.User;
import com.agrosoft.restAPI.payload.CreateMachineRequest;
import com.agrosoft.restAPI.repository.MachineRepository;
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
@RequestMapping({"/api/machines"})
public class MachineController {
    private MachineRepository machineRepository;
    private UserRepository userRepository;

    public MachineController(MachineRepository machineRepository, UserRepository userRepository) {
        this.machineRepository = machineRepository;
        this.userRepository = userRepository;
    }

    @GetMapping()
    public List findAll(@CurrentUser UserPrincipal currentUser) {
        Long currentUserID = currentUser.getUser_id();
        return userRepository.findById(currentUserID)
                .map(user -> machineRepository.findByFarm(user.getFarm()))
                .orElse(Collections.emptyList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Machine> getMachine(@PathVariable Long id, @CurrentUser UserPrincipal currentUser) {
        List<Machine> availableMachines = findAll(currentUser);
        for (Machine machine : availableMachines) {
            if (machine.getMachine_id().equals(id)) {
                return ResponseEntity.ok().body(machine);
            }
        }
        return ResponseEntity.notFound().build();
    }

    @PatchMapping(path = "/{id}")
    public ResponseEntity<Machine> partialUpdate(@PathVariable("id") Long id, @CurrentUser UserPrincipal currentUser, @RequestBody Map<String, Object> updates) {
        List<Machine> availableMachines = findAll(currentUser);
        for (Machine machine : availableMachines) {
            if (machine.getMachine_id().equals(id)) {
                if (updates.containsKey("monthly_instalment")) {
                    machine.setMonthly_instalment((Double) updates.get("monthly_instalment"));
                }
                Machine updated = machineRepository.save(machine);
                return ResponseEntity.ok().body(updated);
            }
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<?> createMachine(@Valid @RequestBody CreateMachineRequest createMachineRequest, @CurrentUser UserPrincipal currentUser) {
        User user = userRepository.findById(currentUser.getUser_id())
                .orElseThrow(() -> new ResourceNotFoundException("Current user not found " + currentUser.getUser_id()));
        Machine machine = new Machine();
        machine.setBrand(createMachineRequest.getBrand());
        machine.setModel(createMachineRequest.getModel());
        machine.setMonthly_instalment(createMachineRequest.getMonthly_instalment());
        machine.setFarm(user.getFarm());

        Machine toSave = machineRepository.save(machine);
        return ResponseEntity.ok().body(toSave);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMachine(@PathVariable Long id, @CurrentUser UserPrincipal currentUser) {

        List<Machine> availableMachines = findAll(currentUser);
        for (Machine machine : availableMachines) {
            if (machine.getMachine_id().equals(id)) {
                machineRepository.deleteById(machine.getMachine_id());
                return ResponseEntity.noContent().build();

            }
        }
        return ResponseEntity.status(404).build();
    }

}
