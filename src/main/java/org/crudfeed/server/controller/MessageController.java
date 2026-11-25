package org.crudfeed.server.controller;

import org.crudfeed.core.dto.message.ViewMessagesRequestDto;
import org.crudfeed.core.dto.message.DeleteMessageRequestDto;
import org.crudfeed.core.dto.message.EditMessageRequestDto;
import org.crudfeed.core.dto.message.WriteMessageRequestDto;
import org.crudfeed.core.entity.Message;
import org.crudfeed.core.service.MessageService;
import org.crudfeed.core.service.UserService;
import org.crudfeed.server.security.TokenStore;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class MessageController {

    private final MessageService messageService;
    private final UserService userService;
    private final TokenStore tokenStore;

    public MessageController(MessageService messageService, UserService userService, TokenStore tokenStore) {
        this.messageService = messageService;
        this.userService = userService;
        this.tokenStore = tokenStore;
    }

    @PostMapping("/writeMessage")
    public String writeMessage(
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authHeader,
            @RequestBody WriteMessageRequestDto dto) {
        try {
            long senderId = tokenStore.requireUserId(authHeader);
            long receiverId = userService.getUserId(dto.receiverNickname());
            return messageService.sendMessage(senderId, receiverId, dto.content());
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    @PostMapping("/viewMessages")
    public List<Message> viewMessages(
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authHeader,
            @RequestBody ViewMessagesRequestDto dto) {
        try {
            long senderId = tokenStore.requireUserId(authHeader);
            long receiverId = userService.getUserId(dto.receiverNickname());
            return messageService.viewMessages(senderId, receiverId);
        } catch (Exception e) {
            return null;
        }
    }

    @PostMapping("/editMessage")
    public String editMessage(
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authHeader,
            @RequestBody EditMessageRequestDto dto) {
        try {
            long senderId = tokenStore.requireUserId(authHeader);
            return messageService.editMessage(dto.messageId(), dto.content(), senderId);
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    @PostMapping("/deleteMessage")
    public String deleteMessage(
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authHeader,
            @RequestBody DeleteMessageRequestDto dto) {
        try {
            long senderId = tokenStore.requireUserId(authHeader);
            return messageService.deleteMessage(dto.messageId(), senderId);
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
}