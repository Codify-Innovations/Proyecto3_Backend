package com.project.demo.logic.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.project.demo.logic.entity.user.User;
import java.io.IOException;

public class UsuarioIdSerializer extends JsonSerializer<User> {
    @Override
    public void serialize(User usuario, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeNumber(usuario.getId());
    }
}