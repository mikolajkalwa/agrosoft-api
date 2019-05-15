package com.agrosoft.restAPI.repository;

import com.agrosoft.restAPI.model.Farm;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FarmRepository extends JpaRepository<Farm, Long> {

}
