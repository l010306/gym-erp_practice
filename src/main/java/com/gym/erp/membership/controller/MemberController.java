package com.gym.erp.membership.controller;

import com.gym.erp.membership.entity.Member;
import com.gym.erp.membership.repository.MemberRepository;
import com.gym.erp.membership.service.MemberService;

import java.util.List;
import java.util.Optional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/members")
public class MemberController {

    private final MemberRepository memberRepository;
    private final MemberService memberService;

    public MemberController(MemberRepository memberRepository, MemberService memberService) {
        this.memberRepository = memberRepository;
        this.memberService = memberService;
    }

    @GetMapping
    public List<Member> getAllMembers() {
        return memberRepository.findAll();
    }

    @GetMapping("/search")
    public Optional<Member> getMemberByPhone(@RequestParam String phone) {
        return memberRepository.findByPhone(phone);
    }

    @PostMapping("/check-in")
    public String checkIn(@RequestBody CheckInRequest request) {
        return memberService.checkEntry(request.memberId(), request.branchId());
    }

    public record CheckInRequest(Long memberId, Long branchId) {
    }

}
