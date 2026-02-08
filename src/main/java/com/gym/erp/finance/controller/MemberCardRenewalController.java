package com.gym.erp.finance.controller;

import com.gym.erp.finance.entity.MemberCardLog;
import com.gym.erp.finance.repository.MemberCardLogRepository;
import com.gym.erp.membership.service.MemberService;

import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/renewals")
public class MemberCardRenewalController {

    private final MemberService memberService;
    private final MemberCardLogRepository logRepository;

    public MemberCardRenewalController(MemberService memberService, MemberCardLogRepository logRepository) {
        this.memberService = memberService;
        this.logRepository = logRepository;
    }

    /**
     * 经理页面：查看所有待审批申请
     */
    @GetMapping("/pending")
    public String listPendingRenewals(Model model, @RequestParam(required = false) String error) {
        List<MemberCardLog> pendingLogs = logRepository.findByStatus("PENDING");
        model.addAttribute("logs", pendingLogs);
        model.addAttribute("pageTitle", "待审批列表 (Pending)");
        model.addAttribute("showPassword", true);
        model.addAttribute("isHistory", false);
        model.addAttribute("error", error); // 传递错误信息
        return "renewal/list";
    }

    /**
     * 经理页面：查看历史记录 (已批准/已拒绝/已撤销)
     */
    @GetMapping("/history")
    public String listHistoryRenewals(Model model, @RequestParam(required = false) String error) {
        List<MemberCardLog> approvedLogs = logRepository.findByStatus("APPROVED");
        model.addAttribute("logs", approvedLogs);
        model.addAttribute("pageTitle", "已审批历史 (Approved)");
        model.addAttribute("showPassword", true);
        model.addAttribute("isHistory", true);
        model.addAttribute("error", error); // 传递错误信息
        return "renewal/list";
    }

    /**
     * 前台页面：提交申请表单
     */
    @GetMapping("/create")
    public String showCreateForm(@RequestParam(required = false) Long cardId, Model model) {
        model.addAttribute("cardId", cardId);
        return "renewal/form"; // 对应 templates/renewal/form.html
    }

    /**
     * 处理提交申请
     */
    @PostMapping("/create")
    public String processCreateForm(
            @RequestParam Long cardId,
            @RequestParam Long requesterId,
            @RequestParam Integer monthsToAdd) {

        memberService.submitRenewalRequest(cardId, requesterId, monthsToAdd);
        return "redirect:/renewals/create?success";
    }

    /**
     * 经理审批通过
     */
    @PostMapping("/{logId}/approve")
    public String approveRenewal(
            @PathVariable Long logId,
            @RequestParam Long approverId,
            @RequestParam String password,
            org.springframework.web.servlet.mvc.support.RedirectAttributes redirectAttributes) {

        try {
            memberService.approveRenewalRequest(logId, approverId, password);
            return "redirect:/renewals/pending";
        } catch (RuntimeException e) {
            redirectAttributes.addAttribute("error", e.getMessage());
            return "redirect:/renewals/pending";
        }
    }

    /**
     * 经理撤销申请
     */
    @PostMapping("/{logId}/revoke")
    public String revokeRenewal(
            @PathVariable Long logId,
            @RequestParam Long approverId,
            @RequestParam String password,
            org.springframework.web.servlet.mvc.support.RedirectAttributes redirectAttributes) {

        try {
            memberService.revokeRenewal(logId, approverId, password);
            return "redirect:/renewals/history";
        } catch (RuntimeException e) {
            redirectAttributes.addAttribute("error", e.getMessage());
            return "redirect:/renewals/history";
        }
    }
}
