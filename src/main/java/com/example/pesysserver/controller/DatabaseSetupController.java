package com.example.pesysserver.controller;

import com.example.pesysserver.common.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/setup")
public class DatabaseSetupController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @PostMapping("/init-data")
    public Result<String> initializeData() {
        try {
            // 清空现有数据
            jdbcTemplate.execute("DELETE FROM exams");
            jdbcTemplate.execute("DELETE FROM class");
            jdbcTemplate.execute("DELETE FROM student");
            jdbcTemplate.execute("DELETE FROM teacher");
            jdbcTemplate.execute("DELETE FROM video");

            // 插入教师数据
            jdbcTemplate.execute("INSERT INTO teacher (teacherId, name, password) VALUES " +
                    "(1, '张老师', '123456'), " +
                    "(2, '李老师', '123456'), " +
                    "(3, '王老师', '123456')");

            // 插入班级数据
            jdbcTemplate.execute("INSERT INTO class (classId, teacherId, examId) VALUES " +
                    "(1, 1, 1), " +
                    "(2, 2, 2), " +
                    "(3, 3, 3)");

            // 插入学生数据
            jdbcTemplate.execute("INSERT INTO student (studentId, name, password, classId) VALUES " +
                    "(1, '张小明', '123456', 1), " +
                    "(2, '李小红', '123456', 1), " +
                    "(3, '王小强', '123456', 1), " +
                    "(4, '刘小美', '123456', 2), " +
                    "(5, '陈小东', '123456', 2), " +
                    "(6, '赵小军', '123456', 3), " +
                    "(7, '黄小芳', '123456', 3), " +
                    "(8, '周小华', '123456', 3)");

            // 插入考试任务数据
            jdbcTemplate.execute("INSERT INTO exams (examId, type, description, classId, begin_time, end_time, state, exam_name) VALUES " +
                    "(1, '800米跑', '800米长跑项目测试', 1, '2025-01-15 09:00:00', '2025-01-15 10:00:00', '已结束', '第一次体能测试'), " +
                    "(2, '立定跳远', '立定跳远项目测试', 2, '2025-01-16 09:00:00', '2025-01-16 10:00:00', '未开始', '第二次体能测试'), " +
                    "(3, '仰卧起坐', '仰卧起坐项目测试', 3, '2025-01-17 09:00:00', '2025-01-17 10:00:00', '正在进行', '第三次体能测试')");

            // 插入视频数据
            jdbcTemplate.execute("INSERT INTO video (vidoeId, name) VALUES " +
                    "(1, '800米跑_张小明_1班.mp4'), " +
                    "(2, '800米跑_李小红_1班.mp4'), " +
                    "(3, '立定跳远_王小强_1班.mp4'), " +
                    "(4, '立定跳远_刘小美_2班.mp4'), " +
                    "(5, '仰卧起坐_陈小东_2班.mp4')");

            return Result.success("数据初始化成功！插入了3个老师、3个班级、8个学生、3个考试任务、5个视频。");
        } catch (Exception e) {
            return Result.error("数据初始化失败: " + e.getMessage());
        }
    }

    @GetMapping("/check-data")
    public Result<String> checkData() {
        try {
            StringBuilder result = new StringBuilder();

            int teacherCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM teacher", Integer.class);
            int classCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM class", Integer.class);
            int studentCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM student", Integer.class);
            int examCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM exams", Integer.class);
            int videoCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM video", Integer.class);

            result.append("数据统计:\n");
            result.append("教师数量: ").append(teacherCount).append("\n");
            result.append("班级数量: ").append(classCount).append("\n");
            result.append("学生数量: ").append(studentCount).append("\n");
            result.append("考试任务数量: ").append(examCount).append("\n");
            result.append("视频数量: ").append(videoCount).append("\n");

            return Result.success(result.toString());
        } catch (Exception e) {
            return Result.error("检查数据失败: " + e.getMessage());
        }
    }
}