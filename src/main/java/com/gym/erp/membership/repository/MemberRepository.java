package com.gym.erp.membership.repository;

import com.gym.erp.membership.entity.Member;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    // AI 插件可以帮你生成的自定义查询：通过手机号查找会员
    Optional<Member> findByPhone(String phone);
}
