package com.backend.mapper;

import com.backend.model.entity.ChatMessage;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.util.List;

@Mapper
public interface ChatMessageMapper {
    @Insert("INSERT INTO chat_message (role, content, create_time, session_id) " +
            "VALUES (#{role}, #{content}, #{createTime}, #{sessionId})")
    void insert(ChatMessage message);

    @Select("SELECT * FROM chat_message WHERE session_id = #{sessionId} ORDER BY create_time ASC")
    List<ChatMessage> selectBySessionId(String sessionId);
} 