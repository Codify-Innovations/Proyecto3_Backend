package com.project.demo.logic.entity.services.videoGeneradoIA;

public record VideoGeneradoIADTO (
        Long userId,
        String imageUrls,
        String style,
        Integer duration,
        String videoUrl
) {}