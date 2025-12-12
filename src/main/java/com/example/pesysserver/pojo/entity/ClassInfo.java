package com.example.pesysserver.pojo.entity;

import lombok.Data;

@Data
public class ClassInfo {
    private Integer classId;
    private Integer teacherId;
    private Integer examId;

    // 扩展字段（可能需要关联查询）
    private String className;
    private String grade;
    private Integer studentCount;
    private String teacherName;
}