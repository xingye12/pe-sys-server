package com.example.pesysserver.mapper;

import com.example.pesysserver.pojo.dto.StudentQueryDTO;
import com.example.pesysserver.pojo.entity.*;
import com.example.pesysserver.pojo.vo.ClassOverviewVO;
import com.example.pesysserver.pojo.vo.StudentScoreVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface TeacherMapper {
    @Select("select * from teacher")
    List<TeacherEntity> getAllTeachers();
    @Select("select * from student where classId = #{classId}")
    List<StudentEntity> getClassStudents(int classId);

    List<Exam> getClassExam(Exam exam);
    @Select("select * from exams where studentId = #{studentId}")
    List<Exam> getStudentExams(int studentId);
    @Select("select * from report where studentId = #{studentId}")
    List<Report> getStudentReports(int studentId);
    @Select("select * from report where studentId = #{studentId} and examId = #{examId}" )
    List<Report> getStudentExamReport(Report report);
    @Select("SELECT \n" +
            "    r.*\n" +
            "FROM report r\n" +
            "WHERE r.studentId IN (\n" +
            "    SELECT id \n" +
            "    FROM student \n" +
            "    WHERE classId = #{classId}\n" +
            ")\n" )
    List<Report> getClassReports(int classId);
    @Select("SELECT * FROM teacher WHERE username = #{username} AND password = #{password}")
    TeacherEntity verifyTeacherCredentials(@Param("username") String username, @Param("password") String password);

    // 获取教师的班级列表
    @Select("SELECT c.*, t.name as teacher_name " +
            "FROM class c " +
            "JOIN teacher_class tc ON c.classId = tc.class_id " +
            "JOIN teacher t ON tc.teacher_id = t.teacherId " +
            "WHERE tc.teacher_id = #{teacherId} AND tc.status = 'active'")
    List<ClassInfo> getTeacherClasses(@Param("teacherId") Integer teacherId);

    // 获取学生成绩列表（支持查询和分页）
    List<StudentScoreVO> getStudentScores(@Param("dto") StudentQueryDTO dto, @Param("offset") Integer offset, @Param("limit") Integer limit);

    // 获取学生成绩总数
    Integer getStudentScoresCount(@Param("dto") StudentQueryDTO dto);

    // 获取学生详细信息
    @Select("SELECT s.*, c.className " +
            "FROM student s " +
            "JOIN class c ON s.classId = c.classId " +
            "WHERE s.studentId = #{studentId}")
    StudentEntity getStudentDetail(@Param("studentId") Integer studentId);

    // 获取学生的所有成绩
    @Select("SELECT sc.*, c.className, e.exam_name as examName " +
            "FROM student_scores sc " +
            "JOIN class c ON sc.class_id = c.classId " +
            "JOIN exams e ON sc.exam_id = e.examId " +
            "WHERE sc.student_id = #{studentId} " +
            "ORDER BY sc.exam_time DESC")
    List<StudentScore> getStudentAllScores(@Param("studentId") Integer studentId);

    // 获取班级概况统计
    @Select("SELECT " +
            "    c.classId, " +
            "    c.className, " +
            "    c.grade, " +
            "    COUNT(DISTINCT s.studentId) as totalStudents, " +
            "    ROUND(AVG(sc.score), 2) as avgScore, " +
            "    ROUND(SUM(CASE WHEN sc.score >= 60 THEN 1 ELSE 0 END) * 100.0 / COUNT(sc.score), 2) as passRate, " +
            "    ROUND(SUM(CASE WHEN sc.score >= 90 THEN 1 ELSE 0 END) * 100.0 / COUNT(sc.score), 2) as excellentRate, " +
            "    COUNT(DISTINCT e.examId) as examCount, " +
            "    MAX(e.begin_time) as latestExamTime, " +
            "    t.name as teacherName " +
            "FROM class c " +
            "JOIN teacher_class tc ON c.classId = tc.class_id " +
            "JOIN teacher t ON tc.teacher_id = t.teacherId " +
            "LEFT JOIN student s ON c.classId = s.classId " +
            "LEFT JOIN student_scores sc ON s.studentId = sc.student_id " +
            "LEFT JOIN exams e ON sc.exam_id = e.examId " +
            "WHERE tc.teacher_id = #{teacherId} AND tc.status = 'active' " +
            "GROUP BY c.classId, c.className, c.grade, t.name")
    List<ClassOverviewVO> getTeacherClassOverview(@Param("teacherId") Integer teacherId);

    // 获取班级统计详情
    @Select("SELECT * FROM class_statistics WHERE class_id = #{classId} AND exam_id = #{examId}")
    ClassStatistics getClassStatistics(@Param("classId") Integer classId, @Param("examId") Integer examId);

    // 获取班级最新考试成绩统计
    @Select("SELECT " +
            "    sc.class_id, " +
            "    c.className, " +
            "    COUNT(DISTINCT sc.student_id) as totalStudents, " +
            "    ROUND(AVG(sc.score), 2) as avgScore, " +
            "    MAX(sc.score) as maxScore, " +
            "    MIN(sc.score) as minScore, " +
            "    SUM(CASE WHEN sc.score >= 60 THEN 1 ELSE 0 END) as passCount, " +
            "    SUM(CASE WHEN sc.score >= 90 THEN 1 ELSE 0 END) as excellentCount, " +
            "    ROUND(SUM(CASE WHEN sc.score >= 60 THEN 1 ELSE 0 END) * 100.0 / COUNT(sc.score), 2) as passRate, " +
            "    ROUND(SUM(CASE WHEN sc.score >= 90 THEN 1 ELSE 0 END) * 100.0 / COUNT(sc.score), 2) as excellentRate, " +
            "    SUM(CASE WHEN sc.score >= 90 THEN 1 ELSE 0 END) as count90Plus, " +
            "    SUM(CASE WHEN sc.score >= 80 AND sc.score < 90 THEN 1 ELSE 0 END) as count80To89, " +
            "    SUM(CASE WHEN sc.score >= 70 AND sc.score < 80 THEN 1 ELSE 0 END) as count70To79, " +
            "    SUM(CASE WHEN sc.score >= 60 AND sc.score < 70 THEN 1 ELSE 0 END) as count60To69, " +
            "    SUM(CASE WHEN sc.score < 60 THEN 1 ELSE 0 END) as countBelow60 " +
            "FROM student_scores sc " +
            "JOIN class c ON sc.class_id = c.classId " +
            "WHERE sc.class_id = #{classId} " +
            "GROUP BY sc.class_id, c.className")
    ClassStatistics getClassScoreStatistics(@Param("classId") Integer classId);

}
