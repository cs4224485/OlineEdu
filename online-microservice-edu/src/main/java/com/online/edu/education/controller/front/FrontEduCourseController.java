package com.online.edu.education.controller.front;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.online.edu.common.utils.JwtUtils;
import com.online.edu.education.client.OrderClient;
import com.online.edu.education.entity.form.CourseInfoForm;
import com.online.edu.education.service.EduCourseService;
import com.online.edu.common.vo.ChapterVo;
import com.online.edu.common.vo.CourseWebVo;
import com.online.edu.common.vo.R;
import com.online.edu.education.entity.EduCourse;
import com.online.edu.education.service.EduChapterService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController()
@RequestMapping("/edu/course")
public class FrontEduCourseController {
    @Autowired
    private EduCourseService courseService;

    @Autowired
    private EduChapterService chapterService;

    @Autowired
    private OrderClient orderClient;

    @ApiOperation(value = "分页课程列表")
    @GetMapping(value = "{page}/{limit}")
    public R pageList(
            @ApiParam(name = "page", value = "当前页码", required = true)
            @PathVariable Long page,

            @ApiParam(name = "limit", value = "每页记录数", required = true)
            @PathVariable Long limit){

        Page<EduCourse> pageParam = new Page<EduCourse>(page, limit);

        Map<String, Object> map = courseService.pageListWeb(pageParam);

        return  R.ok().data(map);
    }

    @ApiOperation(value = "根据ID查询课程")
    @GetMapping(value = "{courseId}")
    public R getById(
            @ApiParam(name = "courseId", value = "课程ID", required = true)
            @PathVariable String courseId, HttpServletRequest request){

        //查询课程信息和讲师信息
        CourseWebVo courseWebVo = courseService.selectCourseWebVoById(courseId);

        //查询当前课程的章节信息
        List<ChapterVo> chapterVoList = chapterService.nestedList(courseId);

        String memberId = JwtUtils.getMemberIdByJwtToken(request);
        //远程调用，判断课程是否被购买
        boolean buyCourse = false;
        if(StringUtils.isNotEmpty(memberId)){
            buyCourse = orderClient.isBuyCourse(JwtUtils.getMemberIdByJwtToken(request), courseId);
        }
  
        return R.ok().data("course", courseWebVo).data("chapterVoList", chapterVoList).data("isbuy",buyCourse);
    }

    //根据课程id查询课程信息
    @GetMapping("getDto/{courseId}")
    public CourseWebVo getCourseInfoApi(@PathVariable String courseId){
        return courseService.getCourseDtoById(courseId);
    }

}
