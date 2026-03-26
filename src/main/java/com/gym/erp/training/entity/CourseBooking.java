package com.gym.erp.training.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.gym.erp.membership.entity.Member;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.Data;

@Data
@Entity
@Table(name = "course_bookings")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class CourseBooking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "course_id", nullable = false)
    @JsonIgnoreProperties({ "coach", "bookings", "hibernateLazyInitializer", "handler" })
    private Course course;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "member_id", nullable = false)
    @JsonIgnoreProperties({ "cards", "hibernateLazyInitializer", "handler" })
    private Member member;

    private LocalDateTime bookingTime; // 预约时间

    @Enumerated(EnumType.STRING)
    private BookingStatus status; // 预约状态

    public enum BookingStatus {
        CONFIRMED, // 已确认
        CANCELLED // 已取消
    }
}
