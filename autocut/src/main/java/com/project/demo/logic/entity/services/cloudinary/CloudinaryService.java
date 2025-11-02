package com.project.demo.logic.entity.services.cloudinary;

import org.springframework.web.multipart.MultipartFile;
import java.util.Map;

public interface CloudinaryService {
    Map uploadFile(MultipartFile file, String folderName, String resourceType);
}
