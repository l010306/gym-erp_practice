package com.gym.erp.training.repository;

import com.gym.erp.training.entity.CourseBooking;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseBookingRepository extends JpaRepository<CourseBooking, Long> {

    // 根据课程ID查找所有预约记录
    List<CourseBooking> findByCourseId(Long courseId);

    // 根据会员ID查找所有预约记录
    List<CourseBooking> findByMemberId(Long memberId);

    // 统计某个课程的已确认预约数量（用于检查是否满员）
    @Query("SELECT COUNT(b) FROM CourseBooking b WHERE b.course.id = ?1 AND b.status = 'CONFIRMED'")
    Long countConfirmedByCourseId(Long courseId);

    // 检查会员是否已预约某课程
    boolean existsByCourseIdAndMemberIdAndStatus(Long courseId, Long memberId, CourseBooking.BookingStatus status);
}
