package com.example.pesysserver.service;

import com.example.pesysserver.pojo.dto.DashboardDTO;
import com.example.pesysserver.pojo.dto.ExamTaskCreateDTO;
import com.example.pesysserver.pojo.entity.ClassInfo;
import com.example.pesysserver.pojo.entity.ExamReport;
import com.example.pesysserver.pojo.entity.ExamTask;
import com.example.pesysserver.pojo.entity.StudentScore;
import com.example.pesysserver.pojo.entity.VideoFile;
import com.example.pesysserver.pojo.vo.ClassDetailVO;

import java.util.List;
import java.util.Map;

public interface AdminService {

    // 班级管理
    List<Map<String, Object>> getAllClassesWithDetails();
    Map<String, Object> getClassDetail(Integer classId);
    List<Map<String, Object>> getClassStudents(Integer classId);
    List<Map<String, Object>> getExamStudents(Integer classId, Integer examId);
    void createClass(Map<String, Object> classData);
    void updateClass(Integer classId, Map<String, Object> classData);
    void deleteClass(Integer classId);

    // 教师管理
    List<Map<String, Object>> getAllTeachers();

    // 考试任务管理
    List<ExamTask> getAllExamTasks();
    ExamTask getExamTaskById(Integer taskId);
    ExamTask createExamTask(ExamTaskCreateDTO examTaskCreateDTO, String createdBy);
    void deleteExamTask(Integer taskId);

    // 考试成绩管理
    ExamReport getExamReport(Integer examId);
    void batchSaveScores(List<StudentScore> scores);
    List<StudentScore> getClassScores(Integer classId);

    // 学生管理
    List<Map<String, Object>> getAllStudents();
    void createStudent(Map<String, Object> studentData);
    void updateStudent(Integer studentId, Map<String, Object> studentData);
    void deleteStudent(Integer studentId);
    void batchCreateStudents(List<Map<String, Object>> students);

    // 视频管理
    List<VideoFile> getVideosByCondition(Integer classId, String analysisStatus);
    VideoFile getVideoById(Integer videoId);
    void analyzeVideo(Integer videoId);
    void batchAnalyzeVideos(List<Integer> videoIds);

    // 数据统计
    DashboardDTO getDashboardStatistics();
    Map<String, Object> getClassStatistics(Integer classId);
}
