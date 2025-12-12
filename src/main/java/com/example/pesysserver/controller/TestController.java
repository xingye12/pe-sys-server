package com.example.pesysserver.controller;

import com.example.pesysserver.common.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/connection")
    public Result<String> testConnection() {
        try {
            String sql = "SELECT 1 as test";
            Map<String, Object> result = jdbcTemplate.queryForMap(sql);
            return Result.success("数据库连接成功！测试结果: " + result.get("test"));
        } catch (Exception e) {
            return Result.error("数据库连接失败: " + e.getMessage());
        }
    }

    @GetMapping("/tables")
    public Result<String> checkTables() {
        try {
            // 检查表是否存在
            String sql = "SHOW TABLES";
            List<Map<String, Object>> tables = jdbcTemplate.queryForList(sql);

            StringBuilder sb = new StringBuilder("数据库中的表:\n");
            for (Map<String, Object> table : tables) {
                sb.append(table.values().iterator().next()).append("\n");
            }

            return Result.success(sb.toString());
        } catch (Exception e) {
            return Result.error("查询表失败: " + e.getMessage());
        }
    }
}