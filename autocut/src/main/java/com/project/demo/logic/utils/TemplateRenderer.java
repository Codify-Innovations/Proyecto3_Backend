package com.project.demo.logic.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Map;

@Component
public class TemplateRenderer {

    private final TemplateEngine templateEngine;

    @Autowired
    public TemplateRenderer(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    /**
     * Renderiza un template Thymeleaf y devuelve el HTML resultante como String.
     *
     * @param templateName  Nombre del archivo dentro de resources/templates/ sin extensión
     * @param variables     Mapa con datos dinámicos para insertar en el template
     * @return HTML listo para convertir a PDF o enviar al frontend
     */
    public String render(String templateName, Map<String, Object> variables) {

        Context context = new Context();

        if (variables != null) {
            variables.forEach(context::setVariable);
        }

        return templateEngine.process(templateName, context);
    }
}
