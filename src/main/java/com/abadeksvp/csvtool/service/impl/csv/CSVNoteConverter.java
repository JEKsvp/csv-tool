package com.abadeksvp.csvtool.service.impl.csv;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import com.abadeksvp.csvtool.model.NoteFile;
import com.abadeksvp.csvtool.model.entity.Note;
import com.abadeksvp.csvtool.service.NoteConverter;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import lombok.SneakyThrows;

@Service
public class CSVNoteConverter implements NoteConverter {

    private static final String FILE_NAME = "notes.csv";
    private static final MediaType CONTENT_TYPE = MediaType.parseMediaType("text/csv");

    @Override
    @SneakyThrows
    public Collection<Note> parseNotes(InputStream fileContent) {
        InputStreamReader inputStreamReader = new InputStreamReader(fileContent, StandardCharsets.UTF_8);
        try (CSVReader reader = new CSVReader(inputStreamReader)) {
            return parseNotes(reader);
        }
    }

    private List<Note> parseNotes(CSVReader reader) {
        List<Note> notes = new ArrayList<>();
        Iterator<String[]> iterator = reader.iterator();
        iterator.next(); // skip header
        while (iterator.hasNext()) {
            String[] row = iterator.next();
            CSVRowValidator.validate(row);
            Note note = CSVNoteRowMapper.toNote(row);
            notes.add(note);
        }
        return notes;
    }

    @Override
    @SneakyThrows
    public NoteFile toFile(Collection<Note> notes) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            try (OutputStreamWriter writer = new OutputStreamWriter(out, StandardCharsets.UTF_8)) {
                try (CSVWriter csvWriter = new CSVWriter(writer)) {
                    writeNotes(notes, csvWriter);
                }
            }
            return NoteFile.builder()
                    .fileContent(new ByteArrayResource(out.toByteArray()))
                    .fileName(FILE_NAME)
                    .contentType(CONTENT_TYPE)
                    .build();
        }
    }

    private void writeNotes(Collection<Note> notes, CSVWriter csvWriter) throws IOException {
        csvWriter.writeNext(CSVHeader.headers());
        csvWriter.writeAll(notes.stream()
                .map(CSVNoteRowMapper::toRow)
                .toList());
        csvWriter.flush();
    }

}
