package com.gym.erp.finance.entity;



import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Data
@Entity
@Table(name = "member_card_logs")
public class MemberCardLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "card_id", nullable = false)
    private Long cardId;

    @Column(name = "old_expire_date")
    private LocalDate oldExpireDate;

    @Column(name = "new_expire_date", nullable = false)
    private LocalDate newExpireDate;

    // 前台提交人ID
    @Column(name = "requester_id", nullable = false)
    private Long requesterId;

    // 经理审批人ID
    @Column(name = "approver_id")
    private Long approverId;

    // 状态: PENDING (待审批), APPROVED (已通过), REJECTED (已拒绝)
    @Column(nullable = false)
    private String status;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
