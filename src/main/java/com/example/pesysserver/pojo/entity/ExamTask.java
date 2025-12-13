package com.example.pesysserver.pojo.entity;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ExamTask {
    private Integer examId;
    private String type;
    private String examName;  // 考试任务名称
    private String description;  // 任务说明
    private Integer classId;
    private LocalDateTime begin_time;
    private LocalDateTime end_time;
    private String state; // 考试状态：未开始，正在进行，已结束

    // 扩展字段
    private String taskName;
    private List<Integer> classIds;
    private List<String> examProjects;
    private LocalDateTime examDate;
    private String location;
    private String createdBy;
}