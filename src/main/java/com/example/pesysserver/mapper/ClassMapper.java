package com.example.pesysserver.mapper;

import com.example.pesysserver.pojo.entity.ClassInfo;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Map;

@Mapper
public interface ClassMapper {

    @Select("SELECT c.*, t.name as teacherName " +
            "FROM class c " +
            "LEFT JOIN teacher t ON c.teacherId = t.teacherId")
    List<ClassInfo> findAll();

    @Select("SELECT c.*, t.name as teacherName " +
            "FROM class c " +
            "LEFT JOIN teacher t ON c.teacherId = t.teacherId " +
            "WHERE c.classId = #{classId}")
    ClassInfo findById(Integer classId);

    // 获取班级详细信息（包含学生数量）
    @Select("SELECT c.*, " +
            "(SELECT t.name FROM teacher t WHERE t.classId = c.classId LIMIT 1) as teacherName, " +
            "(SELECT COUNT(*) FROM student s WHERE s.classId = c.classId) as studentCount, " +
            "COALESCE(c.className, CONCAT(c.classId, '班')) as className, " +
            "COALESCE(c.grade, '未设置') as grade " +
            "FROM class c " +
            "ORDER BY c.classId")
    List<Map<String, Object>> findAllWithDetails();

    // 获取单个班级的完整信息
    @Select("SELECT c.*, " +
            "(SELECT t.name FROM teacher t WHERE t.classId = c.classId LIMIT 1) as teacherName, " +
            "(SELECT COUNT(*) FROM student s WHERE s.classId = c.classId) as studentCount, " +
            "COALESCE(c.className, CONCAT(c.classId, '班')) as className, " +
            "COALESCE(c.grade, '未设置') as grade " +
            "FROM class c " +
            "WHERE c.classId = #{classId}")
    Map<String, Object> findClassDetails(Integer classId);

    @Select("SELECT COUNT(*) FROM class")
    int countClasses();

    @Insert("INSERT IGNORE INTO class (classId, teacherId, examId) VALUES " +
            "(#{classId}, #{teacherId}, #{examId})")
    int insertClass(Integer classId, Integer teacherId, Integer examId);

    @Update("UPDATE class SET teacherId = #{teacherId}, examId = #{examId} WHERE classId = #{classId}")
    int updateClass(Integer classId, Integer teacherId, Integer examId);

    @Delete("DELETE FROM class WHERE classId = #{classId}")
    int deleteClass(Integer classId);

    // 插入示例数据
    @Insert("INSERT IGNORE INTO class (classId, teacherId, examId) VALUES " +
            "(1, 1, 1), (2, 2, 2), (3, 1, 3)")
    int insertSampleData();
}