package com.online.edu.education.controller;


import com.online.edu.common.constants.ResultCodeEnum;
import com.online.edu.common.exception.MyException;
import com.online.edu.common.vo.R;
import com.online.edu.common.vo.SubjectNestedVo;
import com.online.edu.education.entity.EduSubject;
import com.online.edu.education.service.EduSubjectService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;


@RestController
@Api(description = "课程分类管理")
@CrossOrigin //跨域
@RequestMapping("/admin/edu/subject")
public class EduSubjectController {

    @Autowired
    EduSubjectService eduSubjectService;

    @ApiOperation(value = "Excel批量导入")
    @PostMapping("import")
    public R batchImport(
            @ApiParam(name = "file", value = "Excel文件", required = true)
            @RequestParam("file") MultipartFile file) {
        try {
            List<String> errorMsg = eduSubjectService.batchImport(file);
            if (errorMsg.size() == 0) {
                return R.ok().message("批量导入成功");
            } else {
                return R.error().message("部分数据导入失败").data("errorMsgList", errorMsg);
            }
        } catch (Exception e) {
            //无论哪种异常，只要是在excel导入时发生的，一律用转成自定义异常抛出
            throw new MyException(ResultCodeEnum.EXCEL_DATA_IMPORT_ERROR);
        }
    }

    @ApiOperation(value = "嵌套数据列表")
    @GetMapping("")
    public R nestedList() {
        List<SubjectNestedVo> subjectNestedVoList = eduSubjectService.nestedList();
        return R.ok().data("items", subjectNestedVoList);
    }

    @ApiOperation(value = "新增一级分类")
    @PostMapping("save-level-one")
    public R saveLevelOne(
            @ApiParam(name = "subject", value = "课程分类对象", required = true)
            @RequestBody EduSubject subject) {
        String title = subject.getTitle();
        EduSubject eduSubject = eduSubjectService.subjectIsExist(title);
        if (eduSubject == null) {
            String result = eduSubjectService.createSubject("0", title, 0);
            if (StringUtils.isNotBlank(result)) {
                return R.ok();
            } else {
                return R.error().message("保存失败");
            }
        } else {
            return R.error().message("该分类以存在");
        }

    }

    @ApiOperation(value = "新增二级分类")
    @PostMapping("save-level-two")
    public R saveLevelTwo(
            @ApiParam(name = "subject", value = "课程分类对象", required = true)
            @RequestBody EduSubject subject) {
        String pid = subject.getParentId();
        String title = subject.getTitle();
        EduSubject levelTwoSubjectIsExist = eduSubjectService.levelTwoSubjectIsExist(pid, title);
        if(levelTwoSubjectIsExist == null){
            String result = eduSubjectService.createSubject(subject.getParentId(), subject.getTitle(), 0);
            if (StringUtils.isNotBlank(result)) {
                return R.ok();
            } else {
                return R.error().message("保存失败");
            }
        }else {
            return R.error().message("该分类以存在");
        }
    }

    @ApiOperation(value = "根据Id删除标题")
    @DeleteMapping("{id}")
    public R deleteSubject(@PathVariable("id") String id) {
        boolean isDelete = eduSubjectService.deleteById(id);
        if (isDelete) {
            return R.ok();
        } else {
            return R.error();
        }
    }
}

