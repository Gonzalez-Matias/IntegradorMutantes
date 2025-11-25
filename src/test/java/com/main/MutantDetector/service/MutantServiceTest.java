package com.main.MutantDetector.service;

import com.main.MutantDetector.dto.DnaRequestDTO;
import com.main.MutantDetector.dto.DnaResponseDTO;
import com.main.MutantDetector.exceptions.InvalidDnaException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MutantServiceTest {
    @Autowired
    private MutantService mutantService;

    // Test: comprobarMutante retorna esMutante=true para diferentes secuencias de ADN mutante
    @ParameterizedTest
    @MethodSource("dnaMutantProvider")
    void testComprobarMutante_true(String[] dna) throws InvalidDnaException {
        DnaRequestDTO dnaRequestDTO = new DnaRequestDTO(dna);
        DnaResponseDTO dnaResponseDTO = mutantService.comprobarMutante(dnaRequestDTO);
        assertTrue(dnaResponseDTO.esMutante());
    }

    // Test: comprobarMutante retorna esMutante=false para diferentes secuencias de ADN humano
    @ParameterizedTest
    @MethodSource("dnaHumanProvider")
    void testIsMutant_false(String[] dna) throws InvalidDnaException {
        DnaRequestDTO dnaRequestDTO = new DnaRequestDTO(dna);
        DnaResponseDTO dnaResponseDTO = mutantService.comprobarMutante(dnaRequestDTO);
        assertFalse(dnaResponseDTO.esMutante());
    }

    // Test: comprobarMutante retorna esMutante=true y guarda el registro en base de datos
    @Test
    void testComprobarMutante_trueFromH2() throws InvalidDnaException {
        String[] dna = {
                "AAAA",
                "TTTT",
                "CCCC",
                "GGGG"
        };
        DnaRequestDTO dnaRequestDTO = new DnaRequestDTO(dna);
        DnaResponseDTO dnaResponseDTO = mutantService.comprobarMutante(dnaRequestDTO);
        assertTrue(dnaResponseDTO.esMutante());
    }
    static Stream<Arguments> dnaMutantProvider() {
        return Stream.of(
                // Caso 1: Mutante con secuencias horizontales de 4 caracteres iguales
                Arguments.of((Object) new String[]{
                        "AAAA",
                        "TTTT",
                        "CCCC",
                        "GGGG"
                }),
                // Caso 2: Mutante con matriz 6x6
                Arguments.of((Object) new String[]{
                        "ATGCGA",
                        "CAGTGC",
                        "TTATGT",
                        "AGAAGG",
                        "CCCCTA",
                        "TCACTG"
                }),
                // Caso 3: Mutante con matriz 4x4
                Arguments.of((Object) new String[]{
                        "ATGC",
                        "AGGT",
                        "ATGT",
                        "AGGA"
                }),
                // Caso 4: Mutante con múltiples secuencias diagonales descendentes en matriz 6x6
                Arguments.of((Object) new String[]{
                        "ATGCGA",
                        "CAGTCC",
                        "TTATGT",
                        "ATAAGG",
                        "CCTCTA",
                        "TCATTG"
                }),
                // Caso 5: Mutante con secuencia horizontal y diagonal en matriz 6x6
                Arguments.of((Object) new String[]{
                        "ATGAGA",
                        "CAATAC",
                        "TAGTGT",
                        "AGTAGG",
                        "CTGCTA",
                        "TCACTG"
                })

        );
    }

    static Stream<Arguments> dnaHumanProvider() {
        return Stream.of(
                // Caso 1: Humano con secuencias alternadas sin 4 caracteres consecutivos
                Arguments.of((Object) new String[]{
                        "ACGT",
                        "TGCA",
                        "ACGT",
                        "TGCA"}),
                // Caso 2: Humano con solo una secuencia vertical de 4 caracteres iguales
                Arguments.of((Object) new String[]{
                        "ATAT",
                        "CGCG",
                        "ATAT",
                        "CGCG"}),
                // Caso 3: Humano con solo una secuencia diagonal ascendente de 4 caracteres iguales
                Arguments.of((Object) new String[]{
                        "ATAT",
                        "AGCG",
                        "ATAT",
                        "AGCG"}),
                // Caso 4: Humano con solo una secuencia diagonal descendente de 4 caracteres iguales
                Arguments.of((Object) new String[]{
                        "ATAT",
                        "CGTG",
                        "ATAT",
                        "TGCG"}),
                // Caso 5: Humano con solo una secuencia horizontal de 4 caracteres iguales
                Arguments.of((Object) new String[]{
                        "TTTT",
                        "CGCG",
                        "ATAT",
                        "CGCG"}),
                // Caso 6: Humano con solo una secuencia diagonal descendente de 4 caracteres iguales
                Arguments.of((Object) new String[]{
                        "GTAT",
                        "CGCG",
                        "ATGT",
                        "CGCG"})
        );
    }
}