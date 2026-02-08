package com.gym.erp.membership.service;

import com.gym.erp.finance.entity.MemberCardLog;
import com.gym.erp.finance.entity.OperationLog;
import com.gym.erp.finance.repository.MemberCardLogRepository;
import com.gym.erp.finance.repository.OperationLogRepository;
import com.gym.erp.membership.entity.CheckInLog;
import com.gym.erp.membership.entity.MemberCard;
import com.gym.erp.membership.repository.CheckInLogRepository;
import com.gym.erp.membership.repository.MemberCardRepository;
import com.gym.erp.membership.repository.MemberRepository;
import com.gym.erp.staff.service.EmployeeService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MemberService {

    private final MemberCardRepository cardRepository;
    private final MemberRepository memberRepository;
    private final CheckInLogRepository logRepository;
    private final MemberCardLogRepository cardLogRepository;
    private final EmployeeService employeeService;
    private final OperationLogRepository operationLogRepository;

    // 2. 修正构造函数，完成 logRepository 的赋值
    public MemberService(MemberCardRepository cardRepository,
            MemberRepository memberRepository,
            CheckInLogRepository logRepository,
            MemberCardLogRepository cardLogRepository,
            EmployeeService employeeService,
            OperationLogRepository operationLogRepository) {
        this.cardRepository = cardRepository;
        this.memberRepository = memberRepository;
        this.logRepository = logRepository;
        this.cardLogRepository = cardLogRepository;
        this.employeeService = employeeService;
        this.operationLogRepository = operationLogRepository;
    }

    /**
     * 核心业务：判断会员是否可以进入门店，并记录日志
     */
    public String checkEntry(Long memberId, Long currentBranchId) {
        // 1. 初始化时给一个默认值，解决编译器的担忧
        String resultMessage = "准入拒绝：系统异常。";
        String status = "DENIED";

        if (!memberRepository.existsById(memberId)) {
            resultMessage = "准入拒绝：未找到 ID 为 " + memberId + " 的会员。";
        } else {
            List<MemberCard> cards = cardRepository.findByMemberId(memberId);

            if (cards == null || cards.isEmpty()) {
                resultMessage = "准入拒绝：该会员名下暂无会员卡。";
            } else {
                boolean foundValid = false;
                for (MemberCard card : cards) {
                    boolean isNotExpired = card.getExpireDate().isAfter(LocalDate.now());
                    boolean hasAccess = (card.getIsUniversal() != null && card.getIsUniversal())
                            || card.getHomeBranchId().equals(currentBranchId);

                    if (isNotExpired && hasAccess) {
                        resultMessage = "准入允许：欢迎光临！[卡号:" + card.getId() + "]";
                        status = "SUCCESS";
                        foundValid = true;
                        break;
                    }
                }

                if (!foundValid) {
                    resultMessage = "准入拒绝：卡片已过期或无当前门店权限。";
                }
            }
        }

        // 2. 现在无论走哪条路径，resultMessage 都有值了
        saveCheckInLog(memberId, currentBranchId, status, resultMessage);

        return resultMessage;
    }

    private void saveCheckInLog(Long memberId, Long branchId, String status, String reason) {
        CheckInLog log = new CheckInLog();
        log.setMemberId(memberId);
        log.setBranchId(branchId);
        log.setCheckInTime(LocalDateTime.now());
        log.setStatus(status);
        log.setReason(reason);
        logRepository.save(log);
    }

    /**
     * 修改会员卡有效期（延期或重置）
     * 
     * @param cardId        会员卡ID
     * @param newExpireDate 新的截止日期
     */
    @Transactional
    public MemberCard updateExpiryDate(Long cardId, LocalDate newExpireDate) {
        MemberCard card = cardRepository.findById(cardId)
                .orElseThrow(() -> new RuntimeException("未找到该会员卡"));
        card.setExpireDate(newExpireDate);
        return cardRepository.save(card);
    }

    /**
     * 前台提交续费申请 (按月数)
     */
    public MemberCardLog submitRenewalRequest(Long cardId, Long requesterId, Integer monthsToAdd) {
        MemberCard card = cardRepository.findById(cardId)
                .orElseThrow(() -> new RuntimeException("未找到该会员卡"));

        LocalDate baseDate = card.getExpireDate();
        // 如果当前没有有效期或已过期，从今天开始计算
        if (baseDate == null || baseDate.isBefore(LocalDate.now())) {
            baseDate = LocalDate.now();
        }

        LocalDate newDate = baseDate.plusMonths(monthsToAdd);
        return submitRenewalRequest(cardId, requesterId, newDate);
    }

    /**
     * 前台提交续费申请 (指定日期)
     */
    public MemberCardLog submitRenewalRequest(Long cardId, Long requesterId, LocalDate newDate) {
        MemberCard card = cardRepository.findById(cardId)
                .orElseThrow(() -> new RuntimeException("未找到该会员卡"));

        MemberCardLog log = new MemberCardLog();
        log.setCardId(cardId);
        log.setOldExpireDate(card.getExpireDate());
        log.setNewExpireDate(newDate);
        log.setRequesterId(requesterId);
        log.setStatus("PENDING");

        MemberCardLog savedLog = cardLogRepository.save(log);

        // 记录操作日志
        saveOperationLog(requesterId, "SUBMIT_RENEWAL", "MemberCardLog", savedLog.getId(),
                "提交续费申请：卡号 " + cardId + "，新有效期 " + newDate);

        return savedLog;
    }

    /**
     * 经理审批续费申请
     */
    @Transactional
    public MemberCardLog approveRenewalRequest(Long logId, Long approverId, String password) {
        employeeService.verifyPassword(approverId, password);
        MemberCardLog log = cardLogRepository.findById(logId)
                .orElseThrow(() -> new RuntimeException("未找到该申请记录"));

        if (!"PENDING".equals(log.getStatus())) {
            throw new RuntimeException("该申请已被处理");
        }

        // 1. 更新申请状态
        log.setApproverId(approverId);
        log.setStatus("APPROVED");
        cardLogRepository.save(log);

        // 2. 实际更新会员卡有效期
        updateExpiryDate(log.getCardId(), log.getNewExpireDate());

        // 记录操作日志
        saveOperationLog(approverId, "APPROVE_RENEWAL", "MemberCardLog", log.getId(),
                "批准续费：卡号 " + log.getCardId() + "，延期至 " + log.getNewExpireDate());

        return log;
    }

    /**
     * 经理撤销续费申请 (需密码)
     */
    @Transactional
    public void revokeRenewal(Long logId, Long approverId, String password) {
        // 1. 验证密码
        employeeService.verifyPassword(approverId, password);

        MemberCardLog log = cardLogRepository.findById(logId)
                .orElseThrow(() -> new RuntimeException("未找到该申请记录"));

        if (!"APPROVED".equals(log.getStatus())) {
            throw new RuntimeException("只能撤销已批准的申请");
        }

        // 2. 回滚会员卡有效期
        updateExpiryDate(log.getCardId(), log.getOldExpireDate());

        // 3. 更新日志状态
        log.setStatus("REVOKED");
        cardLogRepository.save(log);

        // 记录操作日志
        saveOperationLog(approverId, "REVOKE_RENEWAL", "MemberCardLog", log.getId(),
                "撤销续费：卡号 " + log.getCardId() + "，回滚至 " + log.getOldExpireDate());
    }

    private void saveOperationLog(Long operatorId, String type, String entity, Long targetId, String desc) {
        OperationLog opLog = new OperationLog();
        opLog.setOperatorId(operatorId);
        opLog.setOperationType(type);
        opLog.setTargetEntity(entity);
        opLog.setTargetId(targetId);
        opLog.setDescription(desc);
        operationLogRepository.save(opLog);
    }

}
