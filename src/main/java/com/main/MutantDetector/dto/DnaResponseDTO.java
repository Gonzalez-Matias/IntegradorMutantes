package com.main.MutantDetector.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Respuesta si el DNA ingresado es mutante o no.")
public record DnaResponseDTO(
        Boolean esMutante
) {
}
