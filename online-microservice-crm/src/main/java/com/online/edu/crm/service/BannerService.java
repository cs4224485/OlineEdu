package com.online.edu.crm.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.online.edu.crm.entity.Banner;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 首页banner表 服务类
 * </p>
 *
 * @author harry
 * @since 2020-03-29
 */
public interface BannerService extends IService<Banner>  {

    List<Banner> selectIndex();

    void pageBanner(Page<Banner> bannerPage);
}
