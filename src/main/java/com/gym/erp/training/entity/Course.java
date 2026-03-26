package com.gym.erp.training.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.gym.erp.staff.entity.Coach;
import com.gym.erp.staff.entity.Employee;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

@Data
@Entity
@Table(name = "courses")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name; // 课程名称（如：零基础瑜伽）

    private String description; // 课程介绍

    private LocalDateTime startTime; // 开始时间

    private LocalDateTime endTime; // 结束Time

    // 核心关联：多门课程可以由同一个教练教授
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "coach_id", nullable = false)
    @JsonIgnoreProperties({ "courses", "hibernateLazyInitializer", "handler" })
    private Coach coach;

    private Long branchId; // 所属门店 ID

    @Enumerated(EnumType.STRING) // 在数据库中存储字符串（GROUP 或 PERSONAL）
    private CourseType type;

    // 如果是团课，需要最大人数限制；如果是私教，这个值可以是 1
    private Integer maxCapacity;

    // 记录创建该课程的员工
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "created_by")
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
    private Employee createdBy;

    // 课程创建时间
    @CreationTimestamp
    private LocalDateTime createdAt;
}
