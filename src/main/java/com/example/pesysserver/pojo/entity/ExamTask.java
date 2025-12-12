package com.example.pesysserver.pojo.entity;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ExamTask {
    private Integer examId;
    private String type;
    private String description;
    private Integer classId;
    private LocalDateTime begin_time;
    private LocalDateTime end_time;
    private String status;

    // 扩展字段
    private String taskName;
    private List<Integer> classIds;
    private List<String> examProjects;
    private LocalDateTime examDate;
    private String location;
    private String createdBy;
}