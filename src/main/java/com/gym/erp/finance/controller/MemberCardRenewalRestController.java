package com.gym.erp.finance.controller;

import com.gym.erp.finance.entity.MemberCardLog;
import com.gym.erp.finance.repository.MemberCardLogRepository;
import com.gym.erp.membership.service.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/renewals")
public class MemberCardRenewalRestController {

    private final MemberService memberService;
    private final MemberCardLogRepository logRepository;

    public MemberCardRenewalRestController(MemberService memberService, MemberCardLogRepository logRepository) {
        this.memberService = memberService;
        this.logRepository = logRepository;
    }

    /**
     * 获取待审批的续费申请列表
     */
    @GetMapping("/pending")
    public ResponseEntity<List<MemberCardLog>> getPendingRenewals() {
        List<MemberCardLog> pendingLogs = logRepository.findByStatus("PENDING");
        return ResponseEntity.ok(pendingLogs);
    }

    /**
     * 获取已审批的续费历史记录
     */
    @GetMapping("/history")
    public ResponseEntity<List<MemberCardLog>> getHistoryRenewals() {
        List<MemberCardLog> approvedLogs = logRepository.findByStatus("APPROVED");
        return ResponseEntity.ok(approvedLogs);
    }

    /**
     * 提交新的续费申请
     */
    @PostMapping
    public ResponseEntity<?> submitRenewal(@RequestBody Map<String, Object> request) {
        try {
            Long cardId = Long.valueOf(request.get("cardId").toString());
            Long requesterId = Long.valueOf(request.get("requesterId").toString());
            Integer monthsToAdd = Integer.valueOf(request.get("monthsToAdd").toString());

            MemberCardLog log = memberService.submitRenewalRequest(cardId, requesterId, monthsToAdd);
            return ResponseEntity.ok(log);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    /**
     * 审批通过续费申请
     */
    @PostMapping("/{logId}/approve")
    public ResponseEntity<?> approveRenewal(
            @PathVariable Long logId,
            @RequestBody Map<String, Object> request) {
        try {
            Long approverId = Long.valueOf(request.get("approverId").toString());
            String password = request.get("password").toString();

            memberService.approveRenewalRequest(logId, approverId, password);

            Map<String, String> response = new HashMap<>();
            response.put("message", "审批成功");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    /**
     * 撤销已审批的续费申请
     */
    @PostMapping("/{logId}/revoke")
    public ResponseEntity<?> revokeRenewal(
            @PathVariable Long logId,
            @RequestBody Map<String, Object> request) {
        try {
            Long approverId = Long.valueOf(request.get("approverId").toString());
            String password = request.get("password").toString();

            memberService.revokeRenewal(logId, approverId, password);

            Map<String, String> response = new HashMap<>();
            response.put("message", "撤销成功");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
}
