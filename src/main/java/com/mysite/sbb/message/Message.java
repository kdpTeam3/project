// Message.java
package com.mysite.sbb.message;

import com.mysite.sbb.user.SiteUser;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private SiteUser sender;

    @ManyToOne
    private SiteUser receiver;

    private String content;
    private LocalDateTime sentAt;

    // 추가: 메시지가 읽혔는지 여부를 확인하는 필드
    private boolean isRead = false;

    // 채팅 이력이 있는지 여부
    @Transient
    private boolean hasMessageHistory;
}
