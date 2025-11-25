package com.main.MutantDetector.service;

import com.main.MutantDetector.dto.DnaRequestDTO;
import com.main.MutantDetector.dto.DnaResponseDTO;
import com.main.MutantDetector.dto.StatsResponseDTO;
import com.main.MutantDetector.exceptions.InvalidDnaException;
import com.main.MutantDetector.repository.DnaRecordRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class StatsServiceTest {
    @Autowired
    private StatsService statsService;
    @Autowired
    private MutantService mutantService;
    @Autowired
    private DnaRecordRepository dnaRecordRepository;

    @BeforeAll
    void setUp() throws InvalidDnaException {
        dnaRecordRepository.deleteAll();
        for (String[] dna : dnaRecords()) {
            mutantService.comprobarMutante(new DnaRequestDTO(dna));
        }
    }

    @Test
    void testGetStats(){
        StatsResponseDTO statsResponse = statsService.getStats();
        assertEquals(1L, statsResponse.cantHumanos());
        assertEquals(2L, statsResponse.cantMutantes());
        assertEquals(2.0, statsResponse.ratio());
    }

    private List<String[]> dnaRecords() {
        return List.of(
                new String[]{
                        "AAAA",
                        "TTTT",
                        "CCCC",
                        "GGGG"
                },
                new String[]{
                        "AAAA",
                        "TTTT",
                        "CCCC",
                        "GGGG"
                },
                new String[]{
                        "ACGT",
                        "TGGC",
                        "AACT",
                        "TGAC"
                },
                new String[]{
                        "ACGT",
                        "ACGT",
                        "ACGT",
                        "ACGT"
                }
        );
    }
}