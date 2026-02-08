package com.gym.erp.finance.repository;

import com.gym.erp.finance.entity.OperationLog;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OperationLogRepository extends JpaRepository<OperationLog, Long> {
}
