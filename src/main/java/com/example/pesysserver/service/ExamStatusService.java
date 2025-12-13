package com.example.pesysserver.service;

import com.example.pesysserver.pojo.entity.ExamTask;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 考试状态管理服务
 */
public interface ExamStatusService {

    /**
     * 检查并更新所有考试的状态
     */
    void updateAllExamStatuses();

    /**
     * 根据当前时间确定考试应该的状态
     * @param beginTime 考试开始时间
     * @param endTime 考试结束时间
     * @param currentTime 当前时间
     * @return 应该的状态
     */
    String determineExamStatus(LocalDateTime beginTime, LocalDateTime endTime, LocalDateTime currentTime);

    /**
     * 手动更新指定考试的状态
     * @param examId 考试ID
     * @param newState 新状态
     * @return 是否更新成功
     */
    boolean updateExamStatus(Integer examId, String newState);

    /**
     * 获取需要更新状态的考试列表
     * @return 考试列表
     */
    List<ExamTask> getExamsNeedingStatusUpdate();
}