package com.example.pesysserver.mapper;

import com.example.pesysserver.pojo.entity.ExamTask;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ExamTaskMapper {

    @Select("SELECT e.*, c.classId as classId FROM exams e " +
            "LEFT JOIN class c ON e.examId = c.examId " +
            "ORDER BY e.examId DESC")
    List<ExamTask> findAll();

    @Select("SELECT * FROM exams WHERE examId = #{examId}")
    ExamTask findById(Integer examId);

    @Insert("INSERT IGNORE INTO exams (examId, type, description, classId, begin_time, end_time, status) " +
            "VALUES (#{examId}, #{type}, #{description}, #{classId}, #{begin_time}, #{end_time}, #{status})")
    int insert(ExamTask examTask);

    @Update("UPDATE exams SET type = #{type}, description = #{description}, " +
            "classId = #{classId}, begin_time = #{begin_time}, end_time = #{end_time}, " +
            "status = #{status} WHERE examId = #{examId}")
    int update(ExamTask examTask);

    @Delete("DELETE FROM exams WHERE examId = #{examId}")
    int deleteById(Integer examId);

    @Select("SELECT COUNT(*) FROM exams")
    int countExams();

    // 添加一些示例数据
    @Insert("INSERT IGNORE INTO exams (examId, type, description, classId, status) VALUES " +
            "(1, '800米跑', '第一次体育测试', 1, 'COMPLETED'), " +
            "(2, '立定跳远', '第一次体育测试', 1, 'COMPLETED'), " +
            "(3, '仰卧起坐', '第一次体育测试', 1, 'PENDING')")
    int insertSampleData();
}