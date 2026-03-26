package com.gym.erp.training.service;

import com.gym.erp.membership.entity.Member;
import com.gym.erp.membership.repository.MemberRepository;
import com.gym.erp.training.entity.Course;
import com.gym.erp.training.entity.CourseBooking;
import com.gym.erp.training.repository.CourseBookingRepository;
import com.gym.erp.training.repository.CourseRepository;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CourseBookingService {

    private final CourseBookingRepository bookingRepository;
    private final CourseRepository courseRepository;
    private final MemberRepository memberRepository;

    public CourseBookingService(CourseBookingRepository bookingRepository,
            CourseRepository courseRepository,
            MemberRepository memberRepository) {
        this.bookingRepository = bookingRepository;
        this.courseRepository = courseRepository;
        this.memberRepository = memberRepository;
    }

    /**
     * 会员预约课程
     */
    @Transactional
    public CourseBooking bookCourse(Long courseId, Long memberId) {
        // 1. 验证课程存在
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("未找到课程 ID: " + courseId));

        // 2. 验证会员存在
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("未找到会员 ID: " + memberId));

        // 3. 检查课程是否已开始
        if (course.getStartTime().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("课程已经开始，无法预约");
        }

        // 4. 检查会员是否已预约该课程
        if (bookingRepository.existsByCourseIdAndMemberIdAndStatus(
                courseId, memberId, CourseBooking.BookingStatus.CONFIRMED)) {
            throw new RuntimeException("您已经预约过该课程");
        }

        // 5. 检查课程是否已满员
        Long confirmedCount = bookingRepository.countConfirmedByCourseId(courseId);
        if (confirmedCount >= course.getMaxCapacity()) {
            throw new RuntimeException("课程已满员，无法预约");
        }

        // 6. 创建预约记录
        CourseBooking booking = new CourseBooking();
        booking.setCourse(course);
        booking.setMember(member);
        booking.setBookingTime(LocalDateTime.now());
        booking.setStatus(CourseBooking.BookingStatus.CONFIRMED);

        return bookingRepository.save(booking);
    }

    /**
     * 取消预约
     */
    @Transactional
    public void cancelBooking(Long bookingId, Long memberId) {
        CourseBooking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("未找到预约记录 ID: " + bookingId));

        // 验证是否是本人的预约
        if (!booking.getMember().getId().equals(memberId)) {
            throw new RuntimeException("无权取消他人的预约");
        }

        // 检查课程是否已开始
        if (booking.getCourse().getStartTime().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("课程已经开始，无法取消预约");
        }

        booking.setStatus(CourseBooking.BookingStatus.CANCELLED);
        bookingRepository.save(booking);
    }

    /**
     * 获取会员的所有预约
     */
    public List<CourseBooking> getMemberBookings(Long memberId) {
        return bookingRepository.findByMemberId(memberId);
    }

    /**
     * 获取课程的所有预约
     */
    public List<CourseBooking> getCourseBookings(Long courseId) {
        return bookingRepository.findByCourseId(courseId);
    }

    /**
     * 获取课程的当前预约人数
     */
    public Long getCourseBookingCount(Long courseId) {
        return bookingRepository.countConfirmedByCourseId(courseId);
    }
}
