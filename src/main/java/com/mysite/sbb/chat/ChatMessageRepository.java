package com.mysite.sbb.chat;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findBySenderIdAndReceiverId(Long senderId, Long receiverId);
    List<ChatMessage> findByReceiverIdAndSenderId(Long receiverId, Long senderId);
}
