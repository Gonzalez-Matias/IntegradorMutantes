package com.main.MutantDetector.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Schema(description = "Estadísticas de la base de datos de DNAs.")
@Builder
public record StatsResponseDTO(
        long cantHumanos,
        long cantMutantes,
        double ratio
) {
}
