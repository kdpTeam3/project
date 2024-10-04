package com.mysite.sbb.chat;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long senderId;  // 발신자 ID
    private String senderUsername;  // 발신자 이름
    private String content;  // 메시지 내용
    private Long receiverId;  // 수신자 ID
    private MessageType type;  // 메시지 유형
    private LocalDateTime sentAt;  // 메시지 전송 시간

    public enum MessageType {
        CHAT,
        JOIN,
        LEAVE
    }
}


