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

    // Mutante válido - 200 OK
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

    // Humano válido - 403 Forbidden
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

    // Input inválido - 400 Bad Request
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
    // Input inválido - 400 Bad Request
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

    // Input inválido - 400 Bad Request
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

    // Input inválido - 400 Bad Request
    @Test
    public void testEmptyString_ReturnBadRequest() throws Exception {
        String dnaJson = "";

        mockMvc.perform(post("/mutant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(dnaJson))
                .andExpect(status().isBadRequest());
    }

    // Input inválido - 400 Bad Request
    @Test
    public void testNullDna_ReturnBadRequest() throws Exception {
        mockMvc.perform(post("/mutant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content((byte[]) null))
                .andExpect(status().isBadRequest());
    }

    // Test stats - 200 Ok
    @Test
    public void testStatsEndpoint_ReturnOk() throws Exception {
        mockMvc.perform(get("/stats"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cantHumanos").exists())
                .andExpect(jsonPath("$.cantMutantes").exists())
                .andExpect(jsonPath("$.ratio").exists());
    }
}