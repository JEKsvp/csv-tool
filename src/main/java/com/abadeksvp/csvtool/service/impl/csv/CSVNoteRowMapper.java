package com.abadeksvp.csvtool.service.impl.csv;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.apache.commons.lang3.StringUtils;

import com.abadeksvp.csvtool.model.entity.Note;

import lombok.experimental.UtilityClass;

import static com.abadeksvp.csvtool.service.impl.csv.CSVHeader.CODE;
import static com.abadeksvp.csvtool.service.impl.csv.CSVHeader.CODE_LIST_CODE;
import static com.abadeksvp.csvtool.service.impl.csv.CSVHeader.DISPLAY_VALUE;
import static com.abadeksvp.csvtool.service.impl.csv.CSVHeader.FROM_DATE;
import static com.abadeksvp.csvtool.service.impl.csv.CSVHeader.LONG_DESCRIPTION;
import static com.abadeksvp.csvtool.service.impl.csv.CSVHeader.SORTING_PRIORITY;
import static com.abadeksvp.csvtool.service.impl.csv.CSVHeader.SOURCE;
import static com.abadeksvp.csvtool.service.impl.csv.CSVHeader.TO_DATE;

@UtilityClass
public class CSVNoteRowMapper {

    public static Note toNote(String[] strings) {
        return Note.builder()
                .source(SOURCE.name(strings))
                .codeListCode(CODE_LIST_CODE.name(strings))
                .code(CODE.name(strings))
                .displayValue(DISPLAY_VALUE.name(strings))
                .longDescription(LONG_DESCRIPTION.name(strings))
                .fromDate(formatDate(FROM_DATE.name(strings)))
                .toDate(formatDate(TO_DATE.name(strings)))
                .sortingPriority(parsePriority(SORTING_PRIORITY.name(strings)))
                .build();
    }

    private static Long parsePriority(String priority) {
        if (StringUtils.isEmpty(priority)) {
            return null;
        }
        return Long.parseLong(priority);
    }

    private static LocalDate formatDate(String date) {
        if (StringUtils.isEmpty(date)) {
            return null;
        }
        return LocalDate.parse(date, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
    }

    public static String[] toRow(Note note) {
        return new String[]{
                note.getSource(),
                note.getCodeListCode(),
                note.getCode(),
                note.getDisplayValue(),
                note.getLongDescription(),
                formatDate(note.getFromDate()),
                formatDate(note.getToDate()),
                formatPriority(note.getSortingPriority())
        };
    }

    private String formatPriority(Long sortingPriority) {
        if (sortingPriority == null) {
            return "";
        }
        return sortingPriority.toString();
    }


    private static String formatDate(LocalDate date) {
        if (date == null) {
            return "";
        }
        return date.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
    }

}
