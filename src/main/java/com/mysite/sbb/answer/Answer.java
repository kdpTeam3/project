package com.mysite.sbb.answer;

import java.time.LocalDateTime;

import com.mysite.sbb.question.Question;
import com.mysite.sbb.user.SiteUser;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity	// h2 database 애너테이션 불러오기
public class Answer {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(columnDefinition = "TEXT") // 글자 수 제한 없이 TEXT형태로 들어감
	private String content; // 내용
	
	private LocalDateTime createDate; // 작성 시간
	
	@ManyToOne	// 질문 하나에 답변 여러개
	// Many = 답변 / one / 질문을 의미
	
	private Question question;	// 질문의 대한 엔티티 속성을 가진다.
	
	@ManyToOne
	private SiteUser author;
	
	private LocalDateTime modifyDate;

}
