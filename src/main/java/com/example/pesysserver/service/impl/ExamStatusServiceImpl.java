package com.example.pesysserver.service.impl;

import com.example.pesysserver.mapper.ExamTaskMapper;
import com.example.pesysserver.pojo.entity.ExamTask;
import com.example.pesysserver.service.ExamStatusService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Collections;
import java.util.stream.Collectors;

/**
 * 考试状态管理服务实现
 */
@Slf4j
@Service
public class ExamStatusServiceImpl implements ExamStatusService {

    @Autowired
    private ExamTaskMapper examTaskMapper;

    @Override
    @Transactional
    public void updateAllExamStatuses() {
        try {
            log.info("开始检查和更新考试状态...");

            List<ExamTask> exams = examTaskMapper.findAll();
            LocalDateTime currentTime = LocalDateTime.now();

            int updatedCount = 0;

            for (ExamTask exam : exams) {
                String expectedStatus = determineExamStatus(
                    exam.getBegin_time(),
                    exam.getEnd_time(),
                    currentTime
                );

                // 只有当状态需要更新时才执行更新
                if (!expectedStatus.equals(exam.getState())) {
                    exam.setState(expectedStatus);
                    int updateResult = examTaskMapper.update(exam);

                    if (updateResult > 0) {
                        updatedCount++;
                        log.info("考试 [ID: {}, 名称: {}] 状态已更新为: {}",
                            exam.getExamId(), exam.getType(), expectedStatus);
                    }
                }
            }

            log.info("考试状态更新完成，共更新了 {} 个考试", updatedCount);

        } catch (Exception e) {
            log.error("更新考试状态时发生错误", e);
        }
    }

    @Override
    public String determineExamStatus(LocalDateTime beginTime, LocalDateTime endTime, LocalDateTime currentTime) {
        if (currentTime.isBefore(beginTime)) {
            return "未开始";
        } else if (currentTime.isBefore(endTime)) {
            return "正在进行";
        } else {
            return "已结束";
        }
    }

    @Override
    @Transactional
    public boolean updateExamStatus(Integer examId, String newState) {
        try {
            ExamTask exam = examTaskMapper.findById(examId);
            if (exam == null) {
                log.warn("考试ID {} 不存在", examId);
                return false;
            }

            // 验证状态值是否有效
            if (!isValidStatus(newState)) {
                log.warn("无效的考试状态: {}", newState);
                return false;
            }

            exam.setState(newState);
            int updateResult = examTaskMapper.update(exam);

            if (updateResult > 0) {
                log.info("手动更新考试 [ID: {}, 名称: {}] 状态为: {}",
                    examId, exam.getType(), newState);
                return true;
            } else {
                log.warn("更新考试状态失败，ID: {}", examId);
                return false;
            }

        } catch (Exception e) {
            log.error("手动更新考试状态时发生错误，考试ID: {}", examId, e);
            return false;
        }
    }

    @Override
    public List<ExamTask> getExamsNeedingStatusUpdate() {
        try {
            List<ExamTask> allExams = examTaskMapper.findAll();
            LocalDateTime currentTime = LocalDateTime.now();

            return allExams.stream()
                .filter(exam -> {
                    String expectedStatus = determineExamStatus(
                        exam.getBegin_time(),
                        exam.getEnd_time(),
                        currentTime
                    );
                    return !expectedStatus.equals(exam.getState());
                })
                .collect(Collectors.toList());

        } catch (Exception e) {
            log.error("获取需要更新状态的考试列表时发生错误", e);
            return Collections.emptyList();
        }
    }

    /**
     * 验证状态值是否有效
     */
    private boolean isValidStatus(String status) {
        return "未开始".equals(status) ||
               "正在进行".equals(status) ||
               "已结束".equals(status);
    }
}