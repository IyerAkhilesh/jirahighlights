package com.jirahighlights.chatbotservice.repository;

import com.jirahighlights.chatbotservice.model.ConversationHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConversationRepository extends JpaRepository<ConversationHistory, Long> {
}