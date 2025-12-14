package com.example.pesysserver.service.impl;

import com.example.pesysserver.mapper.AdminMapper;
import com.example.pesysserver.mapper.StudentMapper;
import com.example.pesysserver.mapper.TeacherMapper;
import com.example.pesysserver.pojo.dto.LoginRequestDTO;
import com.example.pesysserver.pojo.entity.*;
import com.example.pesysserver.service.AdminService;
import com.example.pesysserver.service.LoginService;
import com.example.pesysserver.service.TeacherService;
import com.example.pesysserver.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginServiceImpl implements LoginService {


    @Autowired
    private StudentMapper studentMapper;

    @Autowired
    private TeacherMapper teacherMapper;

    @Autowired
    private AdminMapper adminMapper;
    @Override
    public Result login(LoginRequestDTO loginRequest) {
        String userType = loginRequest.getUserType();
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();

        switch (userType) {
            case "student":
                StudentEntity student = verifyStudentCredentials(username, password);
                return student!=null ? Result.success(student) : Result.error("Invalid student credentials");

            case "teacher":
                // 查询教师数据库
                TeacherEntity teacher = verifyTeacherCredentials(username, password);
                return teacher!=null ? Result.success(teacher) : Result.error("Invalid teacher credentials");

            case "admin":
                // 查询管理员数据库
                Admin admin = verifyAdminCredentials(username, password);
                return admin!=null ? Result.success(admin) : Result.error("Invalid admin credentials");

            default:
                return Result.error("Invalid user type");
        }
    }

    private StudentEntity verifyStudentCredentials(String username, String password) {
        return studentMapper.verifyStudentCredentials(username, password) ;
    }

    private TeacherEntity verifyTeacherCredentials(String username, String password) {
        return teacherMapper.verifyTeacherCredentials(username, password) ;
    }

    private Admin verifyAdminCredentials(String username, String password) {
        return adminMapper.verifyAdminCredentials(username, password) ;
    }
}