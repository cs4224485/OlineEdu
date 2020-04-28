package com.online.edu.education.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.online.edu.education.entity.form.CourseInfoForm;
import com.online.edu.common.vo.CoursePublishVo;
import com.online.edu.common.vo.CourseWebVo;
import com.online.edu.education.entity.EduCourse;
import com.baomidou.mybatisplus.extension.service.IService;
import com.online.edu.education.entity.query.CourseQuery;

import java.util.List;
import java.util.Map;


public interface EduCourseService extends IService<EduCourse> {

    String saveCourseInfo(CourseInfoForm courseInfoForm);

    CourseInfoForm getCourseInfoFormById(String id);

    void updateCourseInfoById(CourseInfoForm courseInfoForm);

    void pageQuery(Page<EduCourse> pageParam, CourseQuery courseQuery);

    boolean removeCourseById(String id);

    CoursePublishVo getCoursePublishVoById(String id);

    Boolean publishCourse(String id);

    List<EduCourse> selectByTeacherId(String id);

    Map<String, Object> pageListWeb(Page<EduCourse> pageParam);
    CourseWebVo selectCourseWebVoById(String id);

    List<EduCourse> getIndexCourse();


    CourseWebVo getCourseDtoById(String courseId);
}
