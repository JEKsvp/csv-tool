package com.abadeksvp.csvtool.service.impl;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import com.abadeksvp.csvtool.model.entity.Note;
import com.abadeksvp.csvtool.service.impl.csv.CSVNoteRowMapper;

import static org.junit.jupiter.api.Assertions.*;

class CSVNoteRowMapperTest {

    @Test
    void testToNote() {
        String[] csvRow = {"ZIB", "ZIB001", "271636001", "Polsslag regelmatig", "The long description is necessary", "01-01-2019", "", "1"};
        Note note = CSVNoteRowMapper.toNote(csvRow);

        assertEquals("ZIB", note.getSource());
        assertEquals("ZIB001", note.getCodeListCode());
        assertEquals("271636001", note.getCode());
        assertEquals("Polsslag regelmatig", note.getDisplayValue());
        assertEquals("The long description is necessary", note.getLongDescription());
        assertEquals(LocalDate.of(2019, 1, 1), note.getFromDate());
        assertNull(note.getToDate());
        assertEquals(1, note.getSortingPriority());
    }

    @Test
    void testToRow() {
        Note note = Note.builder()
                .source("ZIB")
                .codeListCode("ZIB001")
                .code("271636001")
                .displayValue("Polsslag regelmatig")
                .longDescription("The long description is necessary")
                .fromDate(LocalDate.of(2019, 1, 1))
                .toDate(null)
                .sortingPriority(1L)
                .build();

        String[] csvRow = CSVNoteRowMapper.toRow(note);
        assertEquals("ZIB", csvRow[0]);
        assertEquals("ZIB001", csvRow[1]);
        assertEquals("271636001", csvRow[2]);
        assertEquals("Polsslag regelmatig", csvRow[3]);
        assertEquals("The long description is necessary", csvRow[4]);
        assertEquals("01-01-2019", csvRow[5]);
        assertEquals("", csvRow[6]); // toDate is null, so it should result in an empty string
        assertEquals("1", csvRow[7]);
    }

}