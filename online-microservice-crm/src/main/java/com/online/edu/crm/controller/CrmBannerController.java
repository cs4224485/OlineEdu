package com.online.edu.crm.controller;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.online.edu.common.vo.R;
import com.online.edu.crm.entity.Banner;
import com.online.edu.crm.service.BannerService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/education/crm-banner")
@CrossOrigin
public class CrmBannerController {

    @Autowired
    BannerService crmBannerService;

    @GetMapping("{page}/{limit}")
    public R index(@PathVariable Long page, @PathVariable Long limit){
        Page<Banner> bannerPage = new Page<>(page, limit);
        crmBannerService.pageBanner(bannerPage);
        return R.ok().data("total", bannerPage.getTotal()).data("items", bannerPage.getRecords());
    }

    @ApiOperation(value = "获取Banner")
    @GetMapping("get/{id}")
    public R get(@PathVariable String id) {
        Banner banner = crmBannerService.getById(id);
        return R.ok().data("item", banner);
    }

    @ApiOperation(value = "修改Banner")
    @PutMapping("update")
    public R updateById(@RequestBody Banner banner) {
        crmBannerService.updateById(banner);
        return R.ok();
    }
    @DeleteMapping("{id}")
    public R deleteById(@PathVariable String id){
        crmBannerService.removeById(id);
        return R.ok();
    }

}

