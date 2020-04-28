package com.online.edu.education.service;

import com.online.edu.common.vo.SubjectNestedVo;
import com.online.edu.education.entity.EduSubject;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 课程科目 服务类
 * </p>
 *
 * @author harry
 * @since 2020-02-29
 */
public interface EduSubjectService extends IService<EduSubject> {
    List<String> batchImport(MultipartFile file) throws Exception;

    List<SubjectNestedVo> nestedList();
    String createSubject(String pid, String title, int order);
    boolean deleteById(String id);
    EduSubject subjectIsExist(String title);
    EduSubject levelTwoSubjectIsExist(String title, String pid);
}
