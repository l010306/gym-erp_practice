package com.gym.erp.finance.repository;

import com.gym.erp.finance.entity.MemberCardLog;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberCardLogRepository extends JpaRepository<MemberCardLog, Long> {
    List<MemberCardLog> findByStatus(String status);

    List<MemberCardLog> findByCardId(Long cardId);
}
