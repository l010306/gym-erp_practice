package com.gym.erp.component;

import com.gym.erp.membership.entity.Member;
import com.gym.erp.membership.repository.MemberRepository;
import com.gym.erp.staff.entity.Coach;
import com.gym.erp.staff.entity.Employee;
import com.gym.erp.staff.repository.CoachRepository;
import com.gym.erp.staff.repository.EmployeeRepository;
import com.gym.erp.training.entity.Course;
import com.gym.erp.training.entity.CourseType;
import com.gym.erp.training.repository.CourseRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/admin/data")
public class AdminController {

    private final MemberRepository memberRepository;
    private final EmployeeRepository employeeRepository;
    private final CourseRepository courseRepository;
    private final CoachRepository coachRepository;

    public AdminController(MemberRepository memberRepository,
                           EmployeeRepository employeeRepository,
                           CourseRepository courseRepository,
                           CoachRepository coachRepository) {
        this.memberRepository = memberRepository;
        this.employeeRepository = employeeRepository;
        this.courseRepository = courseRepository;
        this.coachRepository = coachRepository;
    }

    @GetMapping
    public String index(Model model) {
        model.addAttribute("members", memberRepository.findAll());
        model.addAttribute("employees", employeeRepository.findAll());
        model.addAttribute("courses", courseRepository.findAll());
        return "admin/data";
    }

    @PostMapping("/seed")
    public String seedData() {
        // 1. Seed Members
        if (memberRepository.count() == 0) {
            Member m1 = new Member();
            m1.setName("张伟");
            m1.setPhone("13812345678");
            m1.setGender("MALE");
            memberRepository.save(m1);

            Member m2 = new Member();
            m2.setName("王芳");
            m2.setPhone("13987654321");
            m2.setGender("FEMALE");
            memberRepository.save(m2);
        }

        // 2. Seed Employees
        Employee e1 = null;
        if (employeeRepository.count() == 0) {
            e1 = new Employee();
            e1.setName("李华 (经理)");
            e1.setPhone("13300000001");
            e1.setPassword("123456");
            e1.setPosition("MANAGER");
            e1.setPermissions("ALL");
            e1 = employeeRepository.save(e1);
        }

        // 3. Seed Coaches
        Coach coach = null;
        if (coachRepository.count() == 0) {
            coach = new Coach();
            coach.setName("王大锤");
            coach.setSpecialization("增肌、力量训练");
            coach.setPhone("13500009999");
            coach = coachRepository.save(coach);
        } else {
            coach = coachRepository.findAll().get(0);
        }

        // 4. Seed Courses
        if (courseRepository.count() == 0 && coach != null) {
            Course c1 = new Course();
            c1.setName("零基础增肌入门");
            c1.setType(CourseType.PERSONAL);
            c1.setStartTime(LocalDateTime.now().plusHours(2));
            c1.setEndTime(LocalDateTime.now().plusHours(3));
            c1.setCoach(coach);
            c1.setMaxCapacity(1);
            if (e1 != null) c1.setCreatedBy(e1);
            courseRepository.save(c1);

            Course c2 = new Course();
            c2.setName("动感单车燃脂");
            c2.setType(CourseType.GROUP);
            c2.setStartTime(LocalDateTime.now().plusHours(4));
            c2.setEndTime(LocalDateTime.now().plusHours(5));
            c2.setCoach(coach);
            c2.setMaxCapacity(30);
            if (e1 != null) c2.setCreatedBy(e1);
            courseRepository.save(c2);
        }

        return "redirect:/admin/data?seeded=true";
    }

    @PostMapping("/delete-member/{id}")
    public String deleteMember(@PathVariable Long id) {
        memberRepository.deleteById(id);
        return "redirect:/admin/data";
    }

    @PostMapping("/delete-employee/{id}")
    public String deleteEmployee(@PathVariable Long id) {
        employeeRepository.deleteById(id);
        return "redirect:/admin/data";
    }

    @PostMapping("/delete-course/{id}")
    public String deleteCourse(@PathVariable Long id) {
        courseRepository.deleteById(id);
        return "redirect:/admin/data";
    }
}
