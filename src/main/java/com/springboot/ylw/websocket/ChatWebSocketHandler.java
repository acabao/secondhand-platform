package com.springboot.ylw.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 私信 WebSocket 单例 — 按 userId 维护在线会话。
 * 客户端连接路径：/ws/chat?userId={id}
 * 服务端推送消息格式：{"type":"message"|"unread"|"ping", ...}
 */
@Component
public class ChatWebSocketHandler extends TextWebSocketHandler {

    private static final Logger log = LoggerFactory.getLogger(ChatWebSocketHandler.class);
    private static final ObjectMapper MAPPER = new ObjectMapper();

    /** userId -> session */
    private final Map<Integer, WebSocketSession> sessions = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        Integer userId = (Integer) session.getAttributes().get("userId");
        if (userId == null) {
            try { session.close(CloseStatus.NOT_ACCEPTABLE); } catch (Exception ignored) {}
            return;
        }
        sessions.put(userId, session);
        log.info("[ws] user {} connected, total online = {}", userId, sessions.size());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        Integer userId = (Integer) session.getAttributes().get("userId");
        if (userId != null) sessions.remove(userId, session);
        log.info("[ws] user {} disconnected ({}), total online = {}", userId, status, sessions.size());
    }

    /** 心跳：客户端发 ping，服务端原样回 pong */
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String text = message.getPayload();
        if (text != null && text.contains("\"type\":\"ping\"")) {
            session.sendMessage(new TextMessage("{\"type\":\"pong\"}"));
        }
    }

    /** 向指定用户推送任意 JSON payload；离线则直接忽略 */
    public void pushTo(Integer userId, Object payload) {
        WebSocketSession s = sessions.get(userId);
        if (s == null || !s.isOpen()) return;
        try {
            s.sendMessage(new TextMessage(MAPPER.writeValueAsString(payload)));
        } catch (Exception e) {
            log.warn("[ws] push to {} failed: {}", userId, e.getMessage());
        }
    }

    public boolean isOnline(Integer userId) {
        WebSocketSession s = sessions.get(userId);
        return s != null && s.isOpen();
    }
}
