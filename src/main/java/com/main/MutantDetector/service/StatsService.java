package com.main.MutantDetector.service;

import com.main.MutantDetector.dto.StatsResponseDTO;
import com.main.MutantDetector.repository.DnaRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StatsService {
    private final DnaRecordRepository dnaRecordRepository;

    public StatsResponseDTO getStats() {
        long dnaRecordHumanos =  dnaRecordRepository.countByEsMutante(false);
        long dnaRecordMutantes = dnaRecordRepository.countByEsMutante(true);
        double ratioMutantes = (double) dnaRecordMutantes /dnaRecordHumanos;
        return StatsResponseDTO.builder()
                .cantHumanos(dnaRecordHumanos)
                .cantMutantes(dnaRecordMutantes)
                .ratio(ratioMutantes)
                .build();
    }
}
