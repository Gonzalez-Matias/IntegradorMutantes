package com.main.MutantDetector.service;

import com.main.MutantDetector.exceptions.InvalidDnaException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.Arguments;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MutantDetectorTest {

    @Autowired
    private MutantDetector detector;

    // Test: isMutant retorna true para diferentes secuencias de ADN mutante
    @ParameterizedTest
    @MethodSource("dnaMutantProvider")
    void testIsMutant_true(String[] dna) throws InvalidDnaException {
        boolean result = detector.isMutant(dna);
        assertTrue(result, "Debería ser mutante");
    }

    // Test: isMutant retorna false para diferentes secuencias de ADN humano
    @ParameterizedTest
    @MethodSource("dnaHumanProvider")
    void testIsMutant_false(String[] dna) throws InvalidDnaException {
        boolean result = detector.isMutant(dna);
        assertFalse(result, "Debería ser humano (no mutante)");
    }

    // Test: isMutant retorna false para ADN con secuencias inválidas (menos de 4 caracteres)
    @Test
    public void testInvalidDnaCharacters() throws InvalidDnaException {
        String[] dna = {
                "AAA",
                "TTT",
                "CCC"
        };
        assertFalse(detector.isMutant(dna));
    }

    // Test: isValidDna retorna true para secuencias de ADN válidas
    @ParameterizedTest
    @MethodSource("dnaValidProvider")
    void testIsValidDna_true(String[] dna) throws InvalidDnaException {
        boolean result = detector.isValidDna(dna);
        assertTrue(result, "El ADN debería ser válido");
    }

    // Test: isValidDna lanza InvalidDnaException para secuencias de ADN inválidas
    @ParameterizedTest
    @MethodSource("dnaInvalidProvider")
    void testIsValidDna_Exception(String[] dna) {
        assertThrows(
                InvalidDnaException.class,
                () -> detector.isValidDna(dna)
        );
    }

    // Test: isMutant lanza InvalidDnaException para secuencias de ADN inválidas
    @ParameterizedTest
    @MethodSource("dnaInvalidProvider")
    void testIsMutant_Exception(String[] dna) {
        assertThrows(
                InvalidDnaException.class,
                () -> detector.isMutant(dna)
        );
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
                // Caso 5: Mutante con múltiples secuencias diagonales ascendentes en matriz 6x6
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
                // Caso 2: Humano con patrones repetitivos sin secuencias de mutante
                Arguments.of((Object) new String[]{
                        "ATAT",
                        "CGCG",
                        "ATAT",
                        "CGCG"}),
                // Caso 3: Humano con solo una secuencia vertical de 4 caracteres iguales
                Arguments.of((Object) new String[]{
                        "ATAT",
                        "AGCG",
                        "ATAT",
                        "AGCG"}),
                // Caso 4: Humano con solo una secuencia diagonal ascendente de 4 caracteres iguales
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

    static Stream<Arguments> dnaValidProvider() {
        return Stream.of(
                // Caso 1: ADN válido con matriz cuadrada 4x4 y caracteres válidos (A, C, G, T)
                Arguments.of((Object) new String[]{
                        "ACGT",
                        "ACGT",
                        "ACGT",
                        "ACGT"}),
                // Caso 2: ADN válido con matriz cuadrada 4x4 y caracteres válidos
                Arguments.of((Object) new String[]{
                        "ATGC",
                        "CAGT",
                        "TTAT",
                        "AGAA"})
        );
    }

    static Stream<Arguments> dnaInvalidProvider() {
        return Stream.of(
                // Caso 1: ADN inválido - matriz no cuadrada (3 filas en lugar de 4)
                Arguments.of((Object) new String[]{
                        "ACGT",
                        "ACGT",
                        "ACGT"}),
                // Caso 2: ADN inválido - contiene caracteres no válidos (X, B)
                Arguments.of((Object) new String[]{
                        "ATGC",
                        "CXGT",
                        "TTBT",
                        "AGAA"}),
                // Caso 3: ADN inválido - array con cadena vacía
                Arguments.of((Object) new String[]{""}),
                // Caso 4: ADN inválido - array nulo
                Arguments.of((Object) null)
        );
    }
}
