package com.example.pesysserver.pojo.entity;

import lombok.Data;

import java.util.List;

@Data
public class ExamReport {
    private Integer examId;
    private String examType;
    private String examDescription;
    private Integer classId;
    private String className;
    private String teacherName;
    private String examDate;
    private String status;

    // 统计数据
    private Integer totalStudents;
    private Integer passCount;
    private Integer excellentCount;
    private Double passRate;
    private Double excellentRate;
    private Double avgScore;

    // 分数分布
    private Integer count90Plus;    // 90分以上
    private Integer count80To89;    // 80-89分
    private Integer count70To79;    // 70-79分
    private Integer count60To69;    // 60-69分
    private Integer countBelow60;  // 60分以下

    // 学生成绩列表
    private List<StudentScore> studentScores;

    // 班级排名
    private List<StudentScore> topStudents;
    private List<StudentScore> lowScoreStudents;

    // 考试项目详情
    private String examDetails;
    private Double classRank;
}