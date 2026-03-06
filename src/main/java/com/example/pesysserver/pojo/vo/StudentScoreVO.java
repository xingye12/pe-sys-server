package com.example.pesysserver.pojo.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class StudentScoreVO {
    private Integer studentId;
    private String studentNo;
    private String studentName;
    private String gender;
    private String className;
    private Double avgScore;
    private Integer rank;
    private Integer examCount;
    private Double passRate;
    private Double progress;
    private String latestExamType;
    private LocalDateTime latestExamTime;
}