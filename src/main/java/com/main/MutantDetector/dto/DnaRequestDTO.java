package com.main.MutantDetector.dto;

import com.main.MutantDetector.validation.ValidDnaSequence;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Schema(description = "DTO que contiene el DNA.")
public record DnaRequestDTO(
        @Schema(
                description = "Secuencia de ADN representada como matriz NxN",
                example = "[\"ATGCGA\",\"CAGTGC\",\"TTATGT\",\"AGAAGG\",\"CCCCTA\",\"TCACTG\"]")
        @NotNull(message = "La cadena de DNA no puede ser nula.")
        @NotEmpty(message = "La cadena de DNA no puede estar vacía.")
        @ValidDnaSequence
        String[] dna
) {
}
