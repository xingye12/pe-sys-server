package com.example.pesysserver.pojo.entity;

import lombok.Data;

@Data
public class Student {
    private Integer studentId;
    private String name;
    private String password;
    private Integer classId;

    // 扩展字段
    private String className;
    private String grade;
    private Integer age;
    private String gender;
    private String phone;
    private String parentPhone;
}