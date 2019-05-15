package com.agrosoft.restAPI.repository;

import com.agrosoft.restAPI.model.Farm;
import com.agrosoft.restAPI.model.Silo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SiloRepository extends JpaRepository<Silo, Long> {
    List<Silo> findByFarm(Farm farm);
}
