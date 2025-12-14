package com.example.pesysserver.controller.teacher;

import com.example.pesysserver.pojo.dto.StudentQueryDTO;
import com.example.pesysserver.pojo.entity.Exam;
import com.example.pesysserver.pojo.entity.Student;
import com.example.pesysserver.pojo.entity.Teacher;
import com.example.pesysserver.pojo.entity.TeacherEntity;
import com.example.pesysserver.service.TeacherService;
import com.example.pesysserver.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/teacher")
public class TeacherController {

    // 注入JdbcTemplate（Spring会自动创建Bean，前提是数据库配置正确+引入spring-jdbc依赖）
    @Autowired
    private TeacherService teacherService;

    @GetMapping("/all")
    @ResponseBody
    public List<TeacherEntity> getAllTeachers() {
        return teacherService.getAllTeachers();
    }

    @GetMapping("/class/students")
    @ResponseBody
    public Result getClassStudent(@RequestParam Integer classId){

        return Result.success(teacherService.getClassStudents(classId));
    }
    @GetMapping("/student_report")
    @ResponseBody
    public Result getStudentReport(@RequestParam Integer studentId){

        return Result.success(teacherService.getStudentReports(studentId));
    }
    @GetMapping("/class_reports")
    @ResponseBody
    public Result getClassReports(@RequestParam Integer classId){

        return Result.success(teacherService.getClassReports(classId));
    }
    @GetMapping("/class_exams")
    @ResponseBody
    public Result getClassExams(Exam exam){
        System.out.println(teacherService.getClassExams(exam));
        return Result.success(teacherService.getClassExams(exam));
    }
    @GetMapping("/student_exams")
    @ResponseBody
    public Result getStudentExams(@RequestParam Integer studentId){
        return Result.success(teacherService.getStudentExams(studentId));
    }

    // 新增教师模块API接口
    @GetMapping("/classes")
    @ResponseBody
    public Result getTeacherClasses(@RequestParam Integer teacherId) {
        try {
            return Result.success(teacherService.getTeacherClasses(teacherId));
        } catch (Exception e) {
            return Result.error("获取教师班级列表失败：" + e.getMessage());
        }
    }

    @GetMapping("/students/scores")
    @ResponseBody
    public Result getStudentScores(StudentQueryDTO dto) {
        return teacherService.getStudentScores(dto);
    }

    @GetMapping("/students/{studentId}")
    @ResponseBody
    public Result getStudentDetail(@PathVariable Integer studentId) {
        try {
            return Result.success(teacherService.getStudentDetail(studentId));
        } catch (Exception e) {
            return Result.error("获取学生详情失败：" + e.getMessage());
        }
    }

    @GetMapping("/students/{studentId}/scores")
    @ResponseBody
    public Result getStudentAllScores(@PathVariable Integer studentId) {
        try {
            return Result.success(teacherService.getStudentAllScores(studentId));
        } catch (Exception e) {
            return Result.error("获取学生成绩失败：" + e.getMessage());
        }
    }

    @GetMapping("/class/overview")
    @ResponseBody
    public Result getTeacherClassOverview(@RequestParam Integer teacherId) {
        try {
            return Result.success(teacherService.getTeacherClassOverview(teacherId));
        } catch (Exception e) {
            return Result.error("获取班级概况失败：" + e.getMessage());
        }
    }

    @GetMapping("/class/statistics")
    @ResponseBody
    public Result getClassStatistics(@RequestParam Integer classId, @RequestParam(required = false) Integer examId) {
        try {
            if (examId != null) {
                return Result.success(teacherService.getClassStatistics(classId, examId));
            } else {
                return Result.success(teacherService.getClassScoreStatistics(classId));
            }
        } catch (Exception e) {
            return Result.error("获取班级统计失败：" + e.getMessage());
        }
    }

    @GetMapping("/export/class")
    @ResponseBody
    public Result exportClassData(@RequestParam Integer classId) {
        try {
            // TODO: 实现导出功能
            return Result.success("导出功能开发中");
        } catch (Exception e) {
            return Result.error("导出失败：" + e.getMessage());
        }
    }

    @GetMapping("/export/student/{studentId}")
    @ResponseBody
    public Result exportStudentData(@PathVariable Integer studentId) {
        try {
            // TODO: 实现导出功能
            return Result.success("导出功能开发中");
        } catch (Exception e) {
            return Result.error("导出失败：" + e.getMessage());
        }
    }
}