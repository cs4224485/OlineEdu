package com.online.edu.education.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.online.edu.education.entity.form.CourseInfoForm;
import com.online.edu.education.mapper.EduCourseMapper;
import com.online.edu.education.service.EduChapterService;
import com.online.edu.education.service.EduCourseDescriptionService;
import com.online.edu.education.service.EduCourseService;
import com.online.edu.education.service.EduVideoService;
import com.online.edu.common.exception.MyException;
import com.online.edu.common.vo.CoursePublishVo;
import com.online.edu.common.vo.CourseWebVo;
import com.online.edu.education.entity.EduCourse;
import com.online.edu.education.entity.EduCourseDescription;
import com.online.edu.education.entity.query.CourseQuery;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
@Transactional
public class EduCourseServiceImpl extends ServiceImpl<EduCourseMapper, EduCourse> implements EduCourseService {

    @Autowired
    EduCourseDescriptionService eduCourseDescriptionService;

    @Autowired
    EduChapterService chapterService;

    @Autowired
    EduVideoService videoService;



    @Override
    public String saveCourseInfo(CourseInfoForm courseInfoForm) {
        EduCourse eduCourse = new EduCourse();
        eduCourse.setStatus(EduCourse.COURSE_DRAFT);;
        BeanUtils.copyProperties(courseInfoForm, eduCourse);
        boolean  resultCourseInfo = this.save(eduCourse);
        if (!resultCourseInfo){
            throw new MyException(20001,"课程信息保存失败");
        }

        //保存课程详情信息
        String courseId = eduCourse.getId();
        EduCourseDescription eduCourseDescription = new EduCourseDescription();
        eduCourseDescription.setId(courseId);
        eduCourseDescription.setDescription(courseInfoForm.getDescription());
        boolean saveDes = eduCourseDescriptionService.save(eduCourseDescription);
        if(saveDes){
            return courseId;
        }
        return null;
    }

    @Override
    public boolean removeCourseById(String id) {
//      //根据id删除所有视频
        videoService.removeByCourseId(id);
//      //根据id删除所有章节
        chapterService.removeByCourseId(id);

        eduCourseDescriptionService.removeById(id);

        Integer result = baseMapper.deleteById(id);
        return null != result && result > 0;
    }

    @Override
    public CourseInfoForm getCourseInfoFormById(String id) {
        EduCourse eduCourse = baseMapper.selectById(id);
        if (eduCourse == null){
            throw new MyException(20001, "数据不存在");
        }

        EduCourseDescription courseDescription = eduCourseDescriptionService.getById(id);
        if (courseDescription == null){
            throw new MyException(20001, "数据不完整");
        }
        CourseInfoForm courseInfoForm = new CourseInfoForm();
        BeanUtils.copyProperties(eduCourse, courseInfoForm);
        courseInfoForm.setDescription(courseDescription.getDescription());
        return courseInfoForm;
    }

    @Override
    public void updateCourseInfoById(CourseInfoForm courseInfoForm) {
        EduCourse eduCourse = new EduCourse();
        BeanUtils.copyProperties(courseInfoForm, eduCourse);
        int i = baseMapper.updateById(eduCourse);
        if(i == 0){
            throw new MyException(20001, "更新失败");
        }
        EduCourseDescription courseDescription = new EduCourseDescription();
        courseDescription.setDescription(courseInfoForm.getDescription());
        courseDescription.setId(courseInfoForm.getId());
        eduCourseDescriptionService.updateById(courseDescription);
    }

    @Override
    public void pageQuery(Page<EduCourse> pageParam, CourseQuery courseQuery) {
        QueryWrapper<EduCourse> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByAsc("id");
        if(courseQuery == null){
            baseMapper.selectPage(pageParam, null);
            return;
        }
        String title = courseQuery.getTitle();
        String subjectId = courseQuery.getSubjectId();
        String teacherId = courseQuery.getTeacherId();
        String subjectParentId = courseQuery.getSubjectParentId();
        if (StringUtils.isNotBlank(title)){
            queryWrapper.eq("title", title);
        }
        if(StringUtils.isNotBlank(subjectId)){
            queryWrapper.eq("subject_id", subjectId);
        }
        if(StringUtils.isNotBlank(teacherId)){
            queryWrapper.eq("teacher_id", teacherId);
        }
        if(StringUtils.isNotBlank(subjectParentId)){
            queryWrapper.eq("subject_parent_id", subjectParentId);
        }
        baseMapper.selectPage(pageParam, queryWrapper);
    }

    @Override
    public CoursePublishVo getCoursePublishVoById(String id) {
        /***
         * 获取课程发布信息
         */
        return baseMapper.selectCoursePublishVoById(id);
    }

    @Override
    public Boolean publishCourse(String id) {
        EduCourse eduCourse = new EduCourse();
        eduCourse.setId(id);
        eduCourse.setStatus("Normal");
        Integer result = baseMapper.updateById(eduCourse);
        return null != result && result > 0;
    }

    @Override
    public List<EduCourse> selectByTeacherId(String id) {
        /*
        根据教师ID查课程
         */
        QueryWrapper<EduCourse> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("teacher_id", id);
        //按照最后更新时间倒序排列
        queryWrapper.orderByDesc("gmt_modified");
        List<EduCourse> eduCourses = baseMapper.selectList(queryWrapper);
        return eduCourses;
    }

    @Override
    public Map<String, Object> pageListWeb(Page<EduCourse> pageParam) {
        QueryWrapper<EduCourse> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("gmt_modified");
        baseMapper.selectPage(pageParam, queryWrapper);
        List<EduCourse> records = pageParam.getRecords();
        long current = pageParam.getCurrent();
        long pages = pageParam.getPages();
        long size = pageParam.getSize();
        long total = pageParam.getTotal();
        boolean hasNext = pageParam.hasNext();
        boolean hasPrevious = pageParam.hasPrevious();

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("items", records);
        map.put("current", current);
        map.put("pages", pages);
        map.put("size", size);
        map.put("total", total);
        map.put("hasNext", hasNext);
        map.put("hasPrevious", hasPrevious);

        return map;
    }

    @Transactional
    @Override
    public CourseWebVo selectCourseWebVoById(String id) {
        //更新课程浏览数
        EduCourse course = baseMapper.selectById(id);
        course.setViewCount(course.getViewCount() + 1);
        baseMapper.updateById(course);
        //获取课程信息
        return baseMapper.selectCourseWebVoById(id);
    }

    @Override
    @Cacheable(value = "course", key = "'indexCourse'")
    public List<EduCourse> getIndexCourse() {
        QueryWrapper<EduCourse> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("view_count");
        queryWrapper.last("limit 8");
        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public CourseWebVo getCourseDtoById(String courseId) {
        return baseMapper.selectCourseWebVoById(courseId);
    }
}
