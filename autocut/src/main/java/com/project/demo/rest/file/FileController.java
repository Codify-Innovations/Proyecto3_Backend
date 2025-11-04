package com.project.demo.rest.file;

import com.project.demo.logic.entity.services.file.FileService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.project.demo.logic.entity.http.GlobalResponseHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/files")
@CrossOrigin(origins = "*")
public class FileController {

    @Autowired
    private FileService fileService;

    @Autowired
    private GlobalResponseHandler responseHandler;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadFiles(
            @RequestParam("files") MultipartFile[] files,
            @RequestParam("folderName") String folderName,
            HttpServletRequest request
    ) {
        List<String> uploadedFileUrls = new ArrayList<>();
        for (MultipartFile file : files) {
            try {
                Map<String, Object> uploadResult = null;

                if (file.getContentType().startsWith("image/")) {
                    uploadResult = fileService.uploadImage(file, folderName);
                } else if (file.getContentType().startsWith("video/")) {
                    uploadResult = fileService.uploadVideo(file, folderName);
                } else if (file.getContentType().startsWith("audio/")) {
                    uploadResult = fileService.uploadAudio(file, folderName);
                } else {
                    return ResponseEntity.badRequest().body("Tipo de archivo no soportado.");
                }

                String fileURL = (String) uploadResult.get("url");
                if (fileURL != null) {
                    uploadedFileUrls.add(fileURL);
                }
            } catch (Exception e) {
                throw new RuntimeException("Error al subir el archivo: " + e.getMessage(), e);
            }
        }
        if (uploadedFileUrls.isEmpty()) {
            throw new RuntimeException("No se pudo subir ningún archivo.");
        }
        return responseHandler.handleResponse("Archivos subidos correctamente", uploadedFileUrls, HttpStatus.OK, request);
    }
}

