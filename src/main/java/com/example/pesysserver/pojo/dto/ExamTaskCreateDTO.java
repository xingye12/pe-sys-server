package com.example.pesysserver.pojo.dto;

import lombok.Data;

import java.util.List;

@Data
public class ExamTaskCreateDTO {
    private String taskName;
    private String description;
    private List<Long> classIds;
    private List<String> examProjects;
    private String startTime;
    private String endTime;
    private String location;
}