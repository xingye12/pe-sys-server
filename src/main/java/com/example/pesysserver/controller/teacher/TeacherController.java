package com.example.pesysserver.controller.teacher;

import com.example.pesysserver.pojo.entity.Teacher;
import com.example.pesysserver.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Collections;
import java.util.List;
import java.util.Map;

// 也可以直接用@RestController（= @Controller + @ResponseBody），简化代码
@Controller
@RequestMapping("/teacher")
public class TeacherController {

    // 注入JdbcTemplate（Spring会自动创建Bean，前提是数据库配置正确+引入spring-jdbc依赖）
    @Autowired
    private TeacherService teacherService;

    @GetMapping("/all")
    @ResponseBody
    public List<Teacher> getAllTeachers() {
        return teacherService.getAllTeachers();
    }
}