package com.project.demo.logic.entity.services.file;


import com.project.demo.logic.entity.services.cloudinary.CloudinaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@Service
public class FileServiceImpl implements FileService {

    @Autowired
    private CloudinaryService cloudinaryService;

    private static final List<String> IMAGE_TYPES = List.of("image/jpeg", "image/png", "image/webp");
    private static final List<String> VIDEO_TYPES = List.of("video/mp4", "video/webm", "video/ogg");
    private static final List<String> AUDIO_TYPES = List.of("audio/mpeg", "audio/wav", "audio/ogg");

    @Override
    public Map<String, Object> uploadImage(MultipartFile file, String folderName) {
        validateFields(file, folderName);
        validateFileType(file, IMAGE_TYPES);
        return uploadFile(file, folderName, "image");
    }

    @Override
    public Map<String, Object> uploadVideo(MultipartFile file, String folderName) {
        validateFields(file, folderName);
        validateFileType(file, VIDEO_TYPES);
        return uploadFile(file, folderName, "video");
    }

    @Override
    public Map<String, Object> uploadAudio(MultipartFile file, String folderName) {
        validateFields(file, folderName);
        validateFileType(file, AUDIO_TYPES);
        return uploadFile(file, folderName,"video");
    }

    /// =============== LLAMADA A SERVICIO DE CLOUDINARY =============== //
    public Map<String, Object> uploadFile(MultipartFile file, String folderName, String resourceType) {
        Map uploadResult = cloudinaryService.uploadFile(file, folderName, resourceType);
        String fileURL = (String) uploadResult.get("secure_url");

        if (fileURL == null) {
            throw new RuntimeException("Error al subir el archivo a Cloudinary.");
        }

        return Map.of("url", fileURL);
    }

    /// =============== VALIDACIONES DE FORMATO Y DE CAMPOS VACIOS =============== //
    private void validateFields( MultipartFile file, String folderName) {

        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("El archivo no puede estar vacío.");
        }

        if (folderName == null || folderName.isBlank()) {
            throw new IllegalArgumentException("El nombre del folder no puede estar vacío.");
        }
    }

    private void validateFileType(MultipartFile file, List<String> allowedTypes) {
        String type = file.getContentType();
        if (type == null || !allowedTypes.contains(type)) {
            throw new IllegalArgumentException("Formato no permitido: " + type);
        }
    }
}
