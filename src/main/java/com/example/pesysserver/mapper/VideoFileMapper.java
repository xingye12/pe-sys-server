package com.example.pesysserver.mapper;

import com.example.pesysserver.pojo.entity.VideoFile;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface VideoFileMapper {

    @Select("SELECT * FROM video ORDER BY vidoeId DESC")
    List<VideoFile> findAll();

    @Select("SELECT * FROM video WHERE vidoeId = #{vidoeId}")
    VideoFile findById(Integer vidoeId);

    @Insert("INSERT IGNORE INTO video (vidoeId, name) VALUES " +
            "(1, '800米跑_张小明.mp4'), " +
            "(2, '立定跳远_李小红.mp4'), " +
            "(3, '仰卧起坐_王小强.mp4'), " +
            "(4, '50米跑_赵小军.mp4'), " +
            "(5, '跳绳_黄小芳.mp4')")
    int insertSampleData();

    @Select("SELECT COUNT(*) FROM video")
    int countVideos();

    @Select("SELECT COUNT(*) FROM video WHERE name LIKE '%PENDING%'")
    int countPendingAnalysis();
}