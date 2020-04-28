package com.online.edu.education.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.online.edu.common.utils.ExcelImportUtil;
import com.online.edu.common.vo.SubjectNestedVo;
import com.online.edu.common.vo.SubjectVo;
import com.online.edu.education.entity.EduSubject;
import com.online.edu.education.mapper.EduSubjectMapper;
import com.online.edu.education.service.EduSubjectService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程科目 服务实现类
 * </p>
 *
 * @author harry
 * @since 2020-02-29
 */
@Transactional
@Service
public class EduSubjectServiceImpl extends ServiceImpl<EduSubjectMapper, EduSubject> implements EduSubjectService {
    @Override
    public List<String> batchImport(MultipartFile file) throws Exception {
        //错误消息列表
        List<String> errorMsg = new ArrayList<>();
        ExcelImportUtil excelImportUtil = new ExcelImportUtil(file.getInputStream());
        Sheet sheet = excelImportUtil.getSheet(0);
        int rowCount = sheet.getPhysicalNumberOfRows();
        if(rowCount <=1){
            errorMsg.add("请填写数据");
            return errorMsg;
        }
        for (int rowNum = 1; rowNum < rowCount; rowNum++){
            Row rowData = sheet.getRow(rowNum);
            if(rowData != null){
                // 行不为空
                // 获取一级分类
                Cell levelOneCell = rowData.getCell(0);
                String levelOneValue = cellValue(levelOneCell, excelImportUtil);
                if(StringUtils.isEmpty(levelOneValue)){
                    errorMsg.add("第" + rowNum + "行一级分类为空");
                    continue;
                }
                //判断一级分类是否重复
                //将一级分类存入数据库
                EduSubject subjectIsExist = subjectIsExist(levelOneValue);
                String parentId = "0";
                if(subjectIsExist == null){
                    parentId = createSubject(parentId, levelOneValue, rowNum);
                }else {
                    parentId = subjectIsExist.getId();
                }
                //获取二级分类
                Cell levelTwoCell = rowData.getCell(1);
                String levelTwoValue = cellValue(levelTwoCell , excelImportUtil);
                if (StringUtils.isEmpty(levelTwoValue)) {
                    errorMsg.add("第" + rowNum + "行二级分类为空");
                    continue;
                }
                EduSubject subjectSub = this.levelTwoSubjectIsExist(levelTwoValue, parentId);
                if (subjectSub == null){
                    createSubject(parentId, levelTwoValue, rowNum);
                }
            }
        }
        return errorMsg;
    }

    @Override
    public List<SubjectNestedVo> nestedList() {
        /***
         *  课程标题树形结构列表
         */

        ArrayList<SubjectNestedVo> subjectNestedVos = new ArrayList<>();
        // 获取一级分类
        QueryWrapper<EduSubject> eduSubjectQueryWrapper = new QueryWrapper<>();
        eduSubjectQueryWrapper.eq("parent_id", "0").orderByDesc("sort", "id");
        List<EduSubject> eduSubjects = baseMapper.selectList(eduSubjectQueryWrapper);
        for (EduSubject eduSubject : eduSubjects) {
            // 构建标题树形结构
            SubjectNestedVo subjectNestedVo = new SubjectNestedVo();
            BeanUtils.copyProperties(eduSubject,subjectNestedVo);
            // 获取二级分类
            String pid = eduSubject.getId();
            QueryWrapper<EduSubject> levelTowSubjectQuery = new QueryWrapper<>();
            levelTowSubjectQuery.eq("parent_id", pid).orderByDesc("sort", "id");
            List<EduSubject> levelTwoSubject = baseMapper.selectList(levelTowSubjectQuery);
            for (EduSubject subject : levelTwoSubject) {
                SubjectVo subjectVo = new SubjectVo();
                BeanUtils.copyProperties(subject, subjectVo);
                subjectNestedVo.getChildren().add(subjectVo);
            }
            subjectNestedVos.add(subjectNestedVo);
        }

        return subjectNestedVos;
    }

    @Override
    public boolean deleteById(String id) {
        /***
         * 根据ID删除标题
         */
        QueryWrapper<EduSubject> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("parent_id", id);
        Integer count = baseMapper.selectCount(queryWrapper);
        if(count>0){
            return false;
        }
        int result = baseMapper.deleteById(id);
        return result > 0;
    }

    public String createSubject(String pid, String title, int order){
        /***
         * 创建课程标题
         */
        EduSubject subject = new EduSubject();
        subject.setTitle(title);
        subject.setSort(order);
        if (pid.equals("0")){
            // 添加一级分类
            baseMapper.insert(subject);
            return subject.getId();
        }else {
            // 添加二级分类
            subject.setParentId(pid);
            baseMapper.insert(subject);
            return pid;
        }
    }

    public EduSubject subjectIsExist(String title){
        /***
         * 判断一级标题是否存在
         */
        QueryWrapper<EduSubject> eduSubjectQueryWrapper = new QueryWrapper<>();
        eduSubjectQueryWrapper.eq("title", title).eq("parent_id", "0");
        return baseMapper.selectOne(eduSubjectQueryWrapper);
    }

    public EduSubject levelTwoSubjectIsExist(String title, String pid){
        /***
         * 判断二级标题是否存在
         */
        QueryWrapper<EduSubject> eduSubjectQueryWrapper = new QueryWrapper<>();
        eduSubjectQueryWrapper.eq("title", title).eq("parent_id", pid);
        return baseMapper.selectOne(eduSubjectQueryWrapper);
    }

    public String cellValue(Cell cell, ExcelImportUtil excelImportUtil){
        /***
         * 获取cell中的值
         */
        String value = "";
        if(cell!=null){
            value = excelImportUtil.getCellValue(cell).trim();
        }
        return value;
    }
}
