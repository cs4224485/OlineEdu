package com.online.edu.education.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.online.edu.education.entity.EduTeacher;
import com.online.edu.education.entity.query.TeacherQuery;
import com.online.edu.education.service.impl.EduTeacherServiceImpl;
import com.online.edu.common.exception.MyException;
import com.online.edu.common.vo.R;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/edu/teacher")
@CrossOrigin
public class EduTeacherController {

    @Autowired
    EduTeacherServiceImpl eduTeacherService;

    // 多条件组合代分页
    @PostMapping("conditionList/{page}/{limit}")
    public R getMoreConditionPageList(@PathVariable(required = true) Long page , @PathVariable(required = true) Long limit,
                                      @RequestBody(required = false) TeacherQuery teacherQuery){
        if (page <=0 || limit <=0){
            throw new MyException(10000, "页码传入错误");
        }
        Page<EduTeacher> eduTeacherPage = new Page<>(page, limit);
        // 抵用service的方法实现条件查询带分页
        eduTeacherService.pageListCondition(eduTeacherPage, teacherQuery);
        // 从pageTeacher对象里面获取分页数据
        List<EduTeacher> records = eduTeacherPage.getRecords();
        long total = eduTeacherPage.getTotal();
        return R.ok().data("total", total).data("rows", records);
    }

    @ApiOperation(value = "所有讲师列表")
    @GetMapping
    public R getAllTeacher(){
        List<EduTeacher> eduTeachers = eduTeacherService.list(null);
        R response = R.ok().data("teacherItems", eduTeachers);
        return response;
    }

    @ApiOperation(value = "根据ID删除讲师")
    @DeleteMapping("{id}")
    public R deleteTeacherById(@PathVariable("id") String teacherID){
        boolean b = eduTeacherService.removeById(teacherID);
        if(b){
            return R.ok().success(b);
        }else {
            return R.error();
        }
    }

    @ApiOperation(value = "新增教师")
    @PostMapping("/add")
    public R addTeacher(@RequestBody @ApiParam(name="education", value = "对象讲师") EduTeacher eduTeacher){
        eduTeacherService.save(eduTeacher);
        return R.ok();
    }

    @ApiOperation(value = "根据ID查找教师")
    @GetMapping("{id}")
    public R getTeacherById(@ApiParam(name="id",value = "教师ID") @PathVariable("id") String teacherId){
        EduTeacher teacher = eduTeacherService.getById(teacherId);
        return R.ok().data("teacher", teacher);
    }

    @ApiOperation(value = "根据Id更新")
    @PutMapping("{id}")
    public R updateTeacherById(@ApiParam(name="id", value = "教师ID") @PathVariable("id") String teacherId,
                               @ApiParam(name="education", value = "对象讲师") @RequestBody EduTeacher eduTeacher){
        eduTeacher.setId(teacherId);
        boolean b = eduTeacherService.updateById(eduTeacher);
        if(b){
            return R.ok();
        }else {
            return R.error();
        }
    }


}

