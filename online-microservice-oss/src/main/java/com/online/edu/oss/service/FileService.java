package com.online.edu.oss.service;

import org.springframework.web.multipart.MultipartFile;



public interface FileService {
    String upload(MultipartFile file, String host) ;
}
