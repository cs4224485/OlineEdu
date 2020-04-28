package com.online.edu.education.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.online.edu.education.entity.form.VideoInfoForm;
import com.online.edu.education.mapper.EduVideoMapper;
import com.online.edu.common.exception.MyException;
import com.online.edu.education.client.VodClient;
import com.online.edu.education.entity.EduVideo;
import com.online.edu.education.service.EduVideoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class EduVideoServiceImpl extends ServiceImpl<EduVideoMapper, EduVideo> implements EduVideoService {

    @Autowired
    VodClient vodClient;

    @Override
    public boolean removeByCourseId(String courseId) {
        //根据课程id查询所有视频列表
        QueryWrapper<EduVideo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("course_id", courseId);
        queryWrapper.select("video_source_id");
        List<EduVideo> videoList = baseMapper.selectList(queryWrapper);
        //得到所有视频列表的云端原始视频id
        List<String> videoSourceIdList = new ArrayList<>();
        for (int i = 0; i < videoList.size(); i++) {
            EduVideo video = videoList.get(i);
            String videoSourceId = video.getVideoSourceId();
            if(StringUtils.isNotEmpty(videoSourceId)){
                videoSourceIdList.add(videoSourceId);
            }
        }
        //调用vod服务删除远程视频
        if(videoSourceIdList.size() > 0){
            vodClient.removeVideoList(videoSourceIdList);
        }

        QueryWrapper<EduVideo> queryWrapper2 = new QueryWrapper<>();
        queryWrapper2.eq("course_id", courseId);
        Integer count = baseMapper.delete(queryWrapper2);
        return null != count && count<0;
    }

    @Override
    public boolean saveVideoInfo(VideoInfoForm videoInfoForm) {
        EduVideo video = new EduVideo();
        BeanUtils.copyProperties(videoInfoForm, video);
        return this.save(video);
    }

    @Override
    public VideoInfoForm getVideoInformById(String id) {
        EduVideo eduVideo = this.getById(id);
        if(eduVideo == null){
            throw new MyException(20001, "获取课时信息失败");
        }
        VideoInfoForm videoInfoForm = new VideoInfoForm();
        BeanUtils.copyProperties(eduVideo, videoInfoForm);
        return videoInfoForm;
    }

    @Override
    public void updateVideoInfoForm(VideoInfoForm videoInfoForm) {
        EduVideo eduVideo = new EduVideo();
        BeanUtils.copyProperties(videoInfoForm, eduVideo);
        boolean result = this.updateById(eduVideo);
        if(!result){
            throw new MyException(20001, "课时信息更新失败");
        }
    }

    @Override
    public boolean removeVideoById(String id) {
        // 查询云端视频id
        QueryWrapper<EduVideo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", id).select("video_source_id");
        EduVideo eduVideo = baseMapper.selectOne(queryWrapper);
        String videoSourceId = eduVideo.getVideoSourceId();
        if(!StringUtils.isEmpty(videoSourceId)){
            vodClient.removeVideo(videoSourceId);
        }

        Integer i = baseMapper.deleteById(id);
        return null !=i && i > 0;
    }

    @Override
    public boolean getCountByChapterId(String id) {
        QueryWrapper<EduVideo> eduVideoQueryWrapper = new QueryWrapper<>();
        eduVideoQueryWrapper.eq("chapter_id", id);
        Integer count = baseMapper.selectCount(eduVideoQueryWrapper);
        return null != count && count > 0;
    }
}
