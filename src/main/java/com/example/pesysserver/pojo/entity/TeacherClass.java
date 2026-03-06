package com.example.pesysserver.pojo.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TeacherClass {
    private Integer id;
    private Integer teacherId;
    private Integer classId;
    private LocalDateTime assignedDate;
    private String status;

    // 扩展字段
    private String teacherName;
    private String className;
    private Integer studentCount;
    private String grade;
}