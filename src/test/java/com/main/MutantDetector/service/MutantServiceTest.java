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

    @ParameterizedTest
    @MethodSource("dnaMutantProvider")
    void testComprobarMutante_true(String[] dna) throws InvalidDnaException {
        DnaRequestDTO dnaRequestDTO = new DnaRequestDTO(dna);
        DnaResponseDTO dnaResponseDTO = mutantService.comprobarMutante(dnaRequestDTO);
        assertTrue(dnaResponseDTO.esMutante());
    }

    // -------- isMutant: FALSE --------
    @ParameterizedTest
    @MethodSource("dnaHumanProvider")
    void testIsMutant_false(String[] dna) throws InvalidDnaException {
        DnaRequestDTO dnaRequestDTO = new DnaRequestDTO(dna);
        DnaResponseDTO dnaResponseDTO = mutantService.comprobarMutante(dnaRequestDTO);
        assertFalse(dnaResponseDTO.esMutante());
    }

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
}