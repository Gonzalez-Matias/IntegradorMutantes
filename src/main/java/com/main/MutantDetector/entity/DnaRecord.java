package com.main.MutantDetector.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "dna_records")
@Getter
@Setter
@NoArgsConstructor
public class DnaRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "dna_hash", unique = true, nullable = false)
    private String dnaHash;

    @Column(name = "is_mutant", nullable = false)
    private Boolean esMutante;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime fechaCreacion;
}