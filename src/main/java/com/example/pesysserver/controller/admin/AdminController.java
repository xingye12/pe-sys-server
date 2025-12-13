package com.example.pesysserver.controller.admin;

import com.example.pesysserver.common.Result;
import com.example.pesysserver.pojo.dto.DashboardDTO;
import com.example.pesysserver.pojo.dto.ExamTaskCreateDTO;
import com.example.pesysserver.pojo.entity.ClassInfo;
import com.example.pesysserver.pojo.entity.ExamReport;
import com.example.pesysserver.pojo.entity.ExamTask;
import com.example.pesysserver.pojo.entity.StudentScore;
import com.example.pesysserver.pojo.entity.VideoFile;
import com.example.pesysserver.pojo.vo.ClassDetailVO;
import com.example.pesysserver.service.AdminService;
import com.example.pesysserver.service.ExamStatusService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private ExamStatusService examStatusService;

    // ============ 班级管理 ============

    @GetMapping("/classes")
    public Result<List<Map<String, Object>>> getAllClasses() {
        try {
            List<Map<String, Object>> classes = adminService.getAllClassesWithDetails();
            return Result.success(classes);
        } catch (Exception e) {
            log.error("获取班级列表失败", e);
            return Result.error("获取班级列表失败");
        }
    }

    @PostMapping("/classes")
    public Result<String> createClass(@RequestBody Map<String, Object> classData) {
        try {
            adminService.createClass(classData);
            return Result.success("创建班级成功");
        } catch (Exception e) {
            log.error("创建班级失败", e);
            return Result.error("创建班级失败: " + e.getMessage());
        }
    }

    @PutMapping("/classes/{classId}")
    public Result<String> updateClass(@PathVariable Integer classId, @RequestBody Map<String, Object> classData) {
        try {
            adminService.updateClass(classId, classData);
            return Result.success("更新班级成功");
        } catch (Exception e) {
            log.error("更新班级失败", e);
            return Result.error("更新班级失败: " + e.getMessage());
        }
    }

    @DeleteMapping("/classes/{classId}")
    public Result<String> deleteClass(@PathVariable Integer classId) {
        try {
            adminService.deleteClass(classId);
            return Result.success("删除班级成功");
        } catch (Exception e) {
            log.error("删除班级失败", e);
            return Result.error("删除班级失败");
        }
    }

    @GetMapping("/classes/{classId}")
    public Result<Map<String, Object>> getClassDetail(@PathVariable Integer classId) {
        try {
            Map<String, Object> classDetail = adminService.getClassDetail(classId);
            if (classDetail == null) {
                return Result.error("班级不存在");
            }
            return Result.success(classDetail);
        } catch (Exception e) {
            log.error("获取班级详情失败", e);
            return Result.error("获取班级详情失败");
        }
    }

    @GetMapping("/classes/{classId}/students")
    public Result<List<Map<String, Object>>> getClassStudents(@PathVariable Integer classId) {
        try {
            List<Map<String, Object>> students = adminService.getClassStudents(classId);
            return Result.success(students);
        } catch (Exception e) {
            log.error("获取班级学生失败", e);
            return Result.error("获取班级学生失败");
        }
    }

    @GetMapping("/classes/{classId}/students/{examId}")
    public Result<List<Map<String, Object>>> getExamStudents(@PathVariable Integer classId,
                                                             @PathVariable Integer examId) {
        try {
            List<Map<String, Object>> students = adminService.getExamStudents(classId, examId);
            return Result.success(students);
        } catch (Exception e) {
            log.error("获取考试学生失败", e);
            return Result.error("获取考试学生失败");
        }
    }

    // ============ 教师管理 ============

    @GetMapping("/teachers")
    public Result<List<Map<String, Object>>> getAllTeachers() {
        try {
            List<Map<String, Object>> teachers = adminService.getAllTeachers();
            return Result.success(teachers);
        } catch (Exception e) {
            log.error("获取教师列表失败", e);
            return Result.error("获取教师列表失败");
        }
    }

    // ============ 学生管理 ============

    @GetMapping("/students")
    public Result<Map<String, Object>> getAllStudents(
            @RequestParam(required = false) String classId,
            @RequestParam(required = false) String name,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        try {
            List<Map<String, Object>> students = adminService.getAllStudents();

            // 简单的过滤逻辑
            if (classId != null && !classId.isEmpty()) {
                students = students.stream()
                    .filter(s -> classId.equals(s.get("classId").toString()))
                    .collect(Collectors.toList());
            }
            if (name != null && !name.isEmpty()) {
                students = students.stream()
                    .filter(s -> s.get("name").toString().contains(name))
                    .collect(Collectors.toList());
            }

            // 分页逻辑
            int total = students.size();
            int start = (page - 1) * size;
            int end = Math.min(start + size, total);
            List<Map<String, Object>> pageData = students.subList(start, end);

            Map<String, Object> result = new HashMap<>();
            result.put("list", pageData);
            result.put("total", total);
            result.put("page", page);
            result.put("size", size);

            return Result.success(result);
        } catch (Exception e) {
            log.error("获取学生列表失败", e);
            return Result.error("获取学生列表失败");
        }
    }

  @PostMapping("/students")
    public Result<String> createStudent(@RequestBody Map<String, Object> studentData) {
        try {
            adminService.createStudent(studentData);
            return Result.success("添加学生成功");
        } catch (Exception e) {
            log.error("添加学生失败", e);
            return Result.error("添加学生失败: " + e.getMessage());
        }
    }

  @PutMapping("/students/{studentId}")
    public Result<String> updateStudent(@PathVariable Integer studentId, @RequestBody Map<String, Object> studentData) {
        try {
            adminService.updateStudent(studentId, studentData);
            return Result.success("更新学生成功");
        } catch (Exception e) {
            log.error("更新学生失败", e);
            return Result.error("更新学生失败: " + e.getMessage());
        }
    }

  @DeleteMapping("/students/{studentId}")
    public Result<String> deleteStudent(@PathVariable Integer studentId) {
        try {
            adminService.deleteStudent(studentId);
            return Result.success("删除学生成功");
        } catch (Exception e) {
            log.error("删除学生失败", e);
            return Result.error("删除学生失败");
        }
  }

  @PostMapping("/students/batch")
    public Result<String> batchCreateStudents(@RequestBody List<Map<String, Object>> students) {
        try {
            adminService.batchCreateStudents(students);
            return Result.success("批量添加学生成功");
        } catch (Exception e) {
            log.error("批量添加学生失败", e);
            return Result.error("批量添加学生失败: " + e.getMessage());
        }
    }

    // ============ 考试任务管理 ============

    @GetMapping("/tasks")
    public Result<List<ExamTask>> getAllExamTasks() {
        try {
            List<ExamTask> examTasks = adminService.getAllExamTasks();
            return Result.success(examTasks);
        } catch (Exception e) {
            log.error("获取考试任务列表失败", e);
            return Result.error("获取考试任务列表失败");
        }
    }

    @GetMapping("/tasks/{taskId}")
    public Result<ExamTask> getExamTaskById(@PathVariable Integer taskId) {
        try {
            ExamTask examTask = adminService.getExamTaskById(taskId);
            if (examTask == null) {
                return Result.error("考试任务不存在");
            }
            return Result.success(examTask);
        } catch (Exception e) {
            log.error("获取考试任务详情失败", e);
            return Result.error("获取考试任务详情失败");
        }
    }

    @PostMapping("/tasks")
    public Result<ExamTask> createExamTask(@RequestBody ExamTaskCreateDTO examTaskCreateDTO) {
        try {
            log.info("接收到任务发布请求: {}", examTaskCreateDTO);
            log.info("任务详情 - 名称: {}, 描述: {}, 班级IDs: {}, 考试项目: {}, 开始时间: {}, 结束时间: {}",
                examTaskCreateDTO.getTaskName(),
                examTaskCreateDTO.getDescription(),
                examTaskCreateDTO.getClassIds(),
                examTaskCreateDTO.getExamProjects(),
                examTaskCreateDTO.getStartTime(),
                examTaskCreateDTO.getEndTime());

            ExamTask examTask = adminService.createExamTask(examTaskCreateDTO, "admin");
            log.info("任务创建成功: {}", examTask);
            return Result.success(examTask);
        } catch (Exception e) {
            log.error("创建考试任务失败", e);
            return Result.error("创建考试任务失败: " + e.getMessage());
        }
    }

    @DeleteMapping("/tasks/{taskId}")
    public Result<String> deleteExamTask(@PathVariable Integer taskId) {
        try {
            adminService.deleteExamTask(taskId);
            return Result.success("删除成功");
        } catch (Exception e) {
            log.error("删除考试任务失败", e);
            return Result.error("删除考试任务失败");
        }
    }

    @PutMapping("/tasks/{taskId}/status")
    public Result<String> updateExamTaskStatus(
            @PathVariable Integer taskId,
            @RequestBody Map<String, String> statusData) {
        try {
            String newStatus = statusData.get("status");
            if (newStatus == null || newStatus.trim().isEmpty()) {
                return Result.error("状态不能为空");
            }

            boolean success = examStatusService.updateExamStatus(taskId, newStatus);
            if (success) {
                return Result.success("状态更新成功");
            } else {
                return Result.error("状态更新失败");
            }
        } catch (Exception e) {
            log.error("更新考试任务状态失败", e);
            return Result.error("更新考试任务状态失败: " + e.getMessage());
        }
    }

    @PostMapping("/tasks/update-all-statuses")
    public Result<String> updateAllExamStatuses() {
        try {
            examStatusService.updateAllExamStatuses();
            return Result.success("考试状态检查更新完成");
        } catch (Exception e) {
            log.error("批量更新考试状态失败", e);
            return Result.error("批量更新考试状态失败: " + e.getMessage());
        }
    }

    // ============ 考试成绩管理 ============

    @GetMapping("/exams/{examId}/report")
    public Result<ExamReport> getExamReport(@PathVariable Integer examId) {
        try {
            ExamReport report = adminService.getExamReport(examId);
            return Result.success(report);
        } catch (Exception e) {
            log.error("获取考试报告失败", e);
            return Result.error("获取考试报告失败: " + e.getMessage());
        }
    }

    @PostMapping("/scores")
    public Result<String> batchSaveScores(@RequestBody List<StudentScore> scores) {
        try {
            adminService.batchSaveScores(scores);
            return Result.success("成绩保存成功");
        } catch (Exception e) {
            log.error("批量保存成绩失败", e);
            return Result.error("批量保存成绩失败: " + e.getMessage());
        }
    }

    @GetMapping("/scores/class/{classId}")
    public Result<List<StudentScore>> getClassScores(@PathVariable Integer classId) {
        try {
            List<StudentScore> scores = adminService.getClassScores(classId);
            return Result.success(scores);
        } catch (Exception e) {
            log.error("获取班级成绩失败", e);
            return Result.error("获取班级成绩失败");
        }
    }

    // ============ 视频管理 ============

    @GetMapping("/videos")
    public Result<List<VideoFile>> getVideosByCondition(
            @RequestParam(required = false) Integer classId,
            @RequestParam(required = false) String analysisStatus) {
        try {
            List<VideoFile> videos = adminService.getVideosByCondition(classId, analysisStatus);
            return Result.success(videos);
        } catch (Exception e) {
            log.error("获取视频列表失败", e);
            return Result.error("获取视频列表失败");
        }
    }

    @GetMapping("/videos/{videoId}")
    public Result<VideoFile> getVideoById(@PathVariable Integer videoId) {
        try {
            VideoFile videoFile = adminService.getVideoById(videoId);
            if (videoFile == null) {
                return Result.error("视频不存在");
            }
            return Result.success(videoFile);
        } catch (Exception e) {
            log.error("获取视频详情失败", e);
            return Result.error("获取视频详情失败");
        }
    }

    @PostMapping("/videos/{videoId}/analyze")
    public Result<String> analyzeVideo(@PathVariable Integer videoId) {
        try {
            adminService.analyzeVideo(videoId);
            return Result.success("分析任务已提交");
        } catch (Exception e) {
            log.error("视频分析失败", e);
            return Result.error("视频分析失败");
        }
    }

    @PostMapping("/videos/batch-analyze")
    public Result<String> batchAnalyzeVideos(@RequestBody List<Integer> videoIds) {
        try {
            adminService.batchAnalyzeVideos(videoIds);
            return Result.success("批量分析任务已提交");
        } catch (Exception e) {
            log.error("批量视频分析失败", e);
            return Result.error("批量视频分析失败");
        }
    }

    // ============ 数据统计 ============

    @GetMapping("/dashboard")
    public Result<DashboardDTO> getDashboardStatistics() {
        try {
            DashboardDTO dashboardDTO = adminService.getDashboardStatistics();
            return Result.success(dashboardDTO);
        } catch (Exception e) {
            log.error("获取统计数据失败", e);
            return Result.error("获取统计数据失败");
        }
    }

    @GetMapping("/statistics/class/{classId}")
    public Result<Map<String, Object>> getClassStatistics(@PathVariable Integer classId) {
        try {
            Map<String, Object> stats = adminService.getClassStatistics(classId);
            return Result.success(stats);
        } catch (Exception e) {
            log.error("获取班级统计失败", e);
            return Result.error("获取班级统计失败");
        }
    }
}
