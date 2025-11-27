package com.project.demo.logic.entity.services.contenidoAnalizadoIA;

import com.project.demo.logic.entity.contenidoAnalizadoIA.ContenidoAnalizadoIA;
import com.project.demo.logic.entity.contenidoAnalizadoIA.ContenidoAnalizadoIARepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class ContenidoAnalizadoIAServiceImpl implements ContenidoAnalizadoIAService{

    @Autowired
    private ContenidoAnalizadoIARepository contenidoAnalizadoIARepository;

    @Override
    public ContenidoAnalizadoIA save(ContenidoAnalizadoIA contenidoAnalizadoIA) {
        return contenidoAnalizadoIARepository.save(contenidoAnalizadoIA);
    }
}
