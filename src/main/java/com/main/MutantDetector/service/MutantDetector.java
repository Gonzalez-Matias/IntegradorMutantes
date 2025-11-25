package com.main.MutantDetector.service;

import com.main.MutantDetector.exceptions.InvalidDnaException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MutantDetector {
    private static final int SEQUENCE_LENGTH = 4;
    private static final Set<Character> VALID_BASES = Set.of('A', 'T', 'C', 'G');

    public boolean isMutant(String[] dna) throws InvalidDnaException {
        if (!isValidDna(dna)) return false;

        final int n = dna.length;
        int sequenceCount = 0;  // Contador de secuencias de 4 caracteres iguales encontradas

        // Conversión a char[][] (Optimización #1)
        char[][] matrix = new char[n][];
        for (int i = 0; i < n; i++) {
            matrix[i] = dna[i].toCharArray();
        }

        // Single Pass: recorrer UNA SOLA VEZ (Optimización #2)
        for (int row = 0; row < n; row++) {
            for (int col = 0; col < n; col++) {

                // Verificación horizontal: solo si hay espacio suficiente hacia la derecha
                if (col <= n - SEQUENCE_LENGTH) {
                    if (checkHorizontal(matrix, row, col)) {
                        sequenceCount++;
                        if (sequenceCount > 1) return true;  // Early Termination!
                    }
                }

                // Verificación vertical: solo si hay espacio suficiente hacia abajo
                if (row <= n - SEQUENCE_LENGTH) {
                    if (checkVertical(matrix, row, col)) {
                        sequenceCount++;
                        if (sequenceCount > 1) return true;  // Early Termination!
                    }
                }

                // Verificación diagonal ascendente (de abajo-izquierda a arriba-derecha)
                if (row > n - SEQUENCE_LENGTH && col <= n - SEQUENCE_LENGTH) {
                    if (checkDiagonalAsc(matrix, row, col)) {
                        sequenceCount++;
                        if (sequenceCount > 1) return true;  // Early Termination!
                    }
                }

                // Verificación diagonal descendente (de arriba-izquierda a abajo-derecha)
                if (row <= n - SEQUENCE_LENGTH && col <= n - SEQUENCE_LENGTH) {
                    if (checkDiagonalDec(matrix, row, col)) {
                        sequenceCount++;
                        if (sequenceCount > 1) return true;  // Early Termination!
                    }
                }
            }
        }
        return false;
    }

    private boolean checkHorizontal(char[][] matrix, int row, int col) {
        final char base = matrix[row][col];
        // Evita contar secuencias superpuestas: si el carácter anterior es igual, ya fue contada
        if (col != 0 && matrix[row][col - 1] == base){
            return false;
        }

        // Verifica que los siguientes 3 caracteres sean iguales al actual
        return matrix[row][col + 1] == base &&
               matrix[row][col + 2] == base &&
               matrix[row][col + 3] == base;
    }
    private boolean checkVertical(char[][] matrix, int row, int col) {
        final char base = matrix[row][col];
        // Evita contar secuencias superpuestas: si el carácter superior es igual, ya fue contada
        if (row != 0 && matrix[row - 1][col] == base){
            return false;
        }

        // Verifica que los siguientes 3 caracteres hacia abajo sean iguales al actual
        return matrix[row + 1][col] == base &&
               matrix[row + 2][col] == base &&
               matrix[row + 3][col] == base;
    }

    private boolean checkDiagonalAsc(char[][] matrix, int row, int col) {
        final char base = matrix[row][col];
        // Evita contar secuencias superpuestas: si el carácter diagonal anterior es igual, ya fue contada
        if (row != 0 && col != 0 && matrix[row - 1][col - 1] == base){
            return false;
        }

        // Verifica que los siguientes 3 caracteres en diagonal ascendente sean iguales
        return matrix[row - 1][col + 1] == base &&
               matrix[row - 2][col + 2] == base &&
               matrix[row - 3][col + 3] == base;
    }

    private boolean checkDiagonalDec(char[][] matrix, int row, int col) {
        final char base = matrix[row][col];
        // Evita contar secuencias superpuestas: si el carácter diagonal anterior es igual, ya fue contada
        if (row != (matrix.length - 1) && col != 0 && matrix[row + 1][col - 1] == base){
            return false;
        }

        // Verifica que los siguientes 3 caracteres en diagonal descendente sean iguales
        return matrix[row + 1][col + 1] == base &&
               matrix[row + 2][col + 2] == base &&
               matrix[row + 3][col + 3] == base;
    }

    public boolean isValidDna(String[] dna) throws InvalidDnaException {
        try {
            String letrasString = String.join("", dna);
            if (letrasString.isBlank())
                throw new InvalidDnaException("El DNA no puede estar vacío");
            if (letrasString.length()/dna.length != dna.length)
                throw new InvalidDnaException("El DNA debe ser una matriz cuadrada.");

            Set<Character> letras = letrasString.chars()
                    .mapToObj(letra -> (char)letra)
                    .collect(Collectors.toSet());
            for (Character letra : letras){
                if (!VALID_BASES.contains(letra))
                    throw new InvalidDnaException("Las únicas letras permitidas son A, T, C y G.");
            }

            return dna.length >= SEQUENCE_LENGTH;
        } catch (Exception e) {
            throw new InvalidDnaException(e.getMessage());
        }
    }
}