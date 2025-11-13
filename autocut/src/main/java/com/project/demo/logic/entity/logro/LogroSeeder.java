package com.project.demo.logic.entity.logro;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Order(3)
@Component
public class LogroSeeder implements ApplicationListener<ContextRefreshedEvent> {

    private final LogroRepository logroRepository;

    public LogroSeeder(LogroRepository logroRepository) {
        this.logroRepository = logroRepository;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        crearLogrosIniciales();
    }

    private void crearLogrosIniciales() {

        crearLogroSiNoExiste("Cazador JDM", "Detecta 5 vehículos de la categoría JDM.", "JDM", "categoria_count", 5, "");

        crearLogroSiNoExiste("Maestro Supercar", "Detecta 5 vehículos de la categoría Supercar.", "Supercar", "categoria_count", 5, "");

        crearLogroSiNoExiste("Amante de Toyota", "Registra 5 vehículos de la marca Toyota.", "Toyota", "marca_count", 5, "");

        crearLogroSiNoExiste("Explorador Automotriz", "Registra un total de 20 vehículos, sin importar categoría.", null, "total_detectados", 20, "");

        crearLogroSiNoExiste("Coleccionista Élite", "Registra un total de 50 vehículos diferentes en tu colección.", null, "total_detectados", 50, "");

        crearLogroSiNoExiste("Aficionado a los Supercars", "Detecta 3 vehículos de la categoría Supercar.", "supercar", "categoria_count", 3, "");

        crearLogroSiNoExiste("Corazón Sports", "Registra 5 autos deportivos de la categoría Sports.", "sports", "categoria_count", 5, "");

        crearLogroSiNoExiste("Amante de los Muscle Cars", "Identifica 2 vehículos de la categoría Muscle.", "muscle", "categoria_count", 2, "");

        crearLogroSiNoExiste("Fan de Porsche", "Registra 2 vehículos de la marca Porsche.", "porsche", "marca_count", 2, "");

        crearLogroSiNoExiste("Cazador de Hypercars", "Registra 1 vehículo de la categoría Hypercar.", "hypercar", "categoria_count", 1, "");

        crearLogroSiNoExiste("Espíritu Off-Road", "Detecta 3 vehículos Off-Road.", "off-road", "categoria_count", 3, "");

        crearLogroSiNoExiste("Maestro de los Convertibles", "Identifica 2 convertibles de cualquier color.", "convertible", "categoria_count", 2, "");

    }

    private void crearLogroSiNoExiste(String nombre, String descripcion, String categoria, String criterio, int cantidadRequerida, String iconoUrl) {

        Optional<Logro> optional = logroRepository.findByNombre(nombre);

        if (optional.isPresent()) {
            return;
        }

        Logro logro = new Logro();
        logro.setNombre(nombre);
        logro.setDescripcion(descripcion);
        logro.setCategoria(categoria);
        logro.setCriterio(criterio);
        logro.setCantidadRequerida(cantidadRequerida);
        logro.setIconoUrl(iconoUrl);
        logro.setActivo(true);

        logroRepository.save(logro);
    }
}