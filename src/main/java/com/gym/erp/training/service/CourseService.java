package com.gym.erp.training.service;

import com.gym.erp.membership.repository.MemberRepository;
import com.gym.erp.staff.entity.Employee;
import com.gym.erp.staff.repository.EmployeeRepository;
import com.gym.erp.training.entity.Course;
import com.gym.erp.training.repository.CourseRepository;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CourseService {

    private final CourseRepository courseRepository;
    private final EmployeeRepository employeeRepository;
    private final MemberRepository memberRepository;

    public CourseService(CourseRepository courseRepository,
            EmployeeRepository employeeRepository,
            MemberRepository memberRepository) {
        this.courseRepository = courseRepository;
        this.employeeRepository = employeeRepository;
        this.memberRepository = memberRepository;
    }

    /**
     * 获取所有课程（按时间排序：本周优先，其他按时间顺序）
     */
    public List<Course> getAllCourses() {
        List<Course> courses = courseRepository.findAll();

        // 计算本周开始和结束时间
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime weekStart = now.with(DayOfWeek.MONDAY).truncatedTo(ChronoUnit.DAYS);
        LocalDateTime weekEnd = weekStart.plusWeeks(1);

        // 排序逻辑：本周课程优先，之后按时间排序
        return courses.stream()
                .sorted((c1, c2) -> {
                    boolean c1ThisWeek = isInTimeRange(c1.getStartTime(), weekStart, weekEnd);
                    boolean c2ThisWeek = isInTimeRange(c2.getStartTime(), weekStart, weekEnd);

                    // 本周的课程排在前面
                    if (c1ThisWeek && !c2ThisWeek)
                        return -1;
                    if (!c1ThisWeek && c2ThisWeek)
                        return 1;

                    // 同类型的按开始时间排序
                    return c1.getStartTime().compareTo(c2.getStartTime());
                })
                .collect(Collectors.toList());
    }

    /**
     * 判断时间是否在指定范围内
     */
    private boolean isInTimeRange(LocalDateTime time, LocalDateTime start, LocalDateTime end) {
        return !time.isBefore(start) && time.isBefore(end);
    }

    /**
     * 根据ID获取课程
     */
    public Course getCourseById(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("未找到课程 ID: " + id));
    }

    /**
     * 创建新课程
     * 
     * @param course     课程信息
     * @param employeeId 创建课程的员工ID
     */
    @Transactional
    public Course createCourse(Course course, Long employeeId) {
        // 验证课程时间
        if (course.getStartTime() == null || course.getEndTime() == null) {
            throw new RuntimeException("课程开始和结束时间不能为空");
        }
        if (course.getEndTime().isBefore(course.getStartTime())) {
            throw new RuntimeException("课程结束时间不能早于开始时间");
        }
        if (course.getStartTime().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("课程开始时间不能早于当前时间");
        }

        // 验证员工存在
        if (employeeId != null) {
            Employee employee = employeeRepository.findById(employeeId)
                    .orElseThrow(() -> new RuntimeException("未找到员工 ID: " + employeeId));
            course.setCreatedBy(employee);
        }

        return courseRepository.save(course);
    }

    /**
     * 更新课程（带权限验证）
     */
    @Transactional
    public Course updateCourse(Long id, Course updatedCourse, Long employeeId) {
        // 验证权限
        validateCoursePermission(id, employeeId);

        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("课程不存在 ID: " + id));

        // 更新字段
        if (updatedCourse.getName() != null) {
            course.setName(updatedCourse.getName());
        }
        if (updatedCourse.getDescription() != null) {
            course.setDescription(updatedCourse.getDescription());
        }
        if (updatedCourse.getStartTime() != null) {
            course.setStartTime(updatedCourse.getStartTime());
        }
        if (updatedCourse.getEndTime() != null) {
            course.setEndTime(updatedCourse.getEndTime());
        }

        if (updatedCourse.getMaxCapacity() != null) {
            course.setMaxCapacity(updatedCourse.getMaxCapacity());
        }
        if (updatedCourse.getType() != null) {
            course.setType(updatedCourse.getType());
        }

        // 验证时间有效性
        if (course.getStartTime() == null || course.getEndTime() == null) {
            throw new RuntimeException("课程开始和结束时间不能为空");
        }
        if (course.getStartTime().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("课程开始时间不能早于当前时间");
        }
        if (course.getEndTime().isBefore(course.getStartTime())) {
            throw new RuntimeException("课程结束时间必须晚于开始时间");
        }

        return courseRepository.save(course);
    }

    /**
     * 删除课程（带权限验证）
     */
    @Transactional
    public void deleteCourse(Long id, Long employeeId) {
        // 验证权限
        validateCoursePermission(id, employeeId);

        if (!courseRepository.existsById(id)) {
            throw new RuntimeException("课程不存在 ID: " + id);
        }
        courseRepository.deleteById(id);
    }

    /**
     * 验证员工是否有权限修改课程
     */
    private void validateCoursePermission(Long courseId, Long employeeId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("课程不存在"));

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("员工不存在"));

        if (!course.getBranchId().equals(employee.getBranchId())) {
            throw new RuntimeException("无权限：您只能修改本门店的课程");
        }
    }

    /**
     * 根据教练ID获取课程
     */
    public List<Course> getCoursesByCoachId(Long coachId) {
        return courseRepository.findByCoachId(coachId);
    }

    /**
     * 根据门店ID获取课程
     */
    public List<Course> getCoursesByBranchId(Long branchId) {
        return courseRepository.findByBranchId(branchId);
    }

    /**
     * 获取会员可见的课程（根据会员所属门店筛选）
     */
    public List<Course> getCoursesForMember(Long memberId, String typeStr) {
        // 1. 查询会员信息
        com.gym.erp.membership.entity.Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("未找到会员 ID: " + memberId));

        // 2. 获取会员所属门店
        Long branchId = member.getBranchId();
        if (branchId == null) {
            throw new RuntimeException("会员未设置所属门店");
        }

        // 3. 根据门店和类型筛选课程
        if (typeStr != null && !typeStr.isEmpty()) {
            try {
                com.gym.erp.training.entity.CourseType type = com.gym.erp.training.entity.CourseType
                        .valueOf(typeStr.toUpperCase());
                return courseRepository.findByBranchIdAndType(branchId, type);
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("无效的课程类型: " + typeStr);
            }
        } else {
            return courseRepository.findByBranchId(branchId);
        }
    }
}
