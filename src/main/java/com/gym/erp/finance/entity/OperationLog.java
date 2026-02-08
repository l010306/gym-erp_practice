package com.gym.erp.finance.entity;



import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

@Data
@Entity
@Table(name = "operation_logs")
public class OperationLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long operatorId;

    @Column(nullable = false)
    private String operationType; // SUBMIT, APPROVE, REVOKE

    @Column(nullable = false)
    private String targetEntity; // e.g., "MemberCardLog"

    @Column(nullable = false)
    private Long targetId;

    private String description;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
