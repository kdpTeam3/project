package com.mysite.sbb.question;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.mysite.sbb.user.SiteUser;

public interface QuestionRepository extends JpaRepository<Question, Integer>{
	
	Question findBySubject(String subject);
	Question findBySubjectAndContent(String subject, String content);
	List<Question> findBySubjectLike(String subject);
	Page<Question> findAll(Pageable pageable);

	// 사용자가 작성한 모든 질문 삭제
	void deleteByAuthor(SiteUser author);
}
