package com.online.edu.oss.service;

import com.aliyun.oss.OSSClient;
import com.online.edu.common.constants.ResultCodeEnum;
import com.online.edu.common.exception.MyException;
import com.online.edu.oss.Utils.ConstantPropertiesUtil;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {
    //获取阿里云存储相关常量
    String endPoint = ConstantPropertiesUtil.END_POINT;
    String accessKeyId = ConstantPropertiesUtil.ACCESS_KEY_ID;
    String accessKeySecret = ConstantPropertiesUtil.ACCESS_KEY_SECRET;
    String bucketName = ConstantPropertiesUtil.BUCKET_NAME;
    String fileHost = ConstantPropertiesUtil.FILE_HOST;

    @Override
    public String upload(MultipartFile file, String host) {
        String uploadUrl = "";
        try {
            // 判断oss实例是否存在：如果不存在则创建，如果存在则获取
            OSSClient ossClient = new OSSClient(endPoint, accessKeyId, accessKeySecret);
            if(!ossClient.doesBucketExist(bucketName)){
                // 创建bucket
                ossClient.createBucket(bucketName);
                // 设置oss实例的访问权限:公共读
            }
            // 获上传文件流
            InputStream inputStream = file.getInputStream();
            // 构建日期路径: avatar /2019/02/25/文件名
            String filePath = new DateTime().toString("yyyy/MM/dd");
            // 文件名:uuid.扩展名
            String original = file.getOriginalFilename();
            String fileName = UUID.randomUUID().toString();
            String fileType = original.substring(original.lastIndexOf("."));
            String newName = fileName + fileType;
            if(StringUtils.isNotBlank(host)){
                fileHost = host;
            }
            String fileUrl = fileHost + "/" + filePath + "/" +newName;
            // 文件上传到阿里云
            ossClient.putObject(bucketName, fileUrl, inputStream);
            // 关闭OSSClient
            ossClient.shutdown();
            // 获取url地址
            uploadUrl = "http://" + bucketName +"." + endPoint + "/" +fileUrl;
        } catch (IOException e) {
            throw new MyException(ResultCodeEnum.FILE_UPLOAD_ERROR);
        }

        return uploadUrl;
    }
}
