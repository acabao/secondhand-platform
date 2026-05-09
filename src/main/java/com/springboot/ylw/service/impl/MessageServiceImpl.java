package com.springboot.ylw.service.impl;

import com.springboot.ylw.entity.Message;
import com.springboot.ylw.mapper.MessageMapper;
import com.springboot.ylw.service.MessageService;
import com.springboot.ylw.websocket.ChatWebSocketHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageMapper messageMapper;

    @Autowired
    private ChatWebSocketHandler wsHandler;

    @Override
    public String sendMessage(Message message) {
        messageMapper.insert(message);
        // 推送给接收方（若在线）：{type: "message", message: {...}, unread: N}
        if (message.getToId() != null) {
            Map<String, Object> payload = new HashMap<>();
            payload.put("type", "message");
            payload.put("message", message);
            payload.put("unread", messageMapper.countUnread(message.getToId()));
            wsHandler.pushTo(message.getToId(), payload);
        }
        return "发送成功";
    }

    @Override
    public List<Message> getInbox(Integer userId) {
        return messageMapper.selectInbox(userId);
    }

    @Override
    public List<Message> getConversation(Integer userId1, Integer userId2) {
        messageMapper.markRead(userId1, userId2);
        return messageMapper.selectConversation(userId1, userId2);
    }

    @Override
    public String markRead(Integer fromId, Integer toId) {
        messageMapper.markRead(fromId, toId);
        return "已标记为已读";
    }

    @Override
    public int countUnread(Integer userId) {
        return messageMapper.countUnread(userId);
    }
}
