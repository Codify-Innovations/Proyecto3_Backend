package com.project.demo.logic.entity.services.videoGeneradoIA;

import com.project.demo.logic.entity.videoGeneradoIA.VideoGeneradoIA;
import com.project.demo.logic.entity.videoGeneradoIA.VideoGeneradoIARepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VideoGeneradoIAServiceImpl implements VideoGeneradoIAService {
    @Autowired
    private VideoGeneradoIARepository videoGeneradoIARepository;

    @Override
    public VideoGeneradoIA save(VideoGeneradoIA videoGeneradoIA) {
        return videoGeneradoIARepository.save(videoGeneradoIA);
    }
}
