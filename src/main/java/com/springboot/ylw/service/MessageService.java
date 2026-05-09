package com.springboot.ylw.service;

import com.springboot.ylw.entity.Message;
import java.util.List;

public interface MessageService {
    String sendMessage(Message message);
    List<Message> getInbox(Integer userId);
    List<Message> getConversation(Integer userId1, Integer userId2);
    String markRead(Integer fromId, Integer toId);
    int countUnread(Integer userId);
}
