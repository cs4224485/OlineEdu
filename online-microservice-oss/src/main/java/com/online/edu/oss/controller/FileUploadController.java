package com.online.edu.oss.controller;

import com.online.edu.common.vo.R;
import com.online.edu.oss.Utils.ConstantPropertiesUtil;
import com.online.edu.oss.service.FileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Api(description="阿里云文件管理")
@CrossOrigin //跨域
@RestController
@RequestMapping("/admin/oss/file")
public class FileUploadController {
    @Autowired
    FileService fileService;
    @ApiOperation(value = "文件上传")
    @PostMapping("upload")
    public R upload(@ApiParam(name = "file", value = "文件", required = true)
                    @RequestParam("file") MultipartFile file,
                    @ApiParam(name = "host", value = "文件上传路径", required = false)
                    @RequestParam(value = "host", required = false) String host) {
        /**
         * 文件上传
         */

        String uploadUrl = fileService.upload(file, host);
        return R.ok().message("文件上传成功").data("url", uploadUrl);
    }

}
