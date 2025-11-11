package com.project.demo.logic.entity.media;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.io.IOException;

@RestController
@RequestMapping("/api/media")
public class MediaController {

    private final RestTemplate restTemplate = new RestTemplate();

    @PostMapping("/analyze")
    public ResponseEntity<?> analyze(@RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("No se envió ningún archivo.");
            }

      
            String contentType = file.getContentType();
            if (contentType == null || !(
                contentType.startsWith("image/") ||
                contentType.startsWith("video/") ||
                contentType.startsWith("audio/"))) {
                return ResponseEntity.badRequest().body("Formato no soportado (solo imagen, video o audio).");
            }

        
            if (file.getSize() > 50 * 1024 * 1024) {
                return ResponseEntity.badRequest().body("El archivo excede 50MB.");
            }
        
   String pythonApiUrl = "http://127.0.0.1:8000/api/analyze";


            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("file", new org.springframework.core.io.ByteArrayResource(file.getBytes()) {
                @Override
                public String getFilename() {
                    return file.getOriginalFilename();
                }
            });

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

            ResponseEntity<String> response = restTemplate.postForEntity(pythonApiUrl, requestEntity, String.class);

            return ResponseEntity.ok(response.getBody());

        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("Error al leer el archivo: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Error procesando el análisis: " + e.getMessage());
        }
    }
}
