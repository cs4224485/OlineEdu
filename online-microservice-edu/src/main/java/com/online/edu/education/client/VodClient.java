package com.online.edu.education.client;

import com.online.edu.common.vo.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient("online-vod")
@Component
public interface VodClient {
    @DeleteMapping(value = "/admin/vod/video/{videoId}")
    R removeVideo(@PathVariable("videoId") String videoId);

    @DeleteMapping(value = "/admin/vod/video/delete-batch")
    R removeVideoList(@RequestParam("videoIdList") List<String> videoIdList);
}
