package com.abadeksvp.csvtool.integration;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;

import com.abadeksvp.csvtool.model.entity.Note;
import com.abadeksvp.csvtool.repository.NoteRepository;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static com.abadeksvp.csvtool.integration.TestDataBuilder.buildExpectedCreatedNotes;
import static com.abadeksvp.csvtool.integration.TestDataBuilder.buildExpectedUpdatedNotes;

@TestMethodOrder(OrderAnnotation.class)
class NotesIntegrationTest extends IntegrationTest {

    @Autowired
    private NoteRepository repository;

    @Test
    @DisplayName("Uploaded notes should be saved in the database")
    @Order(1)
    void uploadingNotesTest() throws Exception {
        MockMultipartFile file = createTestFile("/initial-notes.csv");
        mockMvc.perform(multipart("/api/v1/notes/csv")
                        .file(file))
                .andExpect(status().isOk());
        List<Note> notes = repository.findAll();
        assertThat(notes)
                .hasSize(18)
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
                .containsAll(buildExpectedCreatedNotes());
    }

    @Test
    @DisplayName("Already existing notes should be updated in the database")
    @Order(2)
    void uploadingAlreadyExistingNotesTest() throws Exception {
        MockMultipartFile file = createTestFile("/updating-notes.csv");
        mockMvc.perform(multipart("/api/v1/notes/csv")
                        .file(file))
                .andExpect(status().isOk());
        List<Note> notes = repository.findAll();
        assertThat(notes)
                .hasSize(19)
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
                .containsAll(buildExpectedUpdatedNotes());
    }

    @Test
    @DisplayName("Downloaded notes should be the same as uploaded")
    @Order(3)
    void downloadingNotesTest() throws Exception {
        byte[] response = mockMvc.perform(get("/api/v1/notes/csv"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsByteArray();
        assertWithExpectedCsv("/updated-notes.csv", response);
    }

    @Test
    @DisplayName("Downloaded notes should be filtered by code")
    @Order(4)
    void downloadingFilteredNotesTest() throws Exception {
        byte[] response = mockMvc.perform(get("/api/v1/notes/csv?code=Type 4"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsByteArray();
        assertWithExpectedCsv("/filtered-by-code-notes.csv", response);
    }

    @Test
    @DisplayName("When notes are deleted, the database should be empty")
    @Order(5)
    void deleteNotesTest() throws Exception {
        mockMvc.perform(delete("/api/v1/notes/csv"))
                .andExpect(status().isOk());
        List<Note> notes = repository.findAll();
        assertThat(notes).isEmpty();
    }

    private void assertWithExpectedCsv(String expectedFilePath, byte[] response) throws IOException, CsvException {
        InputStream inputStream = getClass().getResourceAsStream(expectedFilePath);
        CSVReader expectedReader = new CSVReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        CSVReader actualReader = new CSVReader(
                new InputStreamReader(new ByteArrayInputStream(response), StandardCharsets.UTF_8));
        List<String[]> expected = expectedReader.readAll();
        List<String[]> actual = actualReader.readAll();
        assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);
    }

    private MockMultipartFile createTestFile(String filePath) throws IOException {
        InputStream inputStream = getClass().getResourceAsStream(filePath);
        return new MockMultipartFile("file", "notes.csv", "text/csv",
                inputStream.readAllBytes());
    }

}
