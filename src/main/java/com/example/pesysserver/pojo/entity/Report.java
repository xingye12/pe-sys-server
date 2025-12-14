package com.example.pesysserver.pojo.entity;

import lombok.Data;

import java.util.Date;

@Data

public class Report {
    private int reportId;
    private int examId;
    private int studentId;
    private float grade_tiaosheng;
    private float grade_zuoweitiqianqv;
    private float grade_jump;
    private String action_defect;
    private String recommendation;
    private String name;
    private Date time;

}
