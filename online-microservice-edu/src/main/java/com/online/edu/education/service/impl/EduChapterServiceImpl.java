package com.online.edu.education.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.online.edu.education.mapper.EduChapterMapper;
import com.online.edu.common.exception.MyException;
import com.online.edu.common.vo.ChapterVo;
import com.online.edu.common.vo.VideoVo;
import com.online.edu.education.entity.EduChapter;
import com.online.edu.education.entity.EduVideo;
import com.online.edu.education.service.EduChapterService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.online.edu.education.service.EduVideoService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class EduChapterServiceImpl extends ServiceImpl<EduChapterMapper, EduChapter> implements EduChapterService {

    @Autowired
    EduVideoService videoService;

    @Override
    public boolean removeByCourseId(String id) {
        QueryWrapper<EduChapter> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("course_id", id);
        Integer count = baseMapper.delete(queryWrapper);
        return null != count && count > 0;
    }

    @Override
    public boolean removeChapterById(String id) {
        //根据id查询是否存在视频，如果有则提示用户尚有子节点
        if(videoService.getCountByChapterId(id)){
            throw new MyException(20001,"该分章节下存在视频课程，请先删除视频课程");
        }
        Integer result = baseMapper.deleteById(id);
        return null != result && result > 0;
    }

    @Override
    public List<ChapterVo> nestedList(String courseId) {
        List<ChapterVo> chapterVoList = new ArrayList<>();

        QueryWrapper<EduChapter> eduChapterQueryWrapper = new QueryWrapper<>();
        eduChapterQueryWrapper.eq("course_id", courseId);
        List<EduChapter> eduChapters = baseMapper.selectList(eduChapterQueryWrapper);

        QueryWrapper<EduVideo> eduVideoQueryWrapper = new QueryWrapper<>();
        eduChapterQueryWrapper.eq("course_id", courseId);
        List<EduVideo> eduVideos = videoService.list(eduVideoQueryWrapper);
        for (int i = 0; i < eduChapters.size(); i++) {
            ChapterVo chapterVo = new ChapterVo();
            BeanUtils.copyProperties(eduChapters.get(i), chapterVo);
            String chapterId = eduChapters.get(i).getId();
            List<VideoVo> videoVoList = new ArrayList<>();
            chapterVoList.add(chapterVo);
            for (int j = 0; j < eduVideos.size(); j++) {
                if (eduVideos.get(j).getChapterId().equals(chapterId)){
                    VideoVo videoVo = new VideoVo();
                    BeanUtils.copyProperties(eduVideos.get(j), videoVo);
                    videoVoList.add(videoVo);
                }
            }

            chapterVo.setChildren(videoVoList);
        }

        return chapterVoList;
    }
}
