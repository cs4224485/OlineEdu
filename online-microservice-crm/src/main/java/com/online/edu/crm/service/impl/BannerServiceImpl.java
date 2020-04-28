package com.online.edu.crm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.online.edu.crm.entity.Banner;
import com.online.edu.crm.mapper.BannerMapper;
import com.online.edu.crm.service.BannerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 首页banner表 服务实现类
 * </p>
 *
 * @author harry
 * @since 2020-03-29
 */
@Service
public class BannerServiceImpl extends ServiceImpl<BannerMapper, Banner> implements BannerService {
    @Override
    public List<Banner> selectIndex() {
        QueryWrapper<Banner> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("sort");
        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public void pageBanner(Page<Banner> bannerPage) {
        baseMapper.selectPage(bannerPage, null);
    }

}
