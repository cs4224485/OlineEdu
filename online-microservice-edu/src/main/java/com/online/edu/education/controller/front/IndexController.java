package com.online.edu.education.controller.front;

import com.online.edu.education.entity.EduCourse;
import com.online.edu.education.entity.EduTeacher;
import com.online.edu.education.service.EduCourseService;
import com.online.edu.education.service.EduTeacherService;
import com.online.edu.common.vo.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/edu/index")
@CrossOrigin
public class IndexController {
    @Autowired
    private EduCourseService courseService;

    @Autowired
    private EduTeacherService teacherService;

    // 查询前八条热门课程，查询前条名师
    @GetMapping()
    public R index(){
       List<EduCourse> eduList = courseService.getIndexCourse();
       List<EduTeacher> teacherList = teacherService.getIndexTeacher();
       return R.ok().data("eduList", eduList).data("teacherList", teacherList).data("courseList", eduList);
    }
}
