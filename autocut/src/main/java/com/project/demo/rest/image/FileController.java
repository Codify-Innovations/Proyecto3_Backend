package com.project.demo.rest.image;

import com.project.demo.logic.entity.services.file.FileService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.project.demo.logic.entity.http.GlobalResponseHandler;

import java.util.Map;

@RestController
@RequestMapping("/api/files")
@CrossOrigin(origins = "*")
public class FileController {

    @Autowired
    private FileService fileService;

    @Autowired
    private GlobalResponseHandler responseHandler;

    // Subir imagen
    @PostMapping("/upload/image")
    public ResponseEntity<?> uploadImage(
            @RequestParam("file") MultipartFile file,
            @RequestParam("folderName") String folderName,
            HttpServletRequest request
    ) {
        Map<String, Object> result = fileService.uploadImage(file, folderName);
        return responseHandler.handleResponse("Imagen subida correctamente", result, HttpStatus.OK, request);
    }

    // Subir video
    @PostMapping("/upload/video")
    public ResponseEntity<?> uploadVideo(
            @RequestParam("file") MultipartFile file,
            @RequestParam("folderName") String folderName,
            HttpServletRequest request
    ) {
        Map<String, Object> result = fileService.uploadVideo(file, folderName);
        return responseHandler.handleResponse("Video subido correctamente", result, HttpStatus.OK, request);
    }

    // Subir audio
    @PostMapping("/upload/audio")
    public ResponseEntity<?> uploadAudio(
            @RequestParam("file") MultipartFile file,
            @RequestParam("folderName") String folderName,
            HttpServletRequest request
    ) {
        Map<String, Object> result = fileService.uploadAudio(file, folderName);
        return responseHandler.handleResponse("Audio subido correctamente", result, HttpStatus.OK, request);
    }
}
