package com.gym.erp.membership.entity;



import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.Data;

@Data
@Entity
@Table(name = "check_in_logs")
public class CheckInLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long memberId;      // 记录会员ID
    private Long branchId;      // 记录门店ID
    private LocalDateTime checkInTime; // 签到时间
    private String status;      // 结果：SUCCESS 或 DENIED
    private String reason;      // 拒绝的原因（如：卡过期）
}
