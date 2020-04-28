package com.online.edu.education.service;

import com.online.edu.education.entity.EduChapter;
import com.online.edu.common.vo.ChapterVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface EduChapterService extends IService<EduChapter> {

    boolean removeByCourseId(String id);
    List<ChapterVo> nestedList(String courseId);

    boolean removeChapterById(String id);
}
