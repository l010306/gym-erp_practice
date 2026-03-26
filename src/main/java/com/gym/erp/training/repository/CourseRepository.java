package com.gym.erp.training.repository;

import com.gym.erp.training.entity.Course;
import com.gym.erp.training.entity.CourseType;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    // 查询某个教练的所有课程
    List<Course> findByCoachId(Long coachId);

    // 查询某个门店的所有课程
    List<Course> findByBranchId(Long branchId);

    // 查询某个门店特定类型的课程
    List<Course> findByBranchIdAndType(Long branchId, CourseType type);
}
