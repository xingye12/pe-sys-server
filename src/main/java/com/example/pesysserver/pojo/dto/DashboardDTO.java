package com.example.pesysserver.pojo.dto;

import lombok.Data;

@Data
public class DashboardDTO {
    private Integer totalClasses;
    private Integer totalStudents;
    private Integer examTasks;
    private Integer videosPendingReview;
    private String avgScore;
    private String passRate;
    private String excellentRate;
}