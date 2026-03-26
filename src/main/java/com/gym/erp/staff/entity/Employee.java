package com.gym.erp.staff.entity;



import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

@Data
@Entity
@Table(name = "employees")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String gender;

    private String password; // 简单明文存储用于演示，生产环境应加密

    private Long branchId;

    @Column(unique = true)
    private String phone;

    private LocalDate hireDate;

    private String position;

    // 对应权限，以逗号分隔的权限编码 (e.g. "STAFF_READ,MEMBER_WRITE")
    private String permissions;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
