package com.example.pesysserver.mapper;

import com.example.pesysserver.pojo.entity.ExamTask;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ExamTaskMapper {

    @Select("SELECT e.examId, e.type, e.exam_name as examName, e.description, e.classId, e.begin_time, e.end_time, e.state FROM exams e " +
            "ORDER BY e.examId DESC")
    List<ExamTask> findAll();

    @Select("SELECT examId, type, exam_name as examName, description, classId, begin_time, end_time, state FROM exams WHERE examId = #{examId}")
    ExamTask findById(Integer examId);

    @Insert("INSERT IGNORE INTO exams (examId, type, description, classId, begin_time, end_time, state, exam_name) " +
            "VALUES (#{examId}, #{type}, #{description}, #{classId}, #{begin_time}, #{end_time}, #{state}, #{examName})")
    int insert(ExamTask examTask);

    @Update("UPDATE exams SET type = #{type}, exam_name = #{examName}, description = #{description}, " +
            "classId = #{classId}, begin_time = #{begin_time}, end_time = #{end_time}, state = #{state} " +
            "WHERE examId = #{examId}")
    int update(ExamTask examTask);

    @Delete("DELETE FROM exams WHERE examId = #{examId}")
    int deleteById(Integer examId);

    @Select("SELECT COUNT(*) FROM exams")
    int countExams();

    // 添加一些示例数据
    @Insert("INSERT IGNORE INTO exams (examId, type, description, classId, state, exam_name) VALUES " +
            "(1, '800米跑', '800米长跑项目测试', 1, '已结束', '第一次体能测试'), " +
            "(2, '立定跳远', '立定跳远项目测试', 1, '已结束', '第二次体能测试'), " +
            "(3, '仰卧起坐', '仰卧起坐项目测试', 1, '未开始', '第三次体能测试')")
    int insertSampleData();
}