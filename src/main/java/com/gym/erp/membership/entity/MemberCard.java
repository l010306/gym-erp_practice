// Java
package com.gym.erp.membership.entity;



import jakarta.persistence.*;
import java.time.LocalDate;
import lombok.Data;

@Data
@Entity
@Table(name = "member_card")
public class MemberCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate expireDate;

    private Long homeBranchId;

    private Boolean isUniversal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    /**
     * 判断是否允许进入：未过期且符合门店权限。
     * 如果 expireDate 为 null，则视为不过期。
     * 如果 homeBranchId 为 null，则视为允许任意门店。
     */
    public boolean isAllowedEntry(Long currentBranchId) {
        if (expireDate != null && expireDate.isBefore(LocalDate.now())) {
            return false;
        }
        if (homeBranchId == null) {
            return true;
        }
        return homeBranchId.equals(currentBranchId);
    }
}
