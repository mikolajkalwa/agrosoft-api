package com.agrosoft.restAPI.repository;

import com.agrosoft.restAPI.model.Farm;
import com.agrosoft.restAPI.model.Machine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MachineRepository extends JpaRepository<Machine, Long> {
    List<Machine> findByFarm(Farm farm);
}
