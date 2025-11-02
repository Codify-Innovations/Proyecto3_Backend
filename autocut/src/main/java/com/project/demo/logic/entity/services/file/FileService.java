package com.project.demo.logic.entity.services.file;

import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface FileService {
    Map<String, Object> uploadImage( MultipartFile file, String folderName);
    Map<String, Object> uploadVideo( MultipartFile file, String folderName);
    Map<String, Object> uploadAudio( MultipartFile file, String folderName);
}