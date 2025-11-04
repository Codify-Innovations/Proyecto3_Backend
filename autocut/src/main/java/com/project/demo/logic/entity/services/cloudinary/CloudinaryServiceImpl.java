package com.project.demo.logic.entity.services.cloudinary;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class CloudinaryServiceImpl implements CloudinaryService {

    @Resource
    private Cloudinary cloudinary;

    @Override
    public Map uploadFile(MultipartFile file, String folderName, String resourceType) {
        try {
            Map<String, Object> options = new HashMap<>();
            options.put("folder", folderName);

            options.put("resource_type", resourceType != null ? resourceType : "auto");

            Transformation transformation = new Transformation()
                    .quality("auto")
                    .fetchFormat("auto");

            switch (resourceType) {
                case "image" -> transformation.width(1200).crop("limit");
                case "video" -> transformation.quality("auto");
                default -> {}
            }

            options.put("transformation", transformation);

            return cloudinary.uploader().upload(file.getBytes(), options);
        } catch (IOException e) {
            throw new RuntimeException("Error al subir el archivo a Cloudinary: ", e);
        }
    }
}
