package com.gym.erp.staff.controller;

import com.gym.erp.staff.entity.Coach;
import com.gym.erp.staff.repository.CoachRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/coaches")
public class CoachRestController {

    private final CoachRepository coachRepository;

    public CoachRestController(CoachRepository coachRepository) {
        this.coachRepository = coachRepository;
    }

    /**
     * 根据门店ID获取教练列表
     */
    @GetMapping
    public ResponseEntity<List<Coach>> getCoachesByBranch(@RequestParam Long branchId) {
        List<Coach> coaches = coachRepository.findByHomeBranchId(branchId);
        return ResponseEntity.ok(coaches);
    }
}
