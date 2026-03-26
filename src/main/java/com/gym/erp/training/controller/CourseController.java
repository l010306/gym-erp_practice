package com.gym.erp.training.controller;

import com.gym.erp.training.entity.Course;
import com.gym.erp.training.entity.CourseBooking;
import com.gym.erp.training.service.CourseBookingService;
import com.gym.erp.training.service.CourseService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    private final CourseService courseService;
    private final CourseBookingService bookingService;

    public CourseController(CourseService courseService,
            CourseBookingService bookingService) {
        this.courseService = courseService;
        this.bookingService = bookingService;
    }

    // ========== 课程管理接口（供前台员工使用）==========

    /**
     * 获取所有课程
     */
    @GetMapping
    public List<Course> getAllCourses() {
        return courseService.getAllCourses();
    }

    /**
     * 根据ID获取课程详情
     */
    @GetMapping("/{id}")
    public Course getCourseById(@PathVariable Long id) {
        return courseService.getCourseById(id);
    }

    /**
     * 创建新课程
     */
    @PostMapping
    public ResponseEntity<?> createCourse(@RequestBody Course course,
            @RequestParam(required = false) Long employeeId) {
        try {
            Course created = courseService.createCourse(course, employeeId);
            return ResponseEntity.ok(created);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * 更新课程信息（带权限验证）
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCourse(
            @PathVariable Long id,
            @RequestBody Course course,
            @RequestParam Long employeeId) {
        try {
            Course updated = courseService.updateCourse(id, course, employeeId);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            return ResponseEntity.status(403)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * 删除课程（带权限验证）
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCourse(
            @PathVariable Long id,
            @RequestParam Long employeeId) {
        try {
            courseService.deleteCourse(id, employeeId);
            return ResponseEntity.ok(Map.of("message", "课程删除成功"));
        } catch (Exception e) {
            return ResponseEntity.status(403)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // ========== 预约管理接口（供会员使用）==========

    /**
     * 会员预约课程
     */
    @PostMapping("/{courseId}/book")
    public ResponseEntity<?> bookCourse(@PathVariable Long courseId, @RequestParam Long memberId) {
        try {
            CourseBooking booking = bookingService.bookCourse(courseId, memberId);
            return ResponseEntity.ok(booking);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * 取消预约
     */
    @PostMapping("/bookings/{bookingId}/cancel")
    public ResponseEntity<?> cancelBooking(@PathVariable Long bookingId, @RequestParam Long memberId) {
        try {
            bookingService.cancelBooking(bookingId, memberId);
            return ResponseEntity.ok(Map.of("message", "预约已取消"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * 获取会员的所有预约
     */
    @GetMapping("/bookings/member/{memberId}")
    public List<CourseBooking> getMemberBookings(@PathVariable Long memberId) {
        return bookingService.getMemberBookings(memberId);
    }

    /**
     * 获取课程的预约情况（包含已预约人数）
     */
    @GetMapping("/{courseId}/bookings")
    public ResponseEntity<?> getCourseBookingInfo(@PathVariable Long courseId) {
        Course course = courseService.getCourseById(courseId);
        Long bookedCount = bookingService.getCourseBookingCount(courseId);
        List<CourseBooking> bookings = bookingService.getCourseBookings(courseId);

        Map<String, Object> info = new HashMap<>();
        info.put("course", course);
        info.put("bookedCount", bookedCount);
        info.put("maxCapacity", course.getMaxCapacity());
        info.put("availableSlots", course.getMaxCapacity() - bookedCount);
        info.put("bookings", bookings);

        return ResponseEntity.ok(info);
    }

    /**
     * 获取会员可见的课程（根据会员所属门店筛选）
     */
    @GetMapping("/member/{memberId}")
    public ResponseEntity<?> getCoursesForMember(
            @PathVariable Long memberId,
            @RequestParam(required = false) String type) {
        try {
            List<Course> courses = courseService.getCoursesForMember(memberId, type);
            return ResponseEntity.ok(courses);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
