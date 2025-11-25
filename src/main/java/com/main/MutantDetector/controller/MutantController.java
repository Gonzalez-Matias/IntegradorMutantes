package com.main.MutantDetector.controller;

import com.main.MutantDetector.dto.DnaRequestDTO;
import com.main.MutantDetector.dto.DnaResponseDTO;
import com.main.MutantDetector.dto.StatsResponseDTO;
import com.main.MutantDetector.exceptions.InvalidDnaException;
import com.main.MutantDetector.service.MutantService;
import com.main.MutantDetector.service.StatsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
@RequiredArgsConstructor
@Tag(name = "Mutant Detector", description = "API para detección de mutantes")
public class MutantController {
    private final MutantService mutantService;
    private final StatsService statsService;

    // Endpoint POST /mutant - Verifica si una secuencia de ADN pertenece a un mutante
    @PostMapping("/mutant")
    @Operation(summary = "Verificar si un ADN es mutante")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Es mutante"),
            @ApiResponse(responseCode = "403", description = "No es mutante"),
            @ApiResponse(responseCode = "400", description = "ADN inválido")
    })
    public ResponseEntity<Void> comprobarMutante(@Valid @RequestBody DnaRequestDTO dnaRequest) throws InvalidDnaException {
        // Analiza el ADN y determina si es mutante o humano
        DnaResponseDTO dnaResponseDTO = mutantService.comprobarMutante(dnaRequest);
        // Retorna 200 OK si es mutante, 403 Forbidden si es humano
        return dnaResponseDTO.esMutante()? ResponseEntity.ok().build() : ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    // Endpoint GET /stats - Retorna estadísticas de todos los ADN analizados
    @GetMapping("/stats")
    @Operation(summary = "Obtener estadísticas de los DnaRecords guardados.")
    public ResponseEntity<StatsResponseDTO> getStats() {
        // Obtiene las estadísticas (cantidad de humanos, mutantes y ratio)
        StatsResponseDTO stats = statsService.getStats();
        return ResponseEntity.ok(stats);
    }

}
