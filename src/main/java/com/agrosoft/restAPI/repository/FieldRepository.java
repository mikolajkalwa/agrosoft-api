package com.agrosoft.restAPI.repository;

import com.agrosoft.restAPI.model.Farm;
import com.agrosoft.restAPI.model.Field;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FieldRepository extends JpaRepository<Field, Long> {
    List<Field> findByFarm(Farm farm);
}
