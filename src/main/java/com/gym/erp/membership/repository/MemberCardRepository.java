package com.gym.erp.membership.repository;

import com.gym.erp.membership.entity.MemberCard;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberCardRepository extends JpaRepository<MemberCard, Long> {

    // 这是一个非常实用的自定义查询：根据会员 ID 查找其名下的所有会员卡
    // 在连锁健身房场景中，一个会员可能会同时持有多种卡（如私教课时卡和健身年卡）
    List<MemberCard> findByMemberId(Long memberId);
}
