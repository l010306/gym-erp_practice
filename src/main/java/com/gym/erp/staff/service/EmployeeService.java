package com.gym.erp.staff.service;

import com.gym.erp.staff.entity.Employee;
import com.gym.erp.staff.repository.EmployeeRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public void verifyPassword(Long employeeId, String password) {
        Employee emp = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("员工不存在"));

        if (emp.getPassword() == null || !emp.getPassword().equals(password)) {
            throw new RuntimeException("密码错误");
        }
    }

    @Transactional
    public void changePassword(Long employeeId, String oldPassword, String newPassword) {
        verifyPassword(employeeId, oldPassword);
        Employee emp = employeeRepository.findById(employeeId).get();
        emp.setPassword(newPassword);
        employeeRepository.save(emp);
    }
}
