package com.main.MutantDetector.service;

import com.main.MutantDetector.dto.DnaRequestDTO;
import com.main.MutantDetector.dto.DnaResponseDTO;
import com.main.MutantDetector.entity.DnaRecord;
import com.main.MutantDetector.exceptions.DnaHashCalculationException;
import com.main.MutantDetector.exceptions.InvalidDnaException;
import com.main.MutantDetector.repository.DnaRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MutantService {
    private final DnaRecordRepository dnaRepository;
    private final MutantDetector mutantDetector;

    public DnaResponseDTO comprobarMutante(DnaRequestDTO dnaRequest) throws InvalidDnaException {
        // Calcula el hash del ADN para identificarlo de forma única
        String dnaHash = calcularDnaHash(dnaRequest.dna());

        // Verifica si el ADN ya fue analizado previamente (cacheo)
        Optional<DnaRecord> dnaRecordGuardado = dnaRepository.findByDnaHash(dnaHash);
        if (dnaRecordGuardado.isPresent()) {
            return new DnaResponseDTO(dnaRecordGuardado.get().getEsMutante());
        }

        // Si no está en cache, analiza el ADN y guarda el resultado
        Boolean esMutante = mutantDetector.isMutant(dnaRequest.dna());
        DnaRecord nuevoDnaRecord = new DnaRecord();
        nuevoDnaRecord.setDnaHash(dnaHash);
        nuevoDnaRecord.setEsMutante(esMutante);
        nuevoDnaRecord.setFechaCreacion(LocalDateTime.now());
        dnaRepository.save(nuevoDnaRecord);

        return new DnaResponseDTO(esMutante);
    }

    // Calcula el hash SHA-256 del ADN para usarlo como identificador único y evitar duplicados
    private String calcularDnaHash(String[] dna){
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            String dnaString = String.join("", dna);
            byte[] hashBytes = digest.digest(dnaString.getBytes(StandardCharsets.UTF_8));

            // Convierte el hash de bytes a string hexadecimal
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');  // Asegura formato de 2 dígitos
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new DnaHashCalculationException("Error calculando hash", e);
        }
    }

}
