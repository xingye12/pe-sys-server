package com.example.pesysserver.mapper;

import com.example.pesysserver.pojo.entity.Admin;
import com.example.pesysserver.pojo.entity.Teacher;
import com.example.pesysserver.pojo.entity.TeacherEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface AdminMapper {
    @Select("SELECT * FROM admin WHERE username = #{username} AND password = #{password}")
    Admin verifyAdminCredentials(@Param("username") String username, @Param("password") String password);
}