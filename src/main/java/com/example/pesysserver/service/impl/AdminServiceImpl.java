package com.example.pesysserver.service.impl;

import com.example.pesysserver.mapper.ClassMapper;
import com.example.pesysserver.mapper.ExamTaskMapper;
import com.example.pesysserver.mapper.StudentMapper;
import com.example.pesysserver.mapper.VideoFileMapper;
import com.example.pesysserver.pojo.dto.DashboardDTO;
import com.example.pesysserver.pojo.dto.ExamTaskCreateDTO;
import com.example.pesysserver.pojo.entity.ExamReport;
import com.example.pesysserver.pojo.entity.ExamTask;
import com.example.pesysserver.pojo.entity.StudentScore;
import com.example.pesysserver.pojo.entity.VideoFile;
import com.example.pesysserver.service.AdminService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private ClassMapper classMapper;

    @Autowired
    private StudentMapper studentMapper;

    @Autowired
    private ExamTaskMapper examTaskMapper;

    @Autowired
    private VideoFileMapper videoFileMapper;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<Map<String, Object>> getAllClassesWithDetails() {
        try {
            // 检查是否有数据，没有则插入示例数据
            int count = classMapper.countClasses();
            if (count == 0) {
                initializeSampleData();
            }
            return classMapper.findAllWithDetails();
        } catch (Exception e) {
            log.error("获取班级详情失败", e);
            return new ArrayList<>();
        }
    }

    @Override
    public Map<String, Object> getClassDetail(Integer classId) {
        try {
            return classMapper.findClassDetails(classId);
        } catch (Exception e) {
            log.error("获取班级详情失败", e);
            return null;
        }
    }

    @Override
    public List<Map<String, Object>> getClassStudents(Integer classId) {
        try {
            return studentMapper.findByClassId(classId);
        } catch (Exception e) {
            log.error("获取班级学生失败", e);
            return new ArrayList<>();
        }
    }

    @Override
    public List<Map<String, Object>> getExamStudents(Integer classId, Integer examId) {
        try {
            return studentMapper.findStudentsByExam(classId, examId);
        } catch (Exception e) {
            log.error("获取考试学生失败", e);
            return new ArrayList<>();
        }
    }

    @Override
    public List<ExamTask> getAllExamTasks() {
        try {
            List<ExamTask> examTasks = examTaskMapper.findAll();
            log.info("获取到 {} 个考试任务", examTasks.size());

            examTasks.forEach(task -> {
                log.info("任务详情 - ID: {}, 类型: {}, 描述: {}, 状态: {}",
                    task.getExamId(), task.getType(), task.getDescription(), task.getState());
                // 扩展字段
                task.setTaskName(task.getType());
                task.setExamDate(task.getBegin_time());
                // 如果没有地点，设置默认地点
                if (task.getLocation() == null || task.getLocation().isEmpty()) {
                    task.setLocation("学校操场");
                }
            });
            return examTasks;
        } catch (Exception e) {
            log.error("获取考试任务失败", e);
            return new ArrayList<>();
        }
    }

    @Override
    public ExamTask getExamTaskById(Integer taskId) {
        try {
            ExamTask examTask = examTaskMapper.findById(taskId);
            if (examTask != null) {
                // 扩展字段
                examTask.setTaskName(examTask.getType());
                examTask.setExamDate(examTask.getBegin_time());
                examTask.setLocation("学校操场");
            }
            return examTask;
        } catch (Exception e) {
            log.error("获取考试任务详情失败", e);
            return null;
        }
    }

    @Override
    @Transactional
    public ExamTask createExamTask(ExamTaskCreateDTO dto, String createdBy) {
        try {
            ExamTask examTask = new ExamTask();
            examTask.setTaskName(dto.getTaskName());
            // examName字段存储任务名称
            examTask.setExamName(dto.getTaskName());
            // description字段存储任务说明
            examTask.setDescription(dto.getDescription());
            // type字段存储主要考试项目，如果有多个项目，取第一个；如果没有项目，使用默认值
            if (dto.getExamProjects() != null && !dto.getExamProjects().isEmpty()) {
                examTask.setType(dto.getExamProjects().get(0));
            } else {
                examTask.setType("体能测试");
            }
            examTask.setExamProjects(dto.getExamProjects());

            // 获取下一个examId
            Integer maxExamId = null;
            try {
                maxExamId = jdbcTemplate.queryForObject("SELECT MAX(examId) FROM exams", Integer.class);
            } catch (Exception e) {
                log.warn("查询examId失败，可能exams表不存在: {}", e.getMessage());
                maxExamId = null;
            }
            Integer newExamId = (maxExamId == null ? 0 : maxExamId) + 1;
            examTask.setExamId(newExamId);
            log.info("生成的examId: {}", newExamId);

            // 使用第一个班级ID（当前数据库结构只支持单个classId）
            if (dto.getClassIds() != null && !dto.getClassIds().isEmpty()) {
                examTask.setClassId(dto.getClassIds().get(0).intValue());
            }

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            // 直接使用前端传递的时间段
            examTask.setBegin_time(LocalDateTime.parse(dto.getStartTime(), formatter));
            examTask.setEnd_time(LocalDateTime.parse(dto.getEndTime(), formatter));
            examTask.setState("未开始");

            log.info("即将插入考试任务: examId={}, type={}, description={}, classId={}",
                examTask.getExamId(), examTask.getType(), examTask.getDescription(), examTask.getClassId());

            int insertResult = examTaskMapper.insert(examTask);
            log.info("数据库插入结果: {}", insertResult);
            log.info("创建考试任务成功，examId: {}, taskName: {}", newExamId, dto.getTaskName());
            return examTask;
        } catch (Exception e) {
            log.error("创建考试任务失败", e);
            throw new RuntimeException("创建考试任务失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public void deleteExamTask(Integer taskId) {
        try {
            examTaskMapper.deleteById(taskId);
        } catch (Exception e) {
            log.error("删除考试任务失败", e);
            throw new RuntimeException("删除考试任务失败");
        }
    }

    @Override
    public ExamReport getExamReport(Integer examId) {
        try {
            // 创建模拟考试报告
            ExamReport report = new ExamReport();
            report.setExamId(examId);

            ExamTask examTask = examTaskMapper.findById(examId);
            if (examTask != null) {
                report.setExamType(examTask.getType());
                report.setExamDescription(examTask.getDescription());
                report.setExamDate(examTask.getBegin_time().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                report.setStatus(examTask.getState());

                // 模拟统计数据
                report.setTotalStudents(45);
                report.setPassCount(38);
                report.setExcellentCount(25);
                report.setPassRate(84.4);
                report.setExcellentRate(55.6);
                report.setAvgScore(82.5);

                // 分数分布
                report.setCount90Plus(8);
                report.setCount80To89(17);
                report.setCount70To79(10);
                report.setCount60To69(5);
                report.setCountBelow60(5);

                // 模拟学生成绩
                List<StudentScore> studentScores = generateMockStudentScores(examId);
                report.setStudentScores(studentScores);

                // 班级排名
                report.setTopStudents(studentScores.stream().limit(5).collect(Collectors.toList()));
                report.setLowScoreStudents(studentScores.stream().skip(40).limit(5).collect(Collectors.toList()));
            }

            return report;
        } catch (Exception e) {
            log.error("获取考试报告失败", e);
            return null;
        }
    }

    @Override
    @Transactional
    public void batchSaveScores(List<StudentScore> scores) {
        try {
            // 这里应该有批量保存成绩的逻辑
            // 由于现有数据库结构限制，暂时记录日志
            log.info("批量保存成绩: {} 条记录", scores.size());
            scores.forEach(score -> log.info("学生ID: {}, 成绩: {}", score.getStudentId(), score.getScore()));
        } catch (Exception e) {
            log.error("批量保存成绩失败", e);
            throw new RuntimeException("批量保存成绩失败");
        }
    }

    @Override
    public List<StudentScore> getClassScores(Integer classId) {
        try {
            // 生成模拟班级成绩数据
            return generateMockStudentScores(null);
        } catch (Exception e) {
            log.error("获取班级成绩失败", e);
            return new ArrayList<>();
        }
    }

    @Override
    public List<VideoFile> getVideosByCondition(Integer classId, String analysisStatus) {
        try {
            List<VideoFile> videos = videoFileMapper.findAll();
            if (classId != null || analysisStatus != null) {
                // 简单过滤逻辑
                return videos.stream()
                    .filter(video -> classId == null || video.getClassId().equals(classId.longValue()))
                    .collect(Collectors.toList());
            }
            return videos;
        } catch (Exception e) {
            log.error("获取视频列表失败", e);
            return new ArrayList<>();
        }
    }

    @Override
    public VideoFile getVideoById(Integer videoId) {
        try {
            return videoFileMapper.findById(videoId);
        } catch (Exception e) {
            log.error("获取视频详情失败", e);
            return null;
        }
    }

    @Override
    public void analyzeVideo(Integer videoId) {
        try {
            log.info("开始分析视频: {}", videoId);
            // 模拟视频分析过程
            simulateAIAnalysis(videoId.longValue());
        } catch (Exception e) {
            log.error("视频分析失败", e);
            throw new RuntimeException("视频分析失败");
        }
    }

    @Override
    public void batchAnalyzeVideos(List<Integer> videoIds) {
        try {
            log.info("开始批量分析视频: {} 个视频", videoIds.size());
            videoIds.forEach(this::analyzeVideo);
        } catch (Exception e) {
            log.error("批量视频分析失败", e);
            throw new RuntimeException("批量视频分析失败");
        }
    }

    @Override
    public DashboardDTO getDashboardStatistics() {
        try {
            DashboardDTO dashboardDTO = new DashboardDTO();

            int classCount = classMapper.countClasses();
            dashboardDTO.setTotalClasses(classCount);

            int examCount = examTaskMapper.countExams();
            dashboardDTO.setExamTasks(examCount);

            int videoCount = videoFileMapper.countVideos();
            dashboardDTO.setVideosPendingReview(Math.max(0, videoCount - 2));

            // 模拟学生数据
            dashboardDTO.setTotalStudents(classCount * 45);
            dashboardDTO.setAvgScore("85.5");
            dashboardDTO.setPassRate("92.3");
            dashboardDTO.setExcellentRate("78.6");

            return dashboardDTO;
        } catch (Exception e) {
            log.error("获取统计数据失败", e);
            // 返回默认数据
            DashboardDTO dashboardDTO = new DashboardDTO();
            dashboardDTO.setTotalClasses(5);
            dashboardDTO.setTotalStudents(225);
            dashboardDTO.setExamTasks(3);
            dashboardDTO.setVideosPendingReview(3);
            dashboardDTO.setAvgScore("85.5");
            dashboardDTO.setPassRate("92.3");
            dashboardDTO.setExcellentRate("78.6");
            return dashboardDTO;
        }
    }

    @Override
    public Map<String, Object> getClassStatistics(Integer classId) {
        try {
            Map<String, Object> stats = new HashMap<>();

            // 获取班级详情
            Map<String, Object> classDetail = classMapper.findClassDetails(classId);
            if (classDetail != null) {
                stats.put("classInfo", classDetail);

                // 模拟统计数据
                stats.put("totalStudents", classDetail.get("studentCount"));
                stats.put("avgScore", 82.5);
                stats.put("passRate", 84.4);
                stats.put("excellentRate", 55.6);
                stats.put("recentExams", 3);
                stats.put("totalVideos", 12);

                // 成绩分布
                Map<String, Integer> scoreDistribution = new HashMap<>();
                scoreDistribution.put("90-100", 8);
                scoreDistribution.put("80-89", 17);
                scoreDistribution.put("70-79", 10);
                scoreDistribution.put("60-69", 5);
                scoreDistribution.put("0-59", 5);
                stats.put("scoreDistribution", scoreDistribution);
            }

            return stats;
        } catch (Exception e) {
            log.error("获取班级统计失败", e);
            return new HashMap<>();
        }
    }

    private void initializeSampleData() {
        try {
            log.info("初始化示例数据");
            classMapper.insertSampleData();
            examTaskMapper.insertSampleData();
            videoFileMapper.insertSampleData();
        } catch (Exception e) {
            log.warn("初始化示例数据失败: {}", e.getMessage());
        }
    }

    private List<StudentScore> generateMockStudentScores(Integer examId) {
        List<StudentScore> scores = new ArrayList<>();
        Random random = new Random();

        String[] names = {"张三", "李四", "王五", "赵六", "钱七", "孙八", "周九", "吴十"};
        String[] grades = {"优秀", "良好", "及格", "不及格"};

        for (int i = 1; i <= 45; i++) {
            StudentScore score = new StudentScore();
            score.setScoreId(i);
            score.setStudentId(i + 1000);
            score.setStudentName(names[i % names.length] + (i + 1));
            score.setClassId(1);
            score.setClassName("1班");
            score.setExamId(examId != null ? examId : 1);
            score.setExamType("800米跑");

            double scoreValue = 50 + random.nextDouble() * 50;
            score.setScore(Math.round(scoreValue * 10.0) / 10.0);

            if (score.getScore() >= 90) {
                score.setGrade(grades[0]);
            } else if (score.getScore() >= 80) {
                score.setGrade(grades[1]);
            } else if (score.getScore() >= 60) {
                score.setGrade(grades[2]);
            } else {
                score.setGrade(grades[3]);
            }

            score.setExamTime(LocalDateTime.now().minusDays(random.nextInt(30)));
            score.setTeacherComment("表现" + score.getGrade());

            scores.add(score);
        }

        // 按成绩排序
        scores.sort((a, b) -> Double.compare(b.getScore(), a.getScore()));

        // 设置排名
        for (int i = 0; i < scores.size(); i++) {
            scores.get(i).setClassRanking(i + 1);
            scores.get(i).setClassAvgScore(82.5);
        }

        return scores;
    }

    private void simulateAIAnalysis(Long videoId) {
        new Thread(() -> {
            try {
                Thread.sleep(3000);

                VideoFile videoFile = videoFileMapper.findById(videoId.intValue());
                if (videoFile != null) {
                    log.info("AI分析完成，视频ID: {}", videoId);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.error("AI分析被中断", e);
            }
        }).start();
    }

    // ============ 班级管理增删改 ============

    @Override
    @Transactional
    public void createClass(Map<String, Object> classData) {
        try {
            // 获取下一个classId
            Integer maxClassId = jdbcTemplate.queryForObject("SELECT MAX(classId) FROM class", Integer.class);
            Integer newClassId = (maxClassId == null ? 0 : maxClassId) + 1;

            String className = (String) classData.get("className");
            String grade = (String) classData.get("grade");
            Integer teacherId = (Integer) classData.get("teacherId");

            // 使用参数化查询防止SQL注入
            jdbcTemplate.update(
                "INSERT INTO class (classId, className, grade) VALUES (?, ?, ?)",
                newClassId, className, grade
            );

            // 如果选择了班主任，更新teacher表的classId
            if (teacherId != null) {
                jdbcTemplate.update(
                    "UPDATE teacher SET classId = ? WHERE teacherId = ?",
                    newClassId, teacherId
                );
            }

            log.info("创建班级成功，classId: {}, className: {}, grade: {}, teacherId: {}", newClassId, className, grade, teacherId);
        } catch (Exception e) {
            log.error("创建班级失败", e);
            throw new RuntimeException("创建班级失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public void updateClass(Integer classId, Map<String, Object> classData) {
        try {
            List<Object> params = new ArrayList<>();
            StringBuilder sql = new StringBuilder("UPDATE class SET ");

            if (classData.containsKey("className")) {
                sql.append("className = ?, ");
                params.add(classData.get("className"));
            }
            if (classData.containsKey("grade")) {
                sql.append("grade = ?, ");
                params.add(classData.get("grade"));
            }

            // 移除最后的逗号
            if (sql.charAt(sql.length() - 2) == ',') {
                sql.setLength(sql.length() - 2);
            }

            sql.append(" WHERE classId = ?");
            params.add(classId);

            jdbcTemplate.update(sql.toString(), params.toArray());
            log.info("更新班级成功，classId: {}", classId);
        } catch (Exception e) {
            log.error("更新班级失败", e);
            throw new RuntimeException("更新班级失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public void deleteClass(Integer classId) {
        try {
            // 检查班级是否存在
            Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM class WHERE classId = " + classId, Integer.class);
            if (count == null || count == 0) {
                throw new RuntimeException("班级不存在");
            }

            // 检查是否有学生在这个班级
            Integer studentCount = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM student WHERE classId = " + classId, Integer.class);

            if (studentCount != null && studentCount > 0) {
                log.warn("班级 {} 中还有 {} 名学生，不能直接删除", classId, studentCount);
                throw new RuntimeException("该班级中还有学生，请先转移或删除学生后再删除班级");
            }

            // 删除班级
            jdbcTemplate.execute("DELETE FROM class WHERE classId = " + classId);
            log.info("删除班级成功，classId: {}", classId);
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            log.error("删除班级失败", e);
            throw new RuntimeException("删除班级失败: " + e.getMessage());
        }
    }

    @Override
    public List<Map<String, Object>> getAllTeachers() {
        try {
            // 查询teacher表，只返回需要的字段
            return jdbcTemplate.queryForList(
                "SELECT teacherId, name FROM teacher ORDER BY teacherId"
            );
        } catch (Exception e) {
            log.error("获取教师列表失败", e);
            return new ArrayList<>();
        }
    }

    // ============ 学生管理增删改 ============

    @Override
    public List<Map<String, Object>> getAllStudents() {
        try {
            return studentMapper.findAllWithDetails();
        } catch (Exception e) {
            log.error("获取所有学生失败", e);
            return new ArrayList<>();
        }
    }

    @Override
    @Transactional
    public void createStudent(Map<String, Object> studentData) {
        try {
            // 获取下一个studentId
            Integer maxStudentId = jdbcTemplate.queryForObject("SELECT MAX(studentId) FROM student", Integer.class);
            Integer newStudentId = (maxStudentId == null ? 0 : maxStudentId) + 1;

            String name = (String) studentData.get("name");
            Integer classId = (Integer) studentData.get("classId");
            String password = (String) studentData.get("password");
            if (password == null) password = "123456";

            jdbcTemplate.execute(String.format(
                "INSERT INTO student (studentId, name, password, classId) VALUES (%d, '%s', '%s', %d)",
                newStudentId, name, password, classId
            ));
            log.info("添加学生成功，studentId: {}, name: {}", newStudentId, name);
        } catch (Exception e) {
            log.error("添加学生失败", e);
            throw new RuntimeException("添加学生失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public void updateStudent(Integer studentId, Map<String, Object> studentData) {
        try {
            StringBuilder sql = new StringBuilder("UPDATE student SET ");

            if (studentData.containsKey("name")) {
                sql.append("name = '").append(studentData.get("name")).append("', ");
            }
            if (studentData.containsKey("password")) {
                sql.append("password = '").append(studentData.get("password")).append("', ");
            }
            if (studentData.containsKey("classId")) {
                sql.append("classId = ").append(studentData.get("classId")).append(", ");
            }

            // 移除最后的逗号
            if (sql.charAt(sql.length() - 2) == ',') {
                sql.setLength(sql.length() - 2);
            }

            sql.append(" WHERE studentId = ").append(studentId);

            jdbcTemplate.execute(sql.toString());
            log.info("更新学生成功，studentId: {}", studentId);
        } catch (Exception e) {
            log.error("更新学生失败", e);
            throw new RuntimeException("更新学生失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public void deleteStudent(Integer studentId) {
        try {
            jdbcTemplate.execute("DELETE FROM student WHERE studentId = " + studentId);
            log.info("删除学生成功，studentId: {}", studentId);
        } catch (Exception e) {
            log.error("删除学生失败", e);
            throw new RuntimeException("删除学生失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public void batchCreateStudents(List<Map<String, Object>> students) {
        try {
            for (Map<String, Object> student : students) {
                createStudent(student);
            }
            log.info("批量添加学生成功，数量: {}", students.size());
        } catch (Exception e) {
            log.error("批量添加学生失败", e);
            throw new RuntimeException("批量添加学生失败: " + e.getMessage());
        }
    }
}