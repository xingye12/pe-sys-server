package com.example.pesysserver.service.impl;

import com.example.pesysserver.mapper.TeacherMapper;
import com.example.pesysserver.pojo.entity.Teacher;
import com.example.pesysserver.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeacherServiceImpl implements TeacherService {
    @Autowired
    TeacherMapper teacherMapper;
    @Override
    public List<Teacher> getAllTeachers() {
        return teacherMapper.getAllTeachers();
    }
}
