package com.springboot.ylw.controller;

import com.springboot.ylw.common.Result;
import com.springboot.ylw.entity.Message;
import com.springboot.ylw.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/message")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @PostMapping("/send")
    public Result<?> send(@RequestBody Message message) {
        return Result.success(messageService.sendMessage(message));
    }

    @GetMapping("/inbox/{userId}")
    public Result<?> inbox(@PathVariable Integer userId) {
        return Result.success(messageService.getInbox(userId));
    }

    @GetMapping("/conversation")
    public Result<?> conversation(@RequestParam Integer userId1, @RequestParam Integer userId2) {
        return Result.success(messageService.getConversation(userId1, userId2));
    }

    @PutMapping("/read")
    public Result<?> markRead(@RequestParam Integer fromId, @RequestParam Integer toId) {
        return Result.success(messageService.markRead(fromId, toId));
    }

    @GetMapping("/unread-count/{userId}")
    public Result<?> unreadCount(@PathVariable Integer userId) {
        return Result.success(messageService.countUnread(userId));
    }
}
