package com.abadeksvp.csvtool.service;

import java.io.InputStream;
import java.util.Collection;

import com.abadeksvp.csvtool.model.NoteFile;
import com.abadeksvp.csvtool.model.entity.Note;

public interface NoteConverter {

    Collection<Note> parseNotes(InputStream fileContent);

    NoteFile toFile(Collection<Note> notes);
}
