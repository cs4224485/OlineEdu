package com.online.edu.education.mapper;

import com.online.edu.common.vo.CoursePublishVo;
import com.online.edu.common.vo.CourseWebVo;
import com.online.edu.education.entity.EduCourse;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;


import javax.websocket.server.PathParam;


public interface EduCourseMapper extends BaseMapper<EduCourse> {
    CoursePublishVo selectCoursePublishVoById(String id);

    CourseWebVo selectCourseWebVoById(@PathParam("id") String courseId);
}
