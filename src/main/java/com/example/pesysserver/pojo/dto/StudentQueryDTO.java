package com.example.pesysserver.pojo.dto;

import lombok.Data;

@Data
public class StudentQueryDTO {
    private Integer classId;
    private String studentName;
    private String scoreRange; // excellent, good, pass, fail
    private String sortBy; // score_desc, score_asc, name, student_no
    private Integer page = 1;
    private Integer size = 10;
}