package com.gym.erp.membership.controller;

import com.gym.erp.membership.entity.Member;
import com.gym.erp.membership.repository.MemberRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/members/view")
public class MemberViewController {

    private final MemberRepository memberRepository;

    public MemberViewController(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("member", new Member());
        return "membership/register";
    }

    @PostMapping("/register")
    public String registerMember(@ModelAttribute Member member) {
        if (member.getBranchId() == null) {
            member.setBranchId(1L);
        }
        memberRepository.save(member);
        return "redirect:/?registered=true";
    }
}
