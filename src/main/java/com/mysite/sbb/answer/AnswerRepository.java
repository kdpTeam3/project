package com.mysite.sbb.answer;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mysite.sbb.user.SiteUser;

public interface AnswerRepository extends JpaRepository<Answer, Integer> {
	// 사용자가 작성한 모든 답변 삭제
	void deleteByAuthor(SiteUser author);
}
