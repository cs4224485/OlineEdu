package com.online.edu.education.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.online.edu.education.entity.EduTeacher;
import com.baomidou.mybatisplus.extension.service.IService;
import com.online.edu.education.entity.query.TeacherQuery;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 讲师 服务类
 * </p>
 *
 * @author Helen
 * @since 2020-02-22
 */
public interface EduTeacherService extends IService<EduTeacher> {

    void pageListCondition(Page<EduTeacher> eduTeacherPage, TeacherQuery teacherQuery);

    Map<String, Object> pageListWeb(Page<EduTeacher> pageParam);

    List<EduTeacher> getIndexTeacher();
}
