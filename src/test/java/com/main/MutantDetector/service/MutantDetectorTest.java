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

    // -------- isMutant: TRUE --------
    @ParameterizedTest
    @MethodSource("dnaMutantProvider")
    void testIsMutant_true(String[] dna) throws InvalidDnaException {
        boolean result = detector.isMutant(dna);
        assertTrue(result, "Debería ser mutante");
    }

    // -------- isMutant: FALSE --------
    @ParameterizedTest
    @MethodSource("dnaHumanProvider")
    void testIsMutant_false(String[] dna) throws InvalidDnaException {
        boolean result = detector.isMutant(dna);
        assertFalse(result, "Debería ser humano (no mutante)");
    }

    // -------- isMutant: Dna inválido --------
    @Test
    public void testInvalidDnaCharacters() throws InvalidDnaException {
        String[] dna = {
                "AAA",
                "TTT",
                "CCC"
        };
        assertFalse(detector.isMutant(dna));
    }

    // -------- isValidDna: TRUE --------
    @ParameterizedTest
    @MethodSource("dnaValidProvider")
    void testIsValidDna_true(String[] dna) throws InvalidDnaException {
        boolean result = detector.isValidDna(dna);
        assertTrue(result, "El ADN debería ser válido");
    }

    // -------- isValidDna: InvalidDnaException --------
    @ParameterizedTest
    @MethodSource("dnaInvalidProvider")
    void testIsValidDna_Exception(String[] dna) {
        assertThrows(
                InvalidDnaException.class,
                () -> detector.isValidDna(dna)
        );
    }

    // -------- isMutant: InvalidDnaException --------
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
                Arguments.of((Object) new String[]{
                        "AAAA",
                        "TTTT",
                        "CCCC",
                        "GGGG"
                }),
                Arguments.of((Object) new String[]{
                        "ATGCGA",
                        "CAGTGC",
                        "TTATGT",
                        "AGAAGG",
                        "CCCCTA",
                        "TCACTG"
                }),
                Arguments.of((Object) new String[]{
                        "ATGC",
                        "AGGT",
                        "ATGT",
                        "AGGA"
                }),
                Arguments.of((Object) new String[]{
                        "ATGCGA",
                        "CAGTCC",
                        "TTATGT",
                        "ATAAGG",
                        "CCTCTA",
                        "TCATTG"
                }),
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
                Arguments.of((Object) new String[]{
                        "ACGT",
                        "TGCA",
                        "ACGT",
                        "TGCA"}),
                Arguments.of((Object) new String[]{
                        "ATAT",
                        "CGCG",
                        "ATAT",
                        "CGCG"}),
                Arguments.of((Object) new String[]{
                        "ATAT",
                        "AGCG",
                        "ATAT",
                        "AGCG"}),
                Arguments.of((Object) new String[]{
                        "ATAT",
                        "CGTG",
                        "ATAT",
                        "TGCG"}),
                Arguments.of((Object) new String[]{
                        "TTTT",
                        "CGCG",
                        "ATAT",
                        "CGCG"}),
                Arguments.of((Object) new String[]{
                        "GTAT",
                        "CGCG",
                        "ATGT",
                        "CGCG"})
        );
    }

    static Stream<Arguments> dnaValidProvider() {
        return Stream.of(
                Arguments.of((Object) new String[]{
                        "ACGT",
                        "ACGT",
                        "ACGT",
                        "ACGT"}),
                Arguments.of((Object) new String[]{
                        "ATGC",
                        "CAGT",
                        "TTAT",
                        "AGAA"})
        );
    }

    static Stream<Arguments> dnaInvalidProvider() {
        return Stream.of(
                Arguments.of((Object) new String[]{
                        "ACGT",
                        "ACGT",
                        "ACGT"}),
                Arguments.of((Object) new String[]{
                        "ATGC",
                        "CXGT",
                        "TTBT",
                        "AGAA"}),
                Arguments.of((Object) new String[]{""}),
                Arguments.of((Object) null)
        );
    }
}
