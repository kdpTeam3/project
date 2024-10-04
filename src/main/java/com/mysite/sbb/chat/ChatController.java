package com.mysite.sbb.chat;

import com.mysite.sbb.user.SiteUser;
import com.mysite.sbb.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Controller
public class ChatController {

    private final UserService userService;
    private final ChatService chatService;
    private final SimpMessagingTemplate messagingTemplate;

    @GetMapping("/chat/{receiverId}")
    public String chat(@PathVariable("receiverId") Long receiverId, Principal principal, Model model) {
        SiteUser sender = userService.findByUsername(principal.getName());
        SiteUser receiver = userService.getUserById(receiverId);

        // 채팅 기록 가져오기
        List<ChatMessage> chatHistory = chatService.getChatHistory(sender.getId(), receiver.getId());

        // 모델에 추가
        model.addAttribute("sender", sender);
        model.addAttribute("receiver", receiver);
        model.addAttribute("receiverUsername", receiver.getUsername());  // 상대방 유저 이름 추가
        model.addAttribute("chatHistory", chatHistory);

        return "message/chat_room";
    }

    @MessageMapping("/chat.sendMessage")
    public void sendMessage(ChatMessage message) {
        if (message.getSenderId() == null || message.getReceiverId() == null) {
            System.out.println("Sender ID 또는 Receiver ID가 설정되지 않았습니다.");
            return;
        }

        // 서버에서 전송 시간을 설정하여 메시지를 저장
        message.setSentAt(LocalDateTime.now());

        // 메시지 저장
        chatService.saveMessage(message.getSenderId(), message.getReceiverId(), message.getContent());

        // 메시지 전송
        String destination = "/topic/chat/" + message.getReceiverId();
        messagingTemplate.convertAndSend(destination, message);
    }
}
