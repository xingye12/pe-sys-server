package com.example.pesysserver.mapper;

import com.example.pesysserver.pojo.entity.Teacher;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface TeacherMapper {
    @Select("select * from teacher")
    List<Teacher> getAllTeachers();
}
