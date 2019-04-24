package com.agrosoft.restAPI.repository;

import com.agrosoft.restAPI.model.MessageRecipient;
import com.agrosoft.restAPI.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRecipientRepository extends JpaRepository<MessageRecipient, Long> {
    List<MessageRecipient> findByRecipient(User recipient);
}
