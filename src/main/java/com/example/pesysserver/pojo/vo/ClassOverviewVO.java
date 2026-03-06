package com.example.pesysserver.pojo.vo;

import lombok.Data;

@Data
public class ClassOverviewVO {
    private Integer classId;
    private String className;
    private String grade;
    private Integer totalStudents;
    private Double avgScore;
    private Double passRate;
    private Double excellentRate;
    private Integer examCount;
    private String latestExamTime;
    private String teacherName;
}