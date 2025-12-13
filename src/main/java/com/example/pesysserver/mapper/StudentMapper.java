package com.example.pesysserver.mapper;

import com.example.pesysserver.pojo.entity.Student;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

@Mapper
public interface StudentMapper {

    @Select("SELECT s.*, c.classId, c.className, " +
            "(SELECT t.name FROM teacher t WHERE t.classId = s.classId LIMIT 1) as teacherName " +
            "FROM student s " +
            "LEFT JOIN class c ON s.classId = c.classId " +
            "ORDER BY s.studentId")
    List<Map<String, Object>> findAllWithDetails();

    @Select("SELECT s.*, c.classId, c.className, " +
            "(SELECT t.name FROM teacher t WHERE t.classId = s.classId LIMIT 1) as teacherName " +
            "FROM student s " +
            "LEFT JOIN class c ON s.classId = c.classId " +
            "WHERE s.classId = #{classId} " +
            "ORDER BY s.studentId")
    List<Map<String, Object>> findByClassId(Integer classId);

    @Select("SELECT * FROM student WHERE studentId = #{studentId}")
    Student findById(Integer studentId);

    @Insert("INSERT INTO student (studentId, name, password, classId) VALUES " +
            "(#{studentId}, #{name}, #{password}, #{classId})")
    int insert(Student student);

    @Update("UPDATE student SET name = #{name}, classId = #{classId} WHERE studentId = #{studentId}")
    int update(Student student);

    @Delete("DELETE FROM student WHERE studentId = #{studentId}")
    int deleteById(Integer studentId);

    @Select("SELECT COUNT(*) FROM student WHERE classId = #{classId}")
    int countByClassId(Integer classId);

    @Select("SELECT s.* FROM student s " +
            "LEFT JOIN exams e ON s.classId = e.classId " +
            "WHERE s.classId = #{classId} AND e.examId = #{examId}")
    List<Map<String, Object>> findStudentsByExam(Integer classId, Integer examId);

    // 批量插入学生
    @Insert("<script>" +
            "INSERT INTO student (studentId, name, password, classId) VALUES " +
            "<foreach collection='students' item='student' separator=','>" +
            "(#{student.studentId}, #{student.name}, #{student.password}, #{student.classId})" +
            "</foreach>" +
            "</script>")
    int batchInsert(@Param("students") List<Student> students);
}