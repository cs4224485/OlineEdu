package com.online.edu.crm.controller;

import com.online.edu.common.vo.R;
import com.online.edu.crm.entity.Banner;
import com.online.edu.crm.service.BannerService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/educms/banner")
@CrossOrigin
public class BannerApiController {

    @Autowired
    BannerService bannerService;

    @Cacheable(value = "banner", key = "'selectIndexList'")
    @ApiOperation(value = "获取首页banner")
    @GetMapping("getAllBanner")
    public R index(){
        List<Banner> list = bannerService.selectIndex();
        return R.ok().data("bannerList", list);
    }
}
