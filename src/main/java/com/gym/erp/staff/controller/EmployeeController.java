package com.gym.erp.staff.controller;

import com.gym.erp.staff.service.EmployeeService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping("/change-password")
    public String showChangePasswordForm(Model model, @RequestParam(required = false) String error,
            @RequestParam(required = false) String success) {
        model.addAttribute("error", error);
        model.addAttribute("success", success);
        return "employee/change_password";
    }

    @PostMapping("/change-password")
    public String processChangePassword(
            @RequestParam Long employeeId,
            @RequestParam String oldPassword,
            @RequestParam String newPassword,
            RedirectAttributes redirectAttributes) {

        try {
            employeeService.changePassword(employeeId, oldPassword, newPassword);
            redirectAttributes.addAttribute("success", "密码修改成功！");
        } catch (RuntimeException e) {
            redirectAttributes.addAttribute("error", e.getMessage());
        }
        return "redirect:/employees/change-password";
    }
}
