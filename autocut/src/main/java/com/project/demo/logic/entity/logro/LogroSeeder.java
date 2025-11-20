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

        crearLogroSiNoExiste("Cazador JDM", "Detecta 5 vehículos de la categoría JDM.", "JDM", "categoria_count", 5, "https://res.cloudinary.com/dzejxb251/image/upload/v1763410555/CAZADOR_JDM_ygdaso.png");

        crearLogroSiNoExiste("Maestro Supercar", "Detecta 5 vehículos de la categoría Supercar.", "Supercar", "categoria_count", 5, "https://res.cloudinary.com/dzejxb251/image/upload/v1763415585/MAESTRO_SUPERCAR_ebiqsj.png");

        crearLogroSiNoExiste("Entusiasta Muscle", "Identifica 2 vehículos de la categoría Muscle.", "muscle", "categoria_count", 2, "https://res.cloudinary.com/dzejxb251/image/upload/v1763415585/ENTUSIASTA_MUSCLE_kguvmq.png");

        crearLogroSiNoExiste("Maestro Porsche", "Registra 3 vehículos de la marca Porsche.", "porsche", "marca_count", 3, "https://res.cloudinary.com/dzejxb251/image/upload/v1763416982/MAESTRO_PORSCHE_toxtjp.png");

        crearLogroSiNoExiste("Cazador Hypercar", "Registra 1 vehículo de la categoría Hypercar.", "hypercar", "categoria_count", 1, "https://res.cloudinary.com/dzejxb251/image/upload/v1763415585/CAZADOR_HYPERCAR_soxt8i.png");

        crearLogroSiNoExiste("Maestro Off-Road", "Detecta 2 vehículos de la categoría Off-Road.", "off-road", "categoria_count", 2, "https://res.cloudinary.com/dzejxb251/image/upload/v1763417820/FANATICO_OFF_ROAD_gm2e07.png");
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