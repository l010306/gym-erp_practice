package com.gym.erp.component;

import com.gym.erp.staff.entity.Employee;
import com.gym.erp.staff.repository.EmployeeRepository;

import java.util.List;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class PasswordMigration implements CommandLineRunner {

    private final EmployeeRepository employeeRepository;

    public PasswordMigration(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        List<Employee> employees = employeeRepository.findAll();
        for (Employee emp : employees) {
            if (emp.getPassword() == null || emp.getPassword().isEmpty()) {
                emp.setPassword("123456"); // 默认密码
                employeeRepository.save(emp);
                System.out.println("Set default password for employee: " + emp.getName());
            }
        }
    }
}
