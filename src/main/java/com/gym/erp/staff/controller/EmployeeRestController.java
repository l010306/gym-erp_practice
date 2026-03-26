package com.gym.erp.staff.controller;

import com.gym.erp.staff.entity.Employee;
import com.gym.erp.staff.repository.EmployeeRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/employees")
public class EmployeeRestController {

    private final EmployeeRepository employeeRepository;

    public EmployeeRestController(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    /**
     * 根据ID获取员工信息
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getEmployeeById(@PathVariable Long id) {
        return employeeRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * 员工登录验证（ID + 密码）
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        try {
            Long employeeId = Long.parseLong(credentials.get("employeeId"));
            String password = credentials.get("password");

            Employee employee = employeeRepository.findById(employeeId)
                    .orElseThrow(() -> new RuntimeException("员工不存在"));

            // 验证密码
            if (employee.getPassword() == null || !employee.getPassword().equals(password)) {
                throw new RuntimeException("密码错误");
            }

            // 返回员工信息（不包含密码）
            employee.setPassword(null);
            return ResponseEntity.ok(employee);
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "员工ID格式错误"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }
}
