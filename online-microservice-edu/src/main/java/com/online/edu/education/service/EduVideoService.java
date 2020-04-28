package com.online.edu.education.service;

import com.online.edu.education.entity.EduVideo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.online.edu.education.entity.form.VideoInfoForm;

/**
 * <p>
 * 课程视频 服务类
 * </p>
 *
 * @author harry
 * @since 2020-03-09
 */
public interface EduVideoService extends IService<EduVideo> {

    boolean removeByCourseId(String id);

    boolean getCountByChapterId(String id);

    boolean saveVideoInfo(VideoInfoForm videoInfoForm);

    VideoInfoForm getVideoInformById(String id);

    void updateVideoInfoForm(VideoInfoForm videoInfoForm);

    boolean removeVideoById(String id);
}
