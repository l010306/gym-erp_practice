package com.gym.erp.membership.repository;

import com.gym.erp.membership.entity.CheckInLog;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CheckInLogRepository extends JpaRepository<CheckInLog, Long> {
}
