package com.agrosoft.restAPI.controller;

import com.agrosoft.restAPI.exception.AppException;
import com.agrosoft.restAPI.exception.ResourceNotFoundException;
import com.agrosoft.restAPI.model.Message;
import com.agrosoft.restAPI.model.MessageRecipient;
import com.agrosoft.restAPI.model.User;
import com.agrosoft.restAPI.payload.ApiResponse;
import com.agrosoft.restAPI.payload.CreateMessageRequest;
import com.agrosoft.restAPI.repository.MessageRecipientRepository;
import com.agrosoft.restAPI.repository.MessageRepository;
import com.agrosoft.restAPI.repository.UserRepository;
import com.agrosoft.restAPI.security.CurrentUser;
import com.agrosoft.restAPI.security.UserPrincipal;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.Instant;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/messages")
public class MessageController {
    private MessageRecipientRepository messageRecipientRepository;
    private MessageRepository messageRepository;
    private UserRepository userRepository;

    public MessageController(MessageRecipientRepository messageRecipientRepository, MessageRepository messageRepository, UserRepository userRepository) {
        this.messageRecipientRepository = messageRecipientRepository;
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
    }

    @GetMapping
    public List getMessages(@CurrentUser UserPrincipal currentUser) {
        Long currentUserID = currentUser.getUser_id();
        return userRepository.findById(currentUserID)
                .map(user -> messageRecipientRepository.findByRecipient(user))
                .orElse(Collections.emptyList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MessageRecipient> getMessage(@PathVariable long id, @CurrentUser UserPrincipal userPrincipal) {
        MessageRecipient messageRecipient = messageRecipientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Message not found" + id));

        if (userPrincipal.getUser_id().equals(messageRecipient.getRecipient().getUser_id())) {
            messageRecipient.setIs_read(true);
            messageRecipientRepository.save(messageRecipient);
        }

        return ResponseEntity.ok().body(messageRecipient);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMessage(@PathVariable long id, @CurrentUser UserPrincipal userPrincipal) {
        MessageRecipient messageRecipient = messageRecipientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Message not found!"));
        User currentUser = userRepository.findById(userPrincipal.getUser_id())
                .orElseThrow(() -> new AppException("Current user not found!"));
        if (messageRecipient.getRecipient().equals(currentUser)) {
            messageRecipientRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(403).build();
        }
    }

    @PostMapping
    public ResponseEntity<?> sendMessage(@Valid @RequestBody CreateMessageRequest createMessageRequest, @CurrentUser UserPrincipal userPrincipal) {
        User currentUser = userRepository.findById(userPrincipal.getUser_id())
                .orElseThrow(() -> new ResourceNotFoundException("Current user not found " + userPrincipal.getUser_id()));

        User recipient = userRepository.findById(createMessageRequest.getRecipient_id())
                .orElseThrow(() -> new ResourceNotFoundException("Recipient not found" + createMessageRequest.getRecipient_id()));

        MessageRecipient messageRecipient = new MessageRecipient();
        Message message = new Message();

        message.setSubject(createMessageRequest.getSubject());
        message.setBody(createMessageRequest.getBody());
        message.setCreated_at(Instant.now());
        message.setCreator(currentUser);

        Message finalMessage = messageRepository.save(message);

        messageRecipient.setIs_read(false);
        messageRecipient.setMessage(finalMessage);
        messageRecipient.setRecipient(recipient);

        messageRecipientRepository.save(messageRecipient);

        return ResponseEntity.ok().body(new ApiResponse(true, "Sent messsage!"));
    }
}
