package com.example.pesysserver.pojo.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class StudentScore {
    private Integer scoreId;
    private Integer studentId;
    private String studentName;
    private Integer classId;
    private String className;
    private Integer examId;
    private String examType;
    private Double score;
    private String grade; // 优秀、良好、及格、不及格
    private LocalDateTime examTime;
    private String teacherComment;

    // 扩展字段
    private String ranking;
    private Double classAvgScore;
    private Integer classRanking;
}