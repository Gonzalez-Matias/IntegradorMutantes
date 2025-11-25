package com.main.MutantDetector.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class MutantControllerTest {
    @Autowired
    private MockMvc mockMvc;

    // Test: POST /mutant retorna 200 OK para ADN de mutante válido
    @Test
    public void testMutantEndpoint_ReturnOk() throws Exception {
        String dnaJson = """
            {
                "dna": ["ATGCGA",
                        "CAGTGC",
                        "TTATGT",
                        "AGAAGG",
                        "CCCCTA",
                        "TCACTG"]
            }
            """;

        mockMvc.perform(post("/mutant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(dnaJson))
                .andExpect(status().isOk());
    }

    // Test: POST /mutant retorna 403 Forbidden para ADN de humano válido
    @Test
    public void testHumanEndpoint_ReturnForbidden() throws Exception {
        String dnaJson = """
            {
                "dna": ["ATGCGA","CAGTGC","TTATTT","AGACGG","GCGTCA","TCACTG"]
            }
            """;

        mockMvc.perform(post("/mutant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(dnaJson))
                .andExpect(status().isForbidden());
    }

    // Test: POST /mutant retorna 400 Bad Request para ADN con caracteres inválidos
    @Test
    public void testInvalidDna_ReturnBadRequest() throws Exception {
        String dnaJson = """
            {
                "dna": ["ATGX","CAGT"]
            }
            """;

        mockMvc.perform(post("/mutant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(dnaJson))
                .andExpect(status().isBadRequest());
    }
    // Test: POST /mutant retorna 400 Bad Request para array de ADN con cadena vacía
    @Test
    public void testEmptyDna_ReturnBadRequest() throws Exception {
        String dnaJson = """
            {
                "dna": [""]
            }
            """;

        mockMvc.perform(post("/mutant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(dnaJson))
                .andExpect(status().isBadRequest());
    }

    // Test: POST /mutant retorna 400 Bad Request para formato de ADN incorrecto (string en lugar de array)
    @Test
    public void testIncorrectFormat_ReturnBadRequest() throws Exception {
        String dnaJson = """
            {
                "dna": ""
            }
            """;

        mockMvc.perform(post("/mutant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(dnaJson))
                .andExpect(status().isBadRequest());
    }

    // Test: POST /mutant retorna 400 Bad Request para cuerpo de petición vacío
    @Test
    public void testEmptyString_ReturnBadRequest() throws Exception {
        String dnaJson = "";

        mockMvc.perform(post("/mutant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(dnaJson))
                .andExpect(status().isBadRequest());
    }

    // Test: POST /mutant retorna 400 Bad Request para cuerpo de petición nulo
    @Test
    public void testNullDna_ReturnBadRequest() throws Exception {
        mockMvc.perform(post("/mutant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content((byte[]) null))
                .andExpect(status().isBadRequest());
    }

    // Test: GET /stats retorna 200 OK con estadísticas válidas (cantHumanos, cantMutantes, ratio)
    @Test
    public void testStatsEndpoint_ReturnOk() throws Exception {
        mockMvc.perform(get("/stats"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cantHumanos").exists())
                .andExpect(jsonPath("$.cantMutantes").exists())
                .andExpect(jsonPath("$.ratio").exists());
    }
}