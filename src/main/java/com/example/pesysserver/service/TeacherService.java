package com.example.pesysserver.service;

import com.example.pesysserver.pojo.dto.StudentQueryDTO;
import com.example.pesysserver.pojo.entity.*;
import com.example.pesysserver.pojo.vo.ClassOverviewVO;
import com.example.pesysserver.pojo.vo.StudentScoreVO;
import com.example.pesysserver.utils.Result;

import java.util.List;

public interface TeacherService {
    List<TeacherEntity> getAllTeachers();

    List<StudentEntity> getClassStudents(int classId);

    List<Report> getClassReports(int classId);
    List<Report> getStudentReports(int studentId);

    List<Exam> getClassExams(Exam exam);

    List<Exam> getStudentExams(int studentId);

    // 新增教师模块功能接口
    List<ClassInfo> getTeacherClasses(Integer teacherId);

    Result getStudentScores(StudentQueryDTO dto);

    StudentEntity getStudentDetail(Integer studentId);

    List<StudentScore> getStudentAllScores(Integer studentId);

    List<ClassOverviewVO> getTeacherClassOverview(Integer teacherId);

    ClassStatistics getClassStatistics(Integer classId, Integer examId);

    ClassStatistics getClassScoreStatistics(Integer classId);
}
