package com.example.pesysserver.controller;

import com.example.pesysserver.common.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/schema")
public class SchemaController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/describe/class")
    public Result<String> describeClassTable() {
        return describeTable("class");
    }

    @GetMapping("/describe/student")
    public Result<String> describeStudentTable() {
        return describeTable("student");
    }

    @GetMapping("/describe/video")
    public Result<String> describeVideoTable() {
        return describeTable("video");
    }

    @GetMapping("/describe/exams")
    public Result<String> describeExamsTable() {
        return describeTable("exams");
    }

    @GetMapping("/describe/teacher")
    public Result<String> describeTeacherTable() {
        return describeTable("teacher");
    }

    private Result<String> describeTable(String tableName) {
        try {
            String sql = "DESCRIBE " + tableName;
            List<Map<String, Object>> columns = jdbcTemplate.queryForList(sql);

            StringBuilder sb = new StringBuilder("表 " + tableName + " 的结构:\n");
            for (Map<String, Object> column : columns) {
                sb.append(column.get("Field")).append(" - ")
                  .append(column.get("Type")).append(" - ")
                  .append(column.get("Null")).append(" - ")
                  .append(column.get("Key")).append("\n");
            }

            return Result.success(sb.toString());
        } catch (Exception e) {
            return Result.error("查询表结构失败: " + e.getMessage());
        }
    }

    @GetMapping("/sample/class")
    public Result<String> getSampleClassData() {
        return getSampleData("class");
    }

    @GetMapping("/sample/student")
    public Result<String> getSampleStudentData() {
        return getSampleData("student");
    }

    @GetMapping("/sample/video")
    public Result<String> getSampleVideoData() {
        return getSampleData("video");
    }

    private Result<String> getSampleData(String tableName) {
        try {
            String sql = "SELECT * FROM " + tableName + " LIMIT 3";
            List<Map<String, Object>> data = jdbcTemplate.queryForList(sql);

            StringBuilder sb = new StringBuilder("表 " + tableName + " 的示例数据:\n");
            for (Map<String, Object> row : data) {
                sb.append(row).append("\n");
            }

            return Result.success(sb.toString());
        } catch (Exception e) {
            return Result.error("查询示例数据失败: " + e.getMessage());
        }
    }
}