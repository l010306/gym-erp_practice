package com.gym.erp.staff.repository;

import com.gym.erp.staff.entity.Coach;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CoachRepository extends JpaRepository<Coach, Long> {

    // 预留一些常用的查询方法
    List<Coach> findByHomeBranchId(Long homeBranchId);

    List<Coach> findBySpecializationContaining(String keyword); // 按负责领域模糊搜索
}
