package com.gym.erp.component;

import com.gym.erp.finance.repository.MemberCardLogRepository;
import com.gym.erp.membership.repository.MemberRepository;
import com.gym.erp.training.repository.CourseRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    private final MemberRepository memberRepository;
    private final MemberCardLogRepository renewalRepository;
    private final CourseRepository courseRepository;

    public DashboardController(MemberRepository memberRepository, 
                               MemberCardLogRepository renewalRepository, 
                               CourseRepository courseRepository) {
        this.memberRepository = memberRepository;
        this.renewalRepository = renewalRepository;
        this.courseRepository = courseRepository;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("totalMembers", memberRepository.count());
        model.addAttribute("pendingRenewals", renewalRepository.findByStatus("PENDING").size());
        model.addAttribute("totalCourses", courseRepository.count());
        return "dashboard";
    }
}
