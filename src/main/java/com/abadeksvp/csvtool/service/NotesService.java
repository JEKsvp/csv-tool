package com.abadeksvp.csvtool.service;

import org.springframework.web.multipart.MultipartFile;

import com.abadeksvp.csvtool.model.NoteFile;
import com.abadeksvp.csvtool.model.NoteFilter;

public interface NotesService {

    void uploadNotes(MultipartFile file);

    NoteFile downloadNotes(NoteFilter filter);

    void deleteNotes();
}
