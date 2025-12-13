package com.example.pesysserver.scheduler;

import com.example.pesysserver.pojo.entity.ExamTask;
import com.example.pesysserver.service.ExamStatusService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 考试状态定时任务调度器
 */
@Slf4j
@Component
public class ExamStatusScheduler {

    @Autowired
    private ExamStatusService examStatusService;

    /**
     * 每分钟检查一次考试状态
     * cron表达式: 秒 分 时 日 月 周
     * "0 * * * * *" = 每分钟的第0秒执行
     */
    @Scheduled(cron = "0 * * * * *")
    public void updateExamStatuses() {
        try {
            log.debug("执行定时任务：检查考试状态...");
            examStatusService.updateAllExamStatuses();
        } catch (Exception e) {
            log.error("定时更新考试状态时发生错误", e);
        }
    }

    /**
     * 每小时执行一次状态检查和日志记录
     */
    @Scheduled(cron = "0 0 * * * *")
    public void hourlyStatusCheck() {
        try {
            log.info("执行每小时考试状态检查...");
            List<ExamTask> examsNeedingUpdate = examStatusService.getExamsNeedingStatusUpdate();
            if (!examsNeedingUpdate.isEmpty()) {
                int updateCount = examsNeedingUpdate.size();
            log.info("发现 {} 个考试需要状态更新", updateCount);
            }
        } catch (Exception e) {
            log.error("每小时考试状态检查时发生错误", e);
        }
    }
}