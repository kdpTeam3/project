package com.mysite.sbb.chat;

import com.mysite.sbb.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ChatService {

    private final ChatMessageRepository chatMessageRepository;
    private final UserService userService;

    // 메시지 저장
    public void saveMessage(Long senderId, Long receiverId, String content) {
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setSenderId(senderId);
        chatMessage.setReceiverId(receiverId);
        chatMessage.setContent(content);
        chatMessage.setSenderUsername(userService.getUserById(senderId).getUsername());  // 발신자 이름 저장
        
        // 서버에서 시간을 기록하는 부분
        LocalDateTime currentTime = LocalDateTime.now();
        chatMessage.setSentAt(currentTime);  // 전송 시간 저장
        
        chatMessage.setType(ChatMessage.MessageType.CHAT);
        chatMessageRepository.save(chatMessage);  // 저장
    }

    // 채팅 기록 가져오기
    public List<ChatMessage> getChatHistory(Long senderId, Long receiverId) {
        // 발신자-수신자와 수신자-발신자 간의 모든 메시지를 조회하여 결합 후, 시간순으로 정렬
        List<ChatMessage> chatHistory = chatMessageRepository.findBySenderIdAndReceiverId(senderId, receiverId);
        chatHistory.addAll(chatMessageRepository.findBySenderIdAndReceiverId(receiverId, senderId));
        
        return chatHistory.stream()
                .sorted((m1, m2) -> m1.getSentAt().compareTo(m2.getSentAt()))
                .collect(Collectors.toList());
    }
}
