package com.springboot.ylw.mapper;

import com.springboot.ylw.entity.Message;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface MessageMapper {
    int countUnread(Integer userId);
    List<Message> selectInbox(Integer toId);
    List<Message> selectByToId(Integer toId);
    List<Message> selectConversation(@Param("userId1") Integer userId1, @Param("userId2") Integer userId2);
    int insert(Message message);
    int markRead(@Param("fromId") Integer fromId, @Param("toId") Integer toId);
}
