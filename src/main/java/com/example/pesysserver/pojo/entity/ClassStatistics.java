package com.example.pesysserver.pojo.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ClassStatistics {
    private Integer statId;
    private Integer classId;
    private Integer examId;
    private Integer totalStudents;
    private Double avgScore;
    private Double maxScore;
    private Double minScore;
    private Integer passCount;
    private Integer excellentCount;
    private Double passRate;
    private Double excellentRate;
    private Integer count90Plus;
    private Integer count80To89;
    private Integer count70To79;
    private Integer count60To69;
    private Integer countBelow60;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;

    // 扩展字段
    private String className;
    private String examName;
    private String teacherName;
}