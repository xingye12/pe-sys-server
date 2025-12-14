package com.example.pesysserver.service.impl;

import com.example.pesysserver.mapper.TeacherMapper;
import com.example.pesysserver.pojo.dto.StudentQueryDTO;
import com.example.pesysserver.pojo.entity.*;
import com.example.pesysserver.pojo.vo.ClassOverviewVO;
import com.example.pesysserver.pojo.vo.StudentScoreVO;
import com.example.pesysserver.service.TeacherService;
import com.example.pesysserver.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TeacherServiceImpl implements TeacherService {
    @Autowired
    TeacherMapper teacherMapper;
    @Override
    public List<TeacherEntity> getAllTeachers() {
        return teacherMapper.getAllTeachers();
    }

    @Override
    public List<StudentEntity> getClassStudents(int classId) {
        return teacherMapper.getClassStudents(classId);
    }

    @Override
    public List<Report> getClassReports(int classId) {
        return teacherMapper.getClassReports(classId);
    }

    @Override
    public List<Report> getStudentReports(int studentId) {
        return teacherMapper.getStudentReports(studentId);
    }

    @Override
    public List<Exam> getClassExams(Exam exam) {
        return teacherMapper.getClassExam(exam);
    }


    @Override
    public List<Exam> getStudentExams(int studentId) {

        return teacherMapper.getStudentExams(studentId);
    }

    // 新增教师模块功能实现
    @Override
    public List<ClassInfo> getTeacherClasses(Integer teacherId) {
        return teacherMapper.getTeacherClasses(teacherId);
    }

    @Override
    public Result getStudentScores(StudentQueryDTO dto) {
        try {
            int offset = (dto.getPage() - 1) * dto.getSize();
            List<StudentScoreVO> list = teacherMapper.getStudentScores(dto, offset, dto.getSize());
            int total = teacherMapper.getStudentScoresCount(dto);

            Map<String, Object> result = new HashMap<>();
            result.put("list", list);
            result.put("total", total);
            result.put("page", dto.getPage());
            result.put("size", dto.getSize());

            return Result.success(result);
        } catch (Exception e) {
            return Result.error("获取学生成绩列表失败：" + e.getMessage());
        }
    }

    @Override
    public StudentEntity getStudentDetail(Integer studentId) {
        return teacherMapper.getStudentDetail(studentId);
    }

    @Override
    public List<StudentScore> getStudentAllScores(Integer studentId) {
        return teacherMapper.getStudentAllScores(studentId);
    }

    @Override
    public List<ClassOverviewVO> getTeacherClassOverview(Integer teacherId) {
        return teacherMapper.getTeacherClassOverview(teacherId);
    }

    @Override
    public ClassStatistics getClassStatistics(Integer classId, Integer examId) {
        return teacherMapper.getClassStatistics(classId, examId);
    }

    @Override
    public ClassStatistics getClassScoreStatistics(Integer classId) {
        return teacherMapper.getClassScoreStatistics(classId);
    }
}
