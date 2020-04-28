package com.online.edu.order.client;

import com.online.edu.common.vo.CourseWebVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Component
@FeignClient("online-edu")
public interface EduClient {
    @GetMapping("/edu/course/getDto/{courseId}")
    CourseWebVo getCourseInfoDto(@PathVariable("courseId") String courseId);
}
