package com.mysite.sbb.message;

import com.mysite.sbb.user.SiteUser;
import com.mysite.sbb.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;

@RequiredArgsConstructor
@Controller
public class MessageController {

    private final MessageService messageService;
    private final UserService userService;

    // 메시지 작성 페이지 반환
    @GetMapping("/messages")
    public String messageForm() {
        return "message/message_send";  // 메시지 작성 페이지 템플릿
    }

    // 받은 메시지 목록 조회
 // MessageController.java

    @GetMapping("/messages/list")
    public String listReceivedMessages(Model model, Principal principal) {
        SiteUser receiver = userService.findByUsername(principal.getName());
        List<Message> receivedMessages = messageService.getReceivedMessages(receiver.getId());

        // 각 메시지를 읽음 상태로 업데이트
        for (Message message : receivedMessages) {
            if (!message.isRead()) {
                message.setRead(true);  // 읽음 처리
                messageService.save(message);  // DB에 저장
            }

            SiteUser sender = message.getSender();
            boolean hasMessageHistory = messageService.hasMessageHistory(sender, receiver);
            message.setHasMessageHistory(hasMessageHistory);
        }

        model.addAttribute("messages", receivedMessages);
        return "message/message_list";
    }



    // 보낸 메시지 목록 조회
 // 메시지를 주고받은 기록이 있을 때 채팅 버튼을 표시하기 위한 로직 추가
    @GetMapping("/messages/sent")
    public String listSentMessages(Model model, Principal principal) {
        SiteUser sender = userService.findByUsername(principal.getName());
        List<Message> sentMessages = messageService.getSentMessages(sender.getId());

        for (Message message : sentMessages) {
            SiteUser receiver = message.getReceiver();
            boolean hasMessageHistory = messageService.hasMessageHistory(sender, receiver);
            // 수신자가 채팅을 시작한 기록이 있을 때만 버튼을 노출하도록 설정
            message.setHasMessageHistory(hasMessageHistory);
        }

        model.addAttribute("messages", sentMessages);
        return "message/message_sent_list";  // 보낸 메시지 목록 템플릿
    }


    // 메시지 전송
    @PostMapping("/messages/send")
    public String sendMessage(@RequestParam("receiverName") String receiverName, @RequestParam("content") String content, Principal principal) {
        SiteUser sender = userService.findByUsername(principal.getName());  // 로그인한 사용자 (보내는 사람)
        SiteUser receiver = userService.findByUsername(receiverName);  // 수신자 이름으로 조회
        messageService.sendMessage(sender, receiver, content);  // 메시지 전송
        return "redirect:/messages/sent";  // 메시지 전송 후 보낸 메시지 목록 페이지로 리다이렉트
    }

    // 실시간 채팅 페이지 반환 (메시지 주고받은 사용자 간에만 허용)
    @GetMapping("/messages/chat/{receiverId}")
    public String initiateChat(@PathVariable("receiverId") Long receiverId, Principal principal, Model model) {
        SiteUser sender = userService.findByUsername(principal.getName());
        SiteUser receiver = userService.getUserById(receiverId);

        // 메시지를 주고받은 사용자 간에만 실시간 채팅을 허용
        boolean hasMessageHistory = messageService.hasMessageHistory(sender, receiver);

        if (!hasMessageHistory) {
            return "redirect:/messages/list";  // 메시지 주고받은 기록이 없으면 메시지 리스트로 리다이렉트
        }

        model.addAttribute("sender", sender);
        model.addAttribute("receiver", receiver);
        model.addAttribute("chatUrl", "/chat/" + receiverId);  // 채팅 경로 제공
        return "chat/chat_room";  // 실시간 채팅 페이지 템플릿으로 이동
    }
}
