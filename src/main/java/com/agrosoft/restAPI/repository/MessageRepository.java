package com.agrosoft.restAPI.repository;

import com.agrosoft.restAPI.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {
}
